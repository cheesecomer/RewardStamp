# CLAUDE.md

ごほうびスタンプ（Android / Jetpack Compose）。単一モジュール `:app`。

開発環境、タスク、パッケージ構成、コードの決まりごとは [docs/development.md](docs/development.md) を参照。
だれが何のために使うか（おとなロックの対象を含む）は [docs/usecases.md](docs/usecases.md)、見た目の決まりは [docs/design/DESIGN.md](docs/design/DESIGN.md) を参照。

## 確認コマンド

変更後は CI と同じタスクを通す。

```bash
./gradlew ktlintCheck detekt testDebugUnitTest screenshotTest koverVerifyAll lintDebug assembleDebug
```

- フォーマット崩れは `./gradlew ktlintFormat` で直す
- 単体のテストクラスは `./gradlew testDebugUnitTest --tests '*.SheetDetailViewModelTest'`
- UI を変えたらエミュレータで表示を確認する（`adb` は `local.properties` の `sdk.dir` 配下の `platform-tools`）

## 注意点

- Paparazzi と Robolectric は同じ JVM で動かない。Paparazzi のテストクラス名は必ず `*ScreenshotTest` にする（`testReleaseUnitTest` だけで実行される）
- カバレッジは debug と release の合算（Kover の `all` バリアント）。`koverVerify` ではなく `koverVerifyAll` を使う。下限は 95%
- Kotlin は AGP 組み込み。`kotlin-android` プラグインは適用しない。JVM ターゲットは `kotlin { compilerOptions { ... } }` で指定する
- targetSdk は意図的に 36 のまま（compileSdk は 37）。上げるときは実機での確認が必要
- detekt の `MatchingDeclarationName` があるため、`XxxTestTags` などのトップレベル宣言は 1 ファイルに 1 つ
- UI の文字列は `strings.xml` に置く。`testTag` は `XxxTestTags` の定数を使い、リテラルを書かない

## Git / PR

- 作業ごとに main からブランチを切る（`feat/` `fix/` `refactor/` `docs/` など）
- コミットメッセージは英語の Conventional Commits（`refactor: share a screen scaffold with the top app bar`）
- PR の本文は英語で、`Summary` / `Changes` / `Testing` の見出しを付ける
- PR のマージはリポジトリのオーナーが行う。自分ではマージしない
