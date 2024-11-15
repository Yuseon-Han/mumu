package com.yuseon.mumu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuseon.mumu.model.MainDataModel
import com.yuseon.mumu.model.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val repo: Repository = Repository()

    private var _currPage: Int = 0

    private val _dataModel: MutableStateFlow<MainDataModel?> = MutableStateFlow(null)
    val dataModel: StateFlow<MainDataModel?> = _dataModel.asStateFlow()

    private val _landingRrl: MutableStateFlow<String?> = MutableStateFlow(null)
    val landingUrl: StateFlow<String?> = _landingRrl.asStateFlow()

    init {
        loadData()
        initVisibleItemData()
    }

    private fun initVisibleItemData() {
        _dataModel.value?.data?.forEach {
            if (it.contents?.type == "GRID" || it.contents?.type == "STYLE") {
                it.contents.displayItemCount = MutableStateFlow(6)
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val result = repo.getMockData()
            _dataModel.value = result
        }
    }

    fun loadUrl(url: String?) {
        _landingRrl.value = url
    }

    fun onPageChanged(page: Int) {
        _currPage = page
    }

    fun onFooterClicked(type: String?) {
        // todo. make it enum
        if (type == "REFRESH") {

        } else if (type == "MORE") {
            onShowMoreClicked()
        }
    }

    fun onShowMoreClicked() {
        val index = _currPage
        val content = _dataModel.value?.data?.get(index)?.contents
        content ?: return
        val stateFlow = _dataModel.value?.data?.get(index)?.contents?.displayItemCount
        stateFlow ?: return

        // todo. 최대값 제한하기
        stateFlow.value = stateFlow.value?.plus(3)
    }
}