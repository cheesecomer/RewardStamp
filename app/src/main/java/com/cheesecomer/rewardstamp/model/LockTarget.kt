package com.cheesecomer.rewardstamp.model

// What the adult lock protects (docs/usecases.md「おとなロック」)
enum class LockTarget {
    // Pressing a stamp, the only thing children usually do
    Stamp,

    // Exchanging rewards
    RewardExchange,

    // Creating, editing and deleting sheets, including changing and cancelling stamps
    SheetEdit,

    // Deleting history records
    HistoryDelete,

    // Changing the adult lock itself, always locked so that children cannot turn it off
    LockSettings,
    ;

    companion object {
        // The targets parents choose in the settings, in the order shown
        val selectable: List<LockTarget> = listOf(Stamp, RewardExchange, SheetEdit, HistoryDelete)
    }
}
