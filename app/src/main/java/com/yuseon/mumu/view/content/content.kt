package com.yuseon.mumu.view.content

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yuseon.mumu.model.Banner
import com.yuseon.mumu.model.Content
import com.yuseon.mumu.model.Good
import com.yuseon.mumu.viewmodel.MainViewModel

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
fun Grid(contentData: Content, mainViewModel: MainViewModel = viewModel()) {
    contentData.displayItemCount ?: return

    val count by contentData.displayItemCount!!.collectAsState()
    val items = contentData.goods?.take(count ?: 0)

    if (items != null) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // 열을 3개로 고정
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.size) { index ->
                SmallItem(item = items[index])
            }
        }
    }
}

@Composable
fun Scroll(contentData: Content, mainViewModel: MainViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState) // 가로 스크롤 가능
            .padding(16.dp)
    ) {
        contentData.goods?.forEach {
            SmallItem(item = it)
        }
    }
}

@Composable
fun SmallItem(item: Good, mainViewModel: MainViewModel = viewModel()) {
    Column(
        modifier = Modifier.clickable {
            mainViewModel.loadUrl(item.linkURL)
        }) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .aspectRatio(0.8f)
                .padding(1.dp)
        ) {
            Column {
                Box() {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = item.thumbnailURL,
                        contentDescription = "Item image",
                        contentScale = ContentScale.Fit
                    )

                    if (item.hasCoupon == true)
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
            }
        }

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
