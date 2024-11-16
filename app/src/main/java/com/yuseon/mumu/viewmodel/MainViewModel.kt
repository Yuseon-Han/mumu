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
    }

    private fun initInnerStateData() {
        _dataModel.value?.data?.forEach {
            val content = it.contents
            val contentType = content?.type

            // for '더보기 '  todo. 모든 타입 반영
            if (contentType == "GRID" || contentType == "STYLE") {
                content.displayItemCount = MutableStateFlow(6)
            }

            // for 'refresh'
            when (contentType) {
                "GRID" -> {
                    content.contentListState = MutableStateFlow(content.goods ?: emptyList())
                }
                "STYLE" -> {
                    content.contentListState = MutableStateFlow(content.styles ?: emptyList())
                }
                "SCROLL" -> {
                    content.contentListState = MutableStateFlow(content.goods ?: emptyList())
                }
            }

        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val result = repo.getData()
            _dataModel.value = result
            initInnerStateData()
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
            onRefreshClicked()
        } else if (type == "MORE") {
            onShowMoreClicked()
        }
    }

    private fun onShowMoreClicked() {
        val index = _currPage
        val content = _dataModel.value?.data?.get(index)?.contents
        content ?: return
        val stateFlow = content.displayItemCount
        stateFlow ?: return

        // todo. 최대값 제한하기
        stateFlow.value = stateFlow.value?.plus(3)
    }

    private fun onRefreshClicked() {
        val index = _currPage
        val content = _dataModel.value?.data?.get(index)?.contents
        content ?: return
        val stateFlow = content.contentListState
        stateFlow ?: return

        stateFlow.value = stateFlow.value.shuffled()
    }
}