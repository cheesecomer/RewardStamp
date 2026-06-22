package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.cheesecomer.rewardseal.ui.theme.SheetBackground
import com.cheesecomer.rewardseal.ui.theme.SheetBorder
import com.cheesecomer.rewardseal.ui.theme.SheetDivider
import com.cheesecomer.rewardseal.ui.theme.SheetPrimary
import com.cheesecomer.rewardseal.ui.theme.SheetText
import com.cheesecomer.rewardseal.ui.theme.StampInk
import kotlin.math.floor

private const val CELL_HEIGHT = 72
private const val GRID_SPACE = 14
private const val VISIBLE_ROW_NUM = 3.5
private const val VISIBLE_COLUMN_NUM = 4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StampPickerBottomSheet(
    modifier: Modifier = Modifier,
    onSelect: (StampType) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    ModalBottomSheet(
        modifier = modifier,
        dragHandle = null,
        onDismissRequest = onDismiss,
        containerColor = SheetBackground,
    ) {
        StampPickerBottomSheetContent(
            onDismiss = onDismiss,
            onSelect = onSelect,
        )
    }
}

@Composable
private fun StampPickerBottomSheetContent(
    modifier: Modifier = Modifier,
    onSelect: (StampType) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp, top = 24.dp),
    ) {
        Text(
            text = "スタンプをえらんでね",
            style =
                MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = SheetText,
                ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.size(20.dp))

        StampGrid(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(
                        max =
                            (
                                (CELL_HEIGHT * VISIBLE_ROW_NUM) + (
                                    GRID_SPACE *
                                        floor(
                                            VISIBLE_ROW_NUM,
                                        )
                                )
                            ).dp,
                    ),
            onSelect = onSelect,
        )

        HorizontalDivider(color = SheetDivider)

        Spacer(Modifier.size(20.dp))
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.End),
        ) {
            Text("キャンセル")
        }
    }
}

@Composable
private fun StampGrid(
    modifier: Modifier = Modifier,
    onSelect: (StampType) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(VISIBLE_COLUMN_NUM),
        horizontalArrangement = Arrangement.spacedBy(GRID_SPACE.dp),
        verticalArrangement = Arrangement.spacedBy(GRID_SPACE.dp),
        modifier = modifier,
    ) {
        items(StampType.entries) { stampType ->
            StampGridItem(
                stamp = stampType,
                onClick = { onSelect(stampType) },
                modifier = Modifier.testTag("StampPickerBottomSheet.StampGrid.${stampType.id}"),
            )
        }
        item {
            Spacer(Modifier.size(20.dp))
        }
    }
}

@Composable
private fun StampGridItem(
    stamp: StampType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val borderColor by animateColorAsState(
        targetValue = if (isPressed) SheetPrimary else SheetBorder,
        label = "stampBorderColor",
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 2.dp,
        label = "stampBorderWidth",
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        label = "stampScale",
    )

    Box(
        modifier =
            modifier
                .size(CELL_HEIGHT.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }.clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = null,
                ).padding(vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier.matchParentSize(),
        ) {
            drawCircle(
                color = Color.White,
                style = Fill,
            )
            drawCircle(
                color = borderColor,
                style = Stroke(width = borderWidth.toPx()),
            )
        }
        Icon(
            painter = painterResource(stamp.iconRes),
            contentDescription = stamp.id,
            tint = StampInk,
            modifier = Modifier.size(50.dp),
        )
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Composable
private fun StampPickerBottomSheetPreview() {
    RewardSealTheme {
        Scaffold {
            StampPickerBottomSheetContent(modifier = Modifier.padding(it))
        }
    }
}
