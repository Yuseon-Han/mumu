package com.yuseon.mumu.model


//todo. DTO 와 VO 분리
import kotlinx.coroutines.flow.MutableStateFlow

data class MainDataModel(
    val data: List<Page>?
)

data class Page(
    val header: Header?,
    val contents: Content?,
    val footer: Footer?
)

data class Header(
    val title: String?,
    val iconURL: String?,
    val linkURL: String?,
)

data class Footer(
    val title: String?,
    val iconURL: String?,
    val type: String?,
    var visibility: MutableStateFlow<Boolean>? = null,
)

data class Content(
    val type: String?,
    val banners: List<Banner>?,
    val goods: List<Good>?,
    val styles: List<Style>?,
    //show more 에 의해 보여지는 아이템 개수가 조절됨, null일 경우 showMore 동작 안함
    var displayItemCount: MutableStateFlow<Int?>? = null,
    var contentListState: MutableStateFlow<List<Any>>? = null,
) {
    fun itemCount(): Int? {
        when (type) {
            "BANNER" -> {
                return banners?.size
            }

            "GRID", "SCROLL" -> {
                return goods?.size
            }

            "STYLE" -> {
                return styles?.size
            }

            else -> return null
        }
    }
}

data class Banner(
    val linkURL: String?,
    val thumbnailURL: String?,
    val title: String?,
    val description: String?,
    val keyword: String?,
)

data class Good(
    val linkURL: String? = null,
    val thumbnailURL: String? = null,
    val brandName: String? = null,
    val price: Int? = null,
    val saleRate: Int? = null,
    val hasCoupon: Boolean = false
)

data class Style(
    val linkURL: String?,
    val thumbnailURL: String?,
)

fun Style.toGood(): Good {
    return Good(linkURL = this.linkURL, thumbnailURL = this.thumbnailURL)
}


