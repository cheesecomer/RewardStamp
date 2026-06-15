package com.cheesecomer.rewardseal.model

import androidx.annotation.DrawableRes
import com.cheesecomer.rewardseal.R

enum class StampType(
    val id: String,
    @DrawableRes val iconRes: Int,
) {
    Dog("dog", R.drawable.stamp_dog),
    Cat("cat", R.drawable.stamp_cat),
    Paw("paw",R.drawable.stamp_paw),
    Frog("frog",R.drawable.stamp_frog),
    Star("star",R.drawable.stamp_star),
    Apple("apple",R.drawable.stamp_apple),
    Bear1("bear_1",R.drawable.stamp_bear_1),
    Bear2("bear_2",R.drawable.stamp_bear_2),
    Butterfly("butterfly",R.drawable.stamp_butterfly),
    Cherry("cherry",R.drawable.stamp_cherry),
    CherryBlossom("cherry_blossom",R.drawable.stamp_cherry_blossom),
    Hippopotamus("hippopotamus",R.drawable.stamp_hippopotamus),
    Horse("horse",R.drawable.stamp_horse),
    Mammoth("mammoth",R.drawable.stamp_mammoth),
    Monkey("monkey",R.drawable.stamp_monkey),
    Penguin("penguin",R.drawable.stamp_penguin),
    Rabbit("rabbit",R.drawable.stamp_rabbit),
    Shark("shark",R.drawable.stamp_shark),
    Turtle("turtle",R.drawable.stamp_turtle),
    Shortcake("shortcake",R.drawable.stamp_shortcake);

    companion object {
        fun fromId(id: String): StampType {
            return entries.firstOrNull {
                it.id == id
            } ?: Dog
        }
    }
}