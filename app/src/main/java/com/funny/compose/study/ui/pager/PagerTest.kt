package com.funny.compose.study.ui.pager

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalPagerTest() {
    val logs = remember { mutableStateListOf<String>() }
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    VerticalPager(pageCount = 10, modifier = Modifier.fillMaxSize()) { page ->
        RecordSelfText(modifier = Modifier.fillMaxSize(), page = page, updateLog = {
            scope.launch {
                logs.add(it)
                lazyListState.animateScrollToItem(logs.size - 1)
            }
        })
    }
    LazyColumn(modifier = Modifier.heightIn(0.dp, 300.dp), state = lazyListState){
        items(logs){
            Text(text = it)
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun RecordSelfText(
    modifier: Modifier,
    page: Int,
    updateLog: (String) -> Unit
) {
    var num by rememberSaveable {
        mutableStateOf(0)
    }
    LaunchedEffect(key1 = Unit){
        num += 1
        Log.d("RecordSelfText", "Page: $page, num: $num")
        updateLog("Page: $page, num: $num")
    }
    val textMeasurer = rememberTextMeasurer(cacheSize = 8)
    val textStyle = MaterialTheme.typography.subtitle2
    Canvas(modifier = modifier){
        drawText(textMeasurer, "Page: $page, num: $num", style = textStyle, topLeft = size.center)
    }
}

//data class TodoItemBean(val name = "hhh")
//
//@Composable
//fun TodoItem(
//    item: TodoItemBean,
//    deleteAction: () -> Unit,
//) {
//    Button(onClick = deleteAction) {
//        Text("delete")
//    }
//}
//
//fun deleteTodoItem(
//    token: String,
//    onSuccess: () -> Unit,
//    onFail: (e: Throwable) -> Unit
//) {
//    /**
//     * deleteTodoRequest.requestDeleteTodo(token)
//            .enqueue(object : Callback<DeleteTodoResponse> {
//
//            // 실패 했을때
//            override fun onFailure(call: Call<DeleteTodoResponse>, t: Throwable) {
//                onFail(t)
//            }
//
//            // 성공 했을때
//            override fun onResponse(
//                call: Call<DeleteTodoResponse>,
//                response: Response<DeleteTodoResponse>,
//            ) {
//                deleteTodoResponse = response.body()
//                if(deleteTodoResponse.code == 200){ onSuccess() }
//                else { onFail(...) }
//            }
//       }
//     */
//}
//
//fun readTodos(
//    token: String,
//    onSuccess: (List<TodoItemBean>) -> Unit,
//    onFail: (e: Throwable) -> Unit
//){
//
//}
//
//
//@Composable
//fun TodoList() {
//    val todos = remember { mutableStateListOf<TodoItemBean>() }
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
//    LaunchedEffect(Unit){
//        readTodos("token", onSuccess = {
//            todos.addAll(it)
//        }, onFail = {
//            Toast.makeText(context, "Failed to load all", Toast.LENGTH_SHORT).show()
//        })
//    }
//    LazyColumn {
//        items(todos){ item ->
//            TodoItem(item = item, deleteAction = {
//                scope.launch {
////                    deleteTodo("token", onSuccess = {
////                        todos.remove(item)
////                    }, onFail = {
////                        Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
////                    })
//
//                }
//            })
//        }
//    }
//}