package com.yuseon.mumu.view.header

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yuseon.mumu.model.Header
import com.yuseon.mumu.viewmodel.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Header(headerData: Header, mainViewModel: MainViewModel = viewModel()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier.weight(1f).horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier,
                text = headerData.title ?: ""
            )
            headerData.iconURL?.apply {
                AsyncImage(
                    modifier = Modifier.size(20.dp),
                    model = this,
                    contentDescription = "Header title Icon",
                    contentScale = ContentScale.Fit
                )
            }
        }

        val context = LocalContext.current

        headerData.linkURL?.apply {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                        mainViewModel.loadUrl(context, this)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "전체")
            }

        }

    }
}