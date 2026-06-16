package com.cheesecomer.rewardseal.feature.completedsheet.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import kotlinx.coroutines.launch

class CompletedSheetListViewModel(
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
) : ViewModel() {
    companion object {
        fun factory(completedRewardSheetRepository: CompletedRewardSheetRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    CompletedSheetListViewModel(
                        completedRewardSheetRepository,
                    )
                }
            }
    }

    var sheets by mutableStateOf<List<CompletedRewardSheet>>(emptyList())
        private set

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            sheets = completedRewardSheetRepository.findAll()
            sheets.forEach {
                android.util.Log.d("findExchangeable", "${it.title} / ${it.sheetId} : ${it.consumedAt}")
            }
        }
    }
}
