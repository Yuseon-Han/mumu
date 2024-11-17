package com.yuseon.mumu.view.content

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.yuseon.mumu.model.Content
import com.yuseon.mumu.model.Good
import com.yuseon.mumu.model.Style
import com.yuseon.mumu.model.toGood
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Grid(contentData: Content, firstItemFocus: Boolean = false) {
    contentData.displayItemCount ?: return
    contentData.contentListState ?: return

    val count by contentData.displayItemCount!!.collectAsState()
    val items by contentData.contentListState!!.collectAsState()
    val list = items.take(count ?: 0)

    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val prevCount = rememberSaveable {
        mutableIntStateOf(count!!)
    }

    if (prevCount.intValue < count!!) {
        coroutineScope.launch {
            gridState.animateScrollToItem(count !! - (count!! - prevCount.intValue))
        }
    }

    prevCount.intValue = count!!

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(GridItemGap),
        horizontalArrangement = Arrangement.spacedBy(GridItemGap)
    ) {
        if (firstItemFocus) {
            item(span = { GridItemSpan(3) }) {
                FocusingGridItem(list = list.take(3))
            }

            items(list.size - 3) { index ->
                val item = list[index + 3]
                if (item is Good) {
                    ContentItem(item = item)
                } else if (item is Style) {
                    ContentItem(item = item.toGood())
                }
            }

        } else {
            items(list.size) { index ->
                val item = list[index]
                if (item is Good) {
                    ContentItem(item = item)
                } else if (item is Style) {
                    ContentItem(item = item.toGood())
                }
            }
        }
    }
}

// first 3 items.
@Composable
fun FocusingGridItem(list: List<Any>) {
    @Composable
    fun getGridItem(index: Int) {
        val item = list[index]
        val focus = index == 0
        if (item is Good) {
            ContentItem(item = item, isBigOne = focus)
        } else if (item is Style) {
            ContentItem(item = item.toGood(), isBigOne = focus)
        }
    }

    val size = list.size;
    if (size > 0) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(GridItemGap)
        ) {
            Box(modifier = Modifier.weight(2f)) {
                getGridItem(index = 0)
            }

            if (size > 1) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(GridItemGap),
                ) {
                    getGridItem(index = 1)
                    if (size > 2) getGridItem(index = 2)
                }
            }
        }
    }
}