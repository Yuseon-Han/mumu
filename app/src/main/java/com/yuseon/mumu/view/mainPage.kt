package com.yuseon.mumu.view

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuseon.mumu.model.Page
import com.yuseon.mumu.view.content.Content
import com.yuseon.mumu.view.footer.Footer
import com.yuseon.mumu.view.header.Header
import com.yuseon.mumu.viewmodel.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPage(mainViewModel: MainViewModel = viewModel()) {
    val landingUrl by mainViewModel.landingUrl.collectAsState()
    BackHandler(enabled = landingUrl != null) {
        mainViewModel.loadUrl(null)
    }
    val dataModel by mainViewModel.dataModel.collectAsState()
    val savedPage = rememberSaveable { mutableStateOf(0) }

    val pageCnt: Int = dataModel?.data?.size ?: 0
    val pagerState = rememberPagerState(pageCount = {
        pageCnt
    }, initialPage = savedPage.value)

    if (landingUrl != null) {
        WebViewScreen(url = landingUrl!!)
    } else {
        if (dataModel == null) {
            Text("loading")
        } else {
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    savedPage.value = page
                    mainViewModel.onPageChanged(page)
                }
            }
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                dataModel?.data?.get(page).apply {
                    Page(this)
                }
            }
        }
    }
}

@Composable
fun MainPageTab(mainViewModel: MainViewModel = viewModel()) {

    val landingUrl by mainViewModel.landingUrl.collectAsState()
    BackHandler(enabled = landingUrl != null) {
        mainViewModel.loadUrl(null)
    }
    val dataModel by mainViewModel.dataModel.collectAsState()

    // 현재 선택된 탭 상태 관리
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // 탭 이름 리스트
    val tabTitles: List<String> = dataModel?.data?.map { it.contents?.type ?: "" } ?: emptyList()

    if (landingUrl != null) {
        WebViewScreen(url = landingUrl!!)
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // TabRow: 탭 컨테이너
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            mainViewModel.onPageChanged(index)
                        },
                        text = { Text(title) }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                dataModel?.data?.get(selectedTabIndex).apply {
                    Page(this)
                }
            }
        }
    }
}

@Composable
fun Page(pageData: Page?) {
    pageData ?: return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        pageData.header?.apply {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Header(this@apply)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Log.i("purin", "1")
            pageData.contents?.apply {
                Log.i("purin", "2")
                Content(this)
            }
        }

        pageData.footer?.apply {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Footer(this@apply)
            }
        }
    }
}
