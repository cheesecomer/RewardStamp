package com.cheesecomer.rewardstamp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.model.StampType

private const val COLUMN_COUNT = 4

@Composable
fun StampTypeGrid(
    onStampTypeClick: (StampType) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(COLUMN_COUNT),
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(StampType.entries) { stampType ->
            StampTypeGridItem(
                stampType = stampType,
                onClick = {
                    onStampTypeClick(stampType)
                },
            )
        }
    }
}

@Composable
private fun StampTypeGridItem(
    stampType: StampType,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .aspectRatio(1f)
                .clickable(onClick = onClick)
                .testTag(stampType.id),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(stampType.iconRes),
                contentDescription = stampType.id,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
