package com.cheesecomer.rewardstamp.model

// What the adult lock protects (docs/usecases.md「おとなロック」)
enum class LockTarget {
    // Pressing a stamp, which some families leave to the child
    Stamp,

    // Everything only the parents do, such as editing sheets and exchanging rewards
    ParentAction,
}
