package com.funny.compose.study.ui.refresh

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeToRefreshTest(
    modifier: Modifier = Modifier
) {
    val list = remember {
        List(4){ "Item $it" }.toMutableStateList()
    }
    var refreshing by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val state = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
        scope.launch {
            refreshing = true
//            delay(1000) // 模拟数据加载
            list+="Item ${list.size+1}"
            refreshing = false
        }
    })
    Box(modifier = modifier
        .fillMaxSize()
        .pullRefresh(state)
    ){
        LazyColumn(Modifier.fillMaxWidth()){
            items(list) {
                Text(
                    text = it,
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}