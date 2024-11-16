package com.yuseon.mumu.view.content

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import Banners
import Scroll
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yuseon.mumu.model.Content
import com.yuseon.mumu.model.Good
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
        when (contentData.type) {
            "BANNER" -> {
                Banners(contentData)
            }

            "GRID" -> {
                Grid(contentData)
            }

            "SCROLL" -> {
                Scroll(contentData)
            }

            "STYLE" -> {
                Grid(contentData, firstItemFocus = true)
            }

            else -> {
                // todo. error page
            }
        }
    }
}

// used for Grid, Scroll, Style commonly
@Composable
fun ContentItem(
    item: Good,
    isBigOne: Boolean = false,
    viewModel: MainViewModel = viewModel()
) {
    val onlyImage = item.brandName.isNullOrBlank() && item.price == null && item.saleRate == null
    val contentHeight = if (onlyImage) GridItemImageHeight else GridItemHeight
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .height(if (isBigOne) contentHeight * 2 else contentHeight)
            .clickable {
                viewModel.loadUrl(context, item.linkURL)
            },
        verticalArrangement = Arrangement.spacedBy(GridItemGap)
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
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

        if (onlyImage) return

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(text = item.brandName ?: "", color = Color.Gray, fontSize = 10.sp)

                Row {
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