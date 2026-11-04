package com.cheesecomer.rewardstamp.ui

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.core.content.res.ResourcesCompat

@Composable
fun rememberDrawable(
    @DrawableRes id: Int,
): Drawable {
    val resources = LocalResources.current
    return remember(resources, id) {
        checkNotNull(ResourcesCompat.getDrawable(resources, id, null))
    }
}
