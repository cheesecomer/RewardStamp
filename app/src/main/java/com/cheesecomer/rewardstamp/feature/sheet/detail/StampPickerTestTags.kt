package com.cheesecomer.rewardstamp.feature.sheet.detail

import com.cheesecomer.rewardstamp.model.StampType

internal object StampPickerTestTags {
    fun stamp(stampType: StampType) = "StampPickerBottomSheet.StampGrid.${stampType.id}"
}
