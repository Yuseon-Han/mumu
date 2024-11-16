package com.yuseon.mumu.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuseon.mumu.model.Page
import com.yuseon.mumu.view.content.Content
import com.yuseon.mumu.view.footer.Footer
import com.yuseon.mumu.view.header.Header
import com.yuseon.mumu.viewmodel.MainViewModel


@Composable
fun MainPageTab(mainViewModel: MainViewModel = viewModel()) {
    val dataModel by mainViewModel.dataModel.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles: List<String> = dataModel?.data?.map { it.contents?.type ?: "" } ?: emptyList()

    Column(modifier = Modifier.fillMaxSize()) {
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
                .padding(7.dp)
        ) {
            dataModel?.data?.get(selectedTabIndex).apply {
                Page(this)
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
            pageData.contents?.apply {
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
