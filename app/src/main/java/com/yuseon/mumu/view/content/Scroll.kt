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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yuseon.mumu.model.Content
import com.yuseon.mumu.model.Good
import com.yuseon.mumu.view.content.ContentItem

@Composable
fun Scroll(contentData: Content) {
    if (contentData.contentListState == null) return

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
