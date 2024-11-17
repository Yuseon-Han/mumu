import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yuseon.mumu.model.Content
import com.yuseon.mumu.model.Good
import com.yuseon.mumu.view.content.ContentItem
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Scroll(contentData: Content) {
    if (contentData.contentListState == null) return
    val scrollState = rememberScrollState()
    val goods by contentData.contentListState!!.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // 새로 고침 됐을때 맨앞으로 스크롤
    coroutineScope.launch {
        scrollState.scrollTo(0)
    }

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
