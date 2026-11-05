# 開発ガイド

ごほうびスタンプの開発に必要な環境、タスク、コードの決まりごとをまとめます。

## 環境

| 項目 | バージョン |
| --- | --- |
| JDK | 21 |
| Gradle | 9.8 |
| Android Gradle Plugin | 9.5（Kotlin は AGP 組み込み） |
| Kotlin | 2.4 |
| compileSdk / targetSdk / minSdk | 37 / 36 / 26 |

ライブラリのバージョンは [gradle/libs.versions.toml](../gradle/libs.versions.toml) で管理しています。

## セットアップ

```bash
git clone git@github.com:cheesecomer/RewardStamp.git
cd RewardStamp

./gradlew help
```

初回の Gradle 実行で Git Hooks が作られます（[settings.gradle.kts](../settings.gradle.kts)）。

| Hook | 実行するタスク |
| --- | --- |
| pre-commit | `ktlintCheck` `detekt` |
| pre-push | `clean` `assembleDebug` `koverVerifyAll` |

## タスク

| 目的 | コマンド |
| --- | --- |
| フォーマット | `./gradlew ktlintFormat` |
| スタイル・静的解析 | `./gradlew ktlintCheck detekt` |
| Android Lint | `./gradlew lintDebug` |
| ユニットテスト | `./gradlew testDebugUnitTest` |
| スクリーンショットテスト | `./gradlew screenshotTest` |
| カバレッジ検証（95% 以上） | `./gradlew koverVerifyAll` |
| カバレッジレポート | `./gradlew koverHtmlReportAll` |
| ビルド | `./gradlew assembleDebug` |
| リリースビルド（Google Play 用） | `./gradlew bundleRelease` |

## リリースビルド

リポジトリ直下に `keystore.properties` を置くと、release ビルドが署名されます。このファイルとキーストアは `.gitignore` 済みで、コミットしません。ファイルがない場合、release は未署名でビルドされます。

```properties
storeFile=RewardStamp.jks
storePassword=...
keyAlias=...
keyPassword=...
```

`storeFile` はリポジトリ直下からの相対パスか、絶対パスで書きます。`./gradlew bundleRelease` を実行すると `app/build/outputs/bundle/release/app-release.aab` ができるので、これを Play Console にアップロードします。

## テスト

テストはすべて `app/src/test`（JVM 上）にあります。

| 種類 | 使うもの | 例 |
| --- | --- | --- |
| ViewModel / Repository | JUnit, Truth, coroutines-test, Mockito | `SheetDetailViewModelTest` |
| Compose UI | Robolectric, compose-ui-test | `SheetDetailContentTest` |
| スクリーンショット | Paparazzi | `RewardBoardViewScreenshotTest` |

### スクリーンショットテストを分けている理由

Paparazzi（layoutlib）と Robolectric は互いに衝突するネイティブライブラリを読み込むため、同じ JVM では動きません。そこでテストタスクを次のように分けています（[app/build.gradle.kts](../app/build.gradle.kts)）。

- `testDebugUnitTest`: `*ScreenshotTest` 以外
- `testReleaseUnitTest`（`screenshotTest`）: `*ScreenshotTest` のみ

Paparazzi を使うテストクラスの名前は、必ず `ScreenshotTest` で終わらせてください。

スクリーンショットテストは、独自描画（`RewardBoardView`）がクラッシュせずに描画できることを確認するためのものです。ゴールデン画像との比較はしていません。

### カバレッジ

Kover の `all` バリアントで、debug と release の結果を合算して計測します。そのため、カバレッジの検証やレポートには `koverVerifyAll` / `koverHtmlReportAll` を使います。

カバレッジから外したいもの（`Application`、DI 用のファクトリなど）には `@ExcludeFromCoverage` を付けます。`*Preview*` は自動で除外されます。

## CI

[.github/workflows/ci.yml](../.github/workflows/ci.yml) で、pull request と main への push ごとに次を実行します。

```bash
./gradlew ktlintCheck detekt testDebugUnitTest screenshotTest koverVerifyAll lintDebug assembleDebug
```

## パッケージ構成

```text
com.cheesecomer.rewardstamp
├── RewardStampApplication  # DB と Repository の生成（手動 DI）
├── MainActivity / App      # エントリポイントと全体の Scaffold
├── navigation/             # Route, NavHost, ScreenFactory
├── feature/                # 画面ごとのパッケージ
│   ├── sheet/{list,detail,edit}
│   ├── exchangeablesheet/list
│   ├── completedsheet/{list,detail}
│   └── setting
├── ui/
│   ├── component/          # 画面をまたいで使う composable
│   └── theme/              # RewardStampTheme
├── model/                  # ドメインモデル
├── data/
│   ├── repository/         # Repository（複数テーブルにまたがる更新は TransactionRunner で囲む）
│   └── source/database/    # Room（entity, dao, mapper）
└── annotation/             # @ExcludeFromCoverage
```

## コードの決まりごと

### 画面

状態を持つ画面は、次のファイルを `feature/<機能>/<画面>/` に置きます。

- `XxxViewModel`: `private val _uiState = MutableStateFlow(XxxUiState())` を持ち、`val uiState: StateFlow<XxxUiState>` として公開する
- `XxxUiState`: 画面の状態を表す data class
- `XxxScreen`: ViewModel を受け取り、`collectAsStateWithLifecycle()` で状態を読んで `XxxContent` に渡す
- `XxxContent`（`internal`）: 状態とコールバックだけを受け取る composable。テストとプレビューはこちらを使う

ViewModel は `rewardStampViewModel { XxxViewModel(rewardSheetRepository, ...) }` で生成します。

画面の骨格（トップバー、戻るボタン、アクション、FAB）には `RewardStampScaffold` を使います。

### 文字列

UI の文字列はすべて [strings.xml](../app/src/main/res/values/strings.xml) に置きます。

- 名前は `common_*`、`tab_*`、画面ごとの接頭辞（`sheet_detail_*` など）を付ける
- 数値を含む文字列は `%1$d` のようなフォーマット引数を使う
- 前後に空白がある文字列は `"あと "` のように引用符で囲む

### テストタグ

`testTag` の値は、composable と同じパッケージに置いた `internal object XxxTestTags` に定義します。composable とテストの両方からこの定数を参照し、文字列リテラルは直接書きません。

```kotlin
internal object CompletedSheetListTestTags {
    const val EMPTY_LIST = "CompletedSheetListScreen.EmptyList"
    const val GRID = "CompletedSheetListScreen.CompletedSheetGrid"

    fun gridItem(sheetId: Long) = "$GRID.$sheetId"
}
```

### Drawable

Compose から `Drawable` を読み込むときは `rememberDrawable(R.drawable.xxx)` を使います。
