package com.yuseon.mumu.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class MainDataModel (
    val data : List<Page>?
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
)

data class Content(
    val type: String?,
    val banners: List<Banner>?,
    val goods: List<Good>?,
    val styles: List<Style>?,
    //show more 에 의해 보여지는 아이템 개수가 조절됨, null일 경우 showMore 동작 안함
    var displayItemCount : MutableStateFlow<Int?>?
)

data class Banner(
    val linkURL: String?,
    val thumbnailURL: String?,
    val title: String?,
    val description: String?,
    val keyword: String?,
)

data class Good(
    val linkURL: String?,
    val thumbnailURL: String?,
    val brandName: String?,
    val price: Int?,
    val saleRate: Int?,
    val hasCoupon: Boolean?
)

data class Style(
    val linkURL: String?,
    val thumbnailURL: String?,
)
