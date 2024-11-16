package com.yuseon.mumu.view.content

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yuseon.mumu.model.Banner
import com.yuseon.mumu.model.Content
import com.yuseon.mumu.model.Good
import com.yuseon.mumu.model.Style
import com.yuseon.mumu.model.toGood
import com.yuseon.mumu.viewmodel.MainViewModel

val GridItemHeight = 200.dp
val GridItemImageHeight = 150.dp
val GridItemGap = 4.dp

@Composable
fun Content(contentData: Content) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        if (contentData.type == "BANNER") {
            Banners(contentData)
        } else if (contentData.type == "GRID") {
            Grid(contentData)
        } else if (contentData.type == "SCROLL") {
            Scroll(contentData)
        } else if (contentData.type == "STYLE") {
            Grid(contentData, firstItemFocus = true)
        } else {
            // todo. error page
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banners(contentData: Content) {
    val savedPage = rememberSaveable { mutableIntStateOf(0) }

    val pageCnt: Int = contentData.banners?.size ?: 0
    val pagerState = rememberPagerState(pageCount = {
        pageCnt
    }, initialPage = savedPage.intValue)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            savedPage.intValue = page
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                contentData.banners?.get(page).apply {
                    Banner(this)
                }
            }

            Box(
                modifier = Modifier
                    .background(
                        color = Color.Gray.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(2.dp)
                    )
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .align(Alignment.BottomEnd),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${savedPage.intValue + 1} / $pageCnt ",
                    color = Color.White
                )
            }
        }

    }
}

@Composable
fun Banner(bannerData: Banner?, mainViewModel: MainViewModel = viewModel()) {
    bannerData ?: return

    Box(
        contentAlignment = Alignment.Center
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Column() {
                    bannerData.title.apply {
                        Text(text = this ?: " ", fontSize = 20.sp)
                    }
                    bannerData.description.apply {
                        Text(text = this ?: " ", fontSize = 13.sp)
                    }
                }
            }

            Box(modifier = Modifier.clickable {
                mainViewModel.loadUrl(bannerData.linkURL)
            }) {
                AsyncImage(
                    model = bannerData.thumbnailURL,
                    contentDescription = "Footer title Icon",
                    contentScale = ContentScale.Crop
                )
                bannerData.keyword.apply {
                    val alpha = if (this@apply.isNullOrBlank()) 0.0f else 0.7f
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.Gray.copy(alpha = alpha),
                                shape = RoundedCornerShape(2.dp)
                            )
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                            .align(Alignment.BottomStart),
                        contentAlignment = Alignment.Center
                    ) {
                        val text = if (this@apply.isNullOrBlank()) " " else "#${this@apply}"
                        Text(
                            text = text, fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun Grid(contentData: Content, firstItemFocus: Boolean = false) {
    contentData.displayItemCount ?: return
    contentData.contentListState ?: return

    // todo. count 변경됐을때 새 아이템으로 포커스
    val count by contentData.displayItemCount!!.collectAsState()
    val items by contentData.contentListState!!.collectAsState()
    val list = items.take(count ?: 0)

    LazyVerticalGrid(
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
            modifier = Modifier
                .fillMaxWidth(),
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

@Composable
fun Scroll(contentData: Content) {
    val scrollState = rememberScrollState()

    val goods by contentData.contentListState!!.collectAsState()

    //todo. goods 리스트 새로고침 됐을때 첫번째로 포커스
    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        goods.forEach {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .padding(horizontal = 2.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                    )
                    .padding(horizontal = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                ContentItem(item = it as Good)
            }

        }
    }
}

@Composable
fun ContentItem(
    item: Good,
    isBigOne: Boolean = false,
    mainViewModel: MainViewModel? = viewModel()
) {
    val onlyImage = item.brandName.isNullOrBlank() && item.price == null && item.saleRate == null
    val contentHeight = if (onlyImage) GridItemImageHeight else GridItemHeight

    Column(
        modifier = Modifier
            .height(if (isBigOne) contentHeight * 2 else contentHeight)
            .clickable {
                mainViewModel?.loadUrl(item.linkURL)
            },
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isBigOne) GridItemImageHeight * 2 else GridItemImageHeight),
                model = item.thumbnailURL,
                contentDescription = "Item image",
                contentScale = ContentScale.Crop
            )

            if (item.hasCoupon)
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Blue,
                            shape = RoundedCornerShape(2.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .align(Alignment.BottomStart),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "쿠폰", fontSize = 10.sp,
                        color = Color.White
                    )
                }
        }

        if (item.brandName.isNullOrBlank() && item.price == null && item.saleRate == null)
            return

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                Text(text = item.brandName ?: "", color = Color.Gray, fontSize = 10.sp)

                Row(
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        text = "${item.price}원",
                        color = Color.Black,
                        fontSize = 10.sp
                    )
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = "${item.saleRate ?: ""} %",
                        color = Color.Red,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true, widthDp = 200,
    heightDp = 300,
)
@Composable
fun MyPreview() {
    ContentItem(
        Good(
            thumbnailURL = "https://image.msscdn.net/images/goods_img/20201221/1727824/1727824_4_320.jpg",
            brandName = "bran",
            price = 10000,
            hasCoupon = true,
            saleRate = 50
        )
    )
}