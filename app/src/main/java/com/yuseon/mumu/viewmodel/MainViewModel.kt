package com.yuseon.mumu.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuseon.mumu.model.MainDataModel
import com.yuseon.mumu.model.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

class MainViewModel : ViewModel() {
    private val repo: Repository = Repository()

    private var _currPage: Int = 0

    private val _dataModel: MutableStateFlow<MainDataModel?> = MutableStateFlow(null)
    val dataModel: StateFlow<MainDataModel?> = _dataModel.asStateFlow()

    private var _job : Job? = null

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

            if (it.footer != null) {
                it.footer.visibility = MutableStateFlow(true)
            }

        }
    }

    private fun loadData() {
        _job = viewModelScope.launch {
            val result = repo.getData()
            _dataModel.value = result
            initInnerStateData()
        }
    }

    override fun onCleared() {
        super.onCleared()
        _job?.cancel()
    }

    fun loadUrl(context: Context, url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
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
        val model = _dataModel.value?.data?.get(index)
        val content = model?.contents
        content ?: return
        val stateFlow = content.displayItemCount
        stateFlow ?: return

        stateFlow.value = min(stateFlow.value?.plus(3) ?: 0, content.itemCount() ?: 0)
        if (stateFlow.value!! >= (content.itemCount() ?: 0)) {
            model.footer?.visibility?.value = false
        }
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