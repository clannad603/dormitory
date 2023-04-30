package com.huangrui.dormitory.ui.wigets

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.huangrui.dormitory.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun <T> Banner(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemClick: ((T) -> Unit)? = null,
    activeIndicator: Color = Color.Magenta,
    inactiveIndicator: Color = Color.Gray,
    itemContent: @Composable (T) -> Unit
) {
    val pagerState = rememberPagerState()
    HorizontalPager(
        state = pagerState,
        count = items.size,
        modifier = Modifier
            //点击事件
            .clickable {
                onItemClick?.invoke((items[pagerState.currentPage]))
            }
            .then(modifier)
    ) { pageIndex ->
        val itemData = items[pageIndex]
        Box(modifier = Modifier
            .fillMaxWidth()) {
            //内容
            itemContent(itemData)
            //指示器
            HorizontalPagerIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                pagerState = pagerState,
                activeColor = MaterialTheme.colorScheme.primary,
                inactiveColor = Color.White
            )
        }
    }
}
@Composable
fun ImageBannerItem(imageUrl:String,modifier: Modifier = Modifier){
    var placeHolderVisible by remember { mutableStateOf(true) }
    AsyncImage(
        model = imageUrl,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .placeholder(visible = placeHolderVisible, highlight = PlaceholderHighlight.fade())
            .then(modifier),
        onState = {
            when (it) {
                is AsyncImagePainter.State.Success -> {
                    placeHolderVisible = false
                }
                is AsyncImagePainter.State.Error -> {
                    //todo 异常处理
                    Log.e("AsyncImage", "ImageBannerItem:State.Error ${it.result.throwable} ")
                }
                else -> {}
            }
        }
    )
}

@Composable
fun ImageBannerItemLocal(model:Int,modifier: Modifier = Modifier){
//    var placeHolderVisible by remember { mutableStateOf(true) }
    Image(
        painter = painterResource(id = R.drawable.banner),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
//            .placeholder(visible = placeHolderVisible, highlight = PlaceholderHighlight.fade())
            .then(modifier),
    )
}