package com.yuseon.mumu.view.footer

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yuseon.mumu.model.Footer
import com.yuseon.mumu.viewmodel.MainViewModel

@Composable
fun Footer(footerData: Footer, mainViewModel: MainViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .padding(20.dp)
            .border(
                width = 1.dp,                   // 테두리 두께
                color = Color.LightGray,              // 테두리 색상
                shape = RoundedCornerShape(30.dp) // 모서리 둥글기
            )
            .height(60.dp)
            .padding(16.dp)
            .fillMaxWidth()
            .clickable {
                mainViewModel.onFooterClicked(footerData.type)
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier, verticalAlignment = Alignment.CenterVertically
        ) {
            footerData.iconURL?.apply {
                AsyncImage(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(20.dp),
                    model = this,
                    contentDescription = "Footer title Icon",
                    contentScale = ContentScale.Fit
                )
            }
            Text(text = footerData.title ?: "", style = MaterialTheme.typography.bodyLarge)
        }

    }
}