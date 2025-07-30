package com.zyc.feature.common_page.pages.webList

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebListViewModel : ViewModel() {
    private val _webList = MutableStateFlow(
        listOf(
            ItemType("百度", "https://www.baidu.com", Color(0xff1079ff)),
            ItemType(
                "豆包",
                "https://www.doubao.com/chat/?channel=bing_sem&source=dbweb_bing_sem_xhs_cpc_pp_tup_hexin_web_05&keywordid=77584708630393&msclkid=e8c8b7243b9c1e7e7bd9d09f16fba26a",
                Color(0xff2a9f7b)
            ),
            ItemType("扣子空间", "https://space.coze.cn/task/7504486802183798835", Color(0xfcce37ff)),
        )
    )

    val webList: StateFlow<List<ItemType>> = _webList.asStateFlow()
}
