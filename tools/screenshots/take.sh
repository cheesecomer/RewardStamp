#!/usr/bin/env bash
# アプリの画面のスクリーンショットを、見本のデータを入れた状態で撮る（docs/development.md「スクリーンショット」）。
#
#   tools/screenshots/take.sh [出力先]       # 既定の出力先は docs/screenshots（リポジトリに入れ、README とストアの掲載に使う）
#
# 1. デバッグ版とテストを Android Emulator に入れ、アプリのデータを消す
# 2. ScreenshotTour が見本のデータ（ShowcaseData）を入れ、アプリを操作して各画面を撮る（1080×1920）
#    スクロールする画面は、隠れている部分まで含めた画像も「名前-full.png」で撮る（高さは画面ごとに変わる）
# 3. 撮った画像を取り出し、出力先を置き換える。
#    失敗したときは出力先を変えず、撮れた分と失敗したときの画面（failed.png）を build/screenshots に残す
#
# 端末は ANDROID_SERIAL で選ぶ（既定は最初の Android Emulator）。アプリのデータを消すので、実機では動かさない。
# 日付とゴールのスタンプは固定されるので、何度撮っても同じ画面になる。ゴールのスタンプは SEED で変えられる（SEED=3 tools/screenshots/take.sh）。
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
OUT="${1:-$ROOT/docs/screenshots}"
WORK="$ROOT/build/screenshots"
PACKAGE="com.cheesecomer.rewardstamp"
APP_ID="$PACKAGE.debug" # デバッグ版は Google Play 版と別のアプリとして入る（app/build.gradle.kts の applicationIdSuffix）
DEVICE_DIR="/sdcard/Android/data/$APP_ID/files/screenshots"

if ! command -v adb >/dev/null; then
  SDK="${ANDROID_HOME:-$(sed -n 's/^sdk\.dir=//p' "$ROOT/local.properties" 2>/dev/null)}"
  PATH="$SDK/platform-tools:$PATH"
fi

export ANDROID_SERIAL="${ANDROID_SERIAL:-$(adb devices | awk '/^emulator-[0-9]+\tdevice$/ { print $1; exit }')}"
if [[ "$ANDROID_SERIAL" != emulator-* ]]; then
  echo "Android Emulator が見つからない（起動するか、ANDROID_SERIAL で指定する）" >&2
  exit 1
fi

mkdir -p "$OUT" "$WORK"
cleanup() {
  adb shell am broadcast -a com.android.systemui.demo -e command exit >/dev/null 2>&1 || true
  adb shell wm size reset >/dev/null 2>&1 || true
}
trap cleanup EXIT

# 1. アプリとテスト。前のデータを残さない
(cd "$ROOT" && ./gradlew -q :app:installDebug :app:installDebugAndroidTest)
adb shell pm clear "$APP_ID" >/dev/null

# Google Play の掲載用の比率（9:16。長辺が短辺の2倍を超える画像は載せられない）で撮る。終わったら戻す
adb shell wm size 1080x1920

# ステータスバーの時刻・電池・電波を固定し、通知を隠す（デモモード）
adb shell settings put global sysui_demo_allowed 1
demo() { adb shell am broadcast -a com.android.systemui.demo -e command "$@" >/dev/null; }
demo enter
demo clock -e hhmm 1000
demo battery -e level 100 -e plugged false
demo network -e wifi show -e level 4 -e fully true -e mobile hide
demo notifications -e visible false

# 2. 撮って取り出す。am instrument はテストが失敗しても 0 で終わるので、結果の行で判定する。
# 失敗したときも、そこまでに撮った画面と、失敗したときの画面（failed.png）を取り出す
seed_args=()
if [[ -n "${SEED:-}" ]]; then
  seed_args=(-e seed "$SEED")
fi
result="$(adb shell am instrument -w -e screenshots true ${seed_args[@]+"${seed_args[@]}"} -e class "$PACKAGE.screenshots.ScreenshotTour" \
  "$APP_ID.test/androidx.test.runner.AndroidJUnitRunner")"
rm -rf "$WORK"/*.png
adb pull "$DEVICE_DIR/." "$WORK" >/dev/null 2>&1 || true
if ! grep -q "^OK (1 test)" <<<"$result"; then
  echo "$result" >&2
  echo "スクリーンショットのテストが失敗した（$WORK/failed.png に失敗したときの画面）" >&2
  exit 1
fi
count="$(find "$WORK" -name '*.png' | wc -l | tr -d ' ')"
if [[ "$count" == 0 ]]; then
  echo "スクリーンショットが撮れていない（テストが飛ばされた可能性がある）" >&2
  exit 1
fi

# 3. 出力先を置き換える
find "$OUT" -name '*.png' -delete
cp "$WORK"/*.png "$OUT"
echo "$count 枚を $OUT に保存した"
