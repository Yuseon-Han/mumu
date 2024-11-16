import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.yuseon.mumu.viewmodel.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Banners(contentData: Content) {
    val savedPage = rememberSaveable { mutableIntStateOf(0) }

    val pageCnt: Int = contentData.banners?.size ?: 0
    val pagerState = rememberPagerState(pageCount = { pageCnt }, initialPage = savedPage.intValue)

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

    Box(contentAlignment = Alignment.Center) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Column {
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
                    contentDescription = "Banner image",
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
