package com.cheesecomer.rewardseal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.feature.sheet.list.SheetListScreen
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme

@ExcludeFromCoverage
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RewardSealTheme {
                App()
            }
        }
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    RewardSealTheme {
        SheetListScreen()
    }
}
