package com.zyc.clover.pages.layout.children.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zyc.clover.models.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _topicList = MutableStateFlow(
        listOf(
            "https://i2.3conline.com/images/piclib/201203/22/batch/1/130539/1332349654033obqessjgay.jpg",
            "https://ts1.tc.mm.bing.net/th/id/R-C.ab86225bd0428ae801a1641af1cb62da?rik=nbCBBFlabS73Hw&riu=http%3a%2f%2fimg95.699pic.com%2fphoto%2f50048%2f6482.jpg_wh860.jpg&ehk=Ikp7qmkz2x7Mxr90wpr0m6Gj%2fvoOiouoOpZ20gUFlfc%3d&risl=&pid=ImgRaw&r=0",
            "https://ts1.tc.mm.bing.net/th/id/R-C.613eefe4f3dce1539fb869512aaec653?rik=JHqP2a98FW9V6g&riu=http%3a%2f%2fimg.keaitupian.cn%2fuploads%2f2020%2f07%2f27%2fu5fvdxekx3v.jpg&ehk=kXJUsPWsN%2bqEyh%2fG3OBv36Zf9YrLkv2IANMYFhB%2fHUo%3d&risl=&pid=ImgRaw&r=0",
            "https://img.shetu66.com/2023/04/25/1682410853619598.png",
            "https://so1.360tres.com/t0165f9b1a3dca6c254.jpg",
            "https://img.shetu66.com/2022/08/31/1661927060990428.jpg"
        )
    )
    val topicList: StateFlow<List<String>> = _topicList





    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

}
