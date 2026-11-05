package com.cheesecomer.rewardstamp.screenshots

import android.graphics.Bitmap
import android.os.SystemClock
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cheesecomer.rewardstamp.MainActivity
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.RewardStampApplication
import com.cheesecomer.rewardstamp.feature.completedsheet.detail.CompletedSheetDetailTestTags
import com.cheesecomer.rewardstamp.feature.completedsheet.list.CompletedSheetListTestTags
import com.cheesecomer.rewardstamp.feature.exchangeablesheet.list.ExchangeableSheetCardTestTags
import com.cheesecomer.rewardstamp.feature.sheet.detail.CongratulationDialogTestTags
import com.cheesecomer.rewardstamp.feature.sheet.detail.GoalCelebrationTestTags
import com.cheesecomer.rewardstamp.feature.sheet.detail.SheetDetailTestTags
import com.cheesecomer.rewardstamp.feature.sheet.detail.StampPickerTestTags
import com.cheesecomer.rewardstamp.feature.sheet.edit.RewardMilestoneDialogTestTags
import com.cheesecomer.rewardstamp.feature.sheet.edit.RewardMilestonesSectionTestTags
import com.cheesecomer.rewardstamp.feature.sheet.edit.SheetEditTestTags
import com.cheesecomer.rewardstamp.feature.sheet.list.SheetListTestTags
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.ui.component.dialog.ChoiceRewardDialogTestTags
import kotlinx.coroutines.runBlocking
import org.junit.Assume.assumeTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * アプリを実際に操作して、画面のスクリーンショットを撮る（docs/development.md「スクリーンショット」）。
 * [ShowcaseData] のデータを入れてから動かす。
 * tools/screenshots/take.sh から `-e screenshots true` を付けて実行する。通常のテストの実行では飛ばす
 */
@RunWith(AndroidJUnit4::class)
class ScreenshotTour {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    private val context get() = instrumentation.targetContext
    private lateinit var scenario: ActivityScenario<MainActivity>
    private lateinit var data: ShowcaseData
    private val outputDir by lazy {
        File(context.getExternalFilesDir(null), "screenshots").apply {
            deleteRecursively()
            mkdirs()
        }
    }

    @Before
    fun insertShowcaseData() {
        assumeTrue(InstrumentationRegistry.getArguments().getString("screenshots") == "true")
        val application = context.applicationContext as RewardStampApplication
        data = ShowcaseData(application.database)
        runBlocking { data.insert() }
    }

    @Test
    fun 全画面を撮る() {
        scenario = ActivityScenario.launch(MainActivity::class.java)

        waitFor(hasText(ShowcaseData.HELPING))
        shoot("01-sheet-list")

        click(hasTestTag(SheetListTestTags.sheetCard(data.toothbrushingId)))
        waitFor(hasText(string(R.string.sheet_detail_stamp)))
        shoot("02-sheet-detail")

        click(hasText(string(R.string.sheet_detail_stamp)))
        waitFor(hasTestTag(StampPickerTestTags.stamp(StampType.Dog)))
        shoot("03-stamp-picker")

        // ゴールの演出は時間で進むので、テストの時計を止めて、ゴールのスタンプが出たところで撮る
        click(hasTestTag(StampPickerTestTags.stamp(StampType.Dog)))
        shootGoalCelebration("04-goal-celebration")

        waitFor(hasTestTag(CongratulationDialogTestTags.ROOT), timeout = CELEBRATION_TIMEOUT)
        shoot("05-congratulation")
        click(hasTestTag(CongratulationDialogTestTags.SURE_BUTTON))

        click(hasTestTag(SheetDetailTestTags.MENU_BUTTON))
        click(hasTestTag(SheetDetailTestTags.MENU_EDIT_BUTTON))
        waitFor(hasText("どうぶつえん"))
        shoot("06-sheet-edit")

        // 一覧は遅延して並べるので、画面の外のボタンは、スクロールするまで見つからない
        composeRule
            .onNode(hasTestTag(SheetEditTestTags.LIST))
            .performScrollToNode(hasTestTag(RewardMilestonesSectionTestTags.ADD_MILESTONE_BUTTON))
        click(hasTestTag(RewardMilestonesSectionTestTags.ADD_MILESTONE_BUTTON))
        waitFor(hasTestTag(RewardMilestoneDialogTestTags.REWARD))
        composeRule.onNode(hasTestTag(RewardMilestoneDialogTestTags.REWARD)).performTextInput("えほん")
        hideKeyboard()
        shoot("07-add-reward-dialog")
        click(hasText(string(R.string.common_cancel)))
        backUntilGone(hasTestTag(SheetEditTestTags.SAVE))
        backUntilGone(hasText(string(R.string.sheet_detail_stamp)))

        click(hasContentDescription(string(R.string.sheet_list_create)))
        waitFor(hasTestTag(SheetEditTestTags.SAVE))
        shoot("08-sheet-new")
        backUntilGone(hasTestTag(SheetEditTestTags.SAVE))

        click(hasText(string(R.string.tab_rewards)))
        waitFor(hasTestTag(ExchangeableSheetCardTestTags.exchangeButton(data.toothbrushingId)))
        shoot("09-rewards")

        click(hasTestTag(ExchangeableSheetCardTestTags.exchangeButton(data.toothbrushingId)))
        waitFor(hasTestTag(ChoiceRewardDialogTestTags.ROOT))
        shoot("10-choose-reward-dialog")
        backUntilGone(hasTestTag(ChoiceRewardDialogTestTags.ROOT))

        click(hasText(string(R.string.tab_history)))
        waitFor(hasTestTag(CompletedSheetListTestTags.gridItem(data.featuredCompletedSheetId)))
        shoot("11-history")

        click(hasTestTag(CompletedSheetListTestTags.gridItem(data.featuredCompletedSheetId)))
        waitFor(hasTestTag(CompletedSheetDetailTestTags.TITLE_LABEL))
        shoot("12-history-detail")
    }

    private fun string(
        @StringRes id: Int,
    ): String = context.getString(id)

    /** 見つからなければ、そのときの画面を failed.png に残す（画面の変更で操作の手順が合わなくなったとき、原因を見るため） */
    private fun waitFor(
        matcher: SemanticsMatcher,
        timeout: Long = TIMEOUT,
    ) {
        runCatching {
            composeRule.waitUntil(timeout) { exists(matcher) }
        }.onFailure {
            capture(FAILED)
            throw AssertionError("見つからない: ${matcher.description}", it)
        }
    }

    private fun exists(matcher: SemanticsMatcher): Boolean =
        composeRule.onAllNodes(matcher, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()

    private fun click(matcher: SemanticsMatcher) {
        waitFor(matcher)
        composeRule.onAllNodes(matcher, useUnmergedTree = true).onFirst().performClick()
    }

    /**
     * 戻るキーを押して、[matcher] の要素が消えるまで待つ（キーボードが出ていると、1回目はキーボードだけが閉じる）。
     * 画面が切り替わるアニメーションの途中で押し直すと1画面戻りすぎるので、押すたびに消えるのを少し待つ
     */
    private fun backUntilGone(matcher: SemanticsMatcher) {
        waitFor(matcher)
        repeat(BACK_ATTEMPTS) {
            Espresso.pressBackUnconditionally()
            val gone =
                runCatching {
                    composeRule.waitUntil(BACK_TIMEOUT) { !exists(matcher) }
                }.isSuccess
            if (gone) return
        }
        capture(FAILED)
        error("戻っても消えない: ${matcher.description}")
    }

    /**
     * ゴールの演出のうち、ゴールのスタンプが画面の真ん中に出たところを撮る。
     * 演出は時間で進み、待っている間に先へ進んでしまうので、テストの時計を止めて1コマずつ進める
     */
    private fun shootGoalCelebration(name: String) {
        composeRule.mainClock.autoAdvance = false
        try {
            val deadline = SystemClock.uptimeMillis() + CELEBRATION_TIMEOUT
            while (!exists(hasTestTag(GoalCelebrationTestTags.GOAL_STAMP))) {
                if (SystemClock.uptimeMillis() > deadline) {
                    capture(FAILED)
                    throw AssertionError("ゴールのスタンプが出ない")
                }
                composeRule.mainClock.advanceTimeByFrame()
                SystemClock.sleep(FRAME_MILLIS)
            }
            // スタンプが大きくなりきるのを待つ
            composeRule.mainClock.advanceTimeBy(SHOWCASE_ENTER_MILLIS)
            SystemClock.sleep(SETTLE_MILLIS)
            capture(name)
        } finally {
            composeRule.mainClock.autoAdvance = true
        }
    }

    /** キーボードが出ていたら閉じる。ダイアログは開いたまま */
    private fun hideKeyboard() {
        settle()
        if (!keyboardVisible()) return
        Espresso.pressBackUnconditionally()
        runCatching {
            composeRule.waitUntil(TIMEOUT) { !keyboardVisible() }
        }.onFailure {
            capture(FAILED)
            throw AssertionError("キーボードが消えない", it)
        }
    }

    private fun keyboardVisible(): Boolean {
        var visible = false
        scenario.onActivity { activity ->
            visible =
                ViewCompat
                    .getRootWindowInsets(activity.window.decorView)
                    ?.isVisible(WindowInsetsCompat.Type.ime()) == true
        }
        return visible
    }

    /** 画面が落ち着くのを待つ（キーボードやシートのアニメーションは Compose の待機の対象外） */
    private fun settle() {
        composeRule.waitForIdle()
        SystemClock.sleep(SETTLE_MILLIS)
        composeRule.waitForIdle()
    }

    private fun shoot(name: String) {
        settle()
        capture(name)
    }

    /** 画面全体（ダイアログやシートの別ウィンドウも含む）を撮る */
    private fun capture(name: String) {
        val bitmap = checkNotNull(instrumentation.uiAutomation.takeScreenshot()) { "スクリーンショットを撮れない: $name" }
        File(outputDir, "$name.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, it) }
    }

    private companion object {
        const val TIMEOUT = 15_000L
        const val CELEBRATION_TIMEOUT = 10_000L
        const val SETTLE_MILLIS = 1_000L
        const val BACK_TIMEOUT = 2_000L
        const val BACK_ATTEMPTS = 3
        const val FRAME_MILLIS = 16L
        const val SHOWCASE_ENTER_MILLIS = 700L
        const val PNG_QUALITY = 100
        const val FAILED = "failed"
    }
}
