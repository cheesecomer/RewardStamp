package com.cheesecomer.rewardstamp.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.cheesecomer.rewardstamp.ui.theme.SheetCard
import com.cheesecomer.rewardstamp.ui.theme.SheetDivider
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimary
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimaryContainer
import com.cheesecomer.rewardstamp.ui.theme.SheetText

@Composable
private fun ExchangeDialogIcon(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(SheetPrimaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_gift),
            contentDescription = null,
            tint = SheetPrimary,
            modifier = Modifier.size(32.dp),
        )
    }
}

@Composable
private fun ExchangeDialogRewardSection(
    milestone: RewardMilestone,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = milestone.reward,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SheetText,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "シート${milestone.requiredSheetCount}枚と交換するよ",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDialog(
    milestone: RewardMilestone,
    modifier: Modifier = Modifier,
    onRewardSelect: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .testTag("ExchangeDialog.Background"),
            shape = RoundedCornerShape(28.dp),
            color = SheetCard,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ExchangeDialogIcon()

                ExchangeDialogRewardSection(milestone)

                HorizontalDivider(
                    color = SheetDivider,
                    modifier = Modifier.padding(top = 4.dp),
                )

                DialogButtons(
                    confirmText = "交換する",
                    onDismissClick = onDismissRequest,
                    onConfirmClick = {
                        onDismissRequest()
                        onRewardSelect()
                    },
                )
            }
        }
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun ExchangeDialogPreview() {
    RewardStampTheme {
        ExchangeDialog(
            milestone =
                RewardMilestone(
                    id = 0L,
                    sheetId = 1L,
                    requiredSheetCount = 1,
                    reward = "スーパーカップ",
                ),
        )
    }
}
