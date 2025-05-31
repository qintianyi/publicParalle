package com.tianyi.parallelspace.ui.main.add

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tianyi.parallelspace.R
import com.tianyi.parallelspace.data.model.AppInfo
import com.tianyi.parallelspace.data.model.UiEvent
import com.tianyi.parallelspace.data.model.UiState
import com.tianyi.parallelspace.data.model.VirtualSpaceInfo
import com.tianyi.parallelspace.theme.ParallelSpaceAppTheme
import com.tianyi.parallelspace.ui.ParallelSpaceViewModel
import com.tianyi.parallelspace.ui.widget.AppItem
import com.tianyi.parallelspace.util.gridItems
import kotlinx.coroutines.launch
import java.util.stream.IntStream.range

@Composable
fun ParallelSpaceAddScreen(
    mainViewModel: ParallelSpaceViewModel,
    viewModel: ParallelSpaceAddViewModel = viewModel(factory = object : ViewModelProvider.Factory{
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return ParallelSpaceAddViewModel(mainViewModel) as T
        }
    })
) {
    //ui Event listen
//    var isConfirmDialogShow by remember { mutableStateOf(false) }
//    var installApp by remember { mutableStateOf<AppInfo?>(null) }
//    var avaSpaces by remember { mutableStateOf<List<VirtualSpaceInfo>>(emptyList()) }
//    LaunchedEffect(Unit) {
//        viewModel.uiEvent.collect { event ->
//            when (event) {
//                is UiEvent.ShowConfirmInstallDialog -> {
//                    isConfirmDialogShow = true
//                    installApp = event.appInfo
//                    avaSpaces = event.availableSpaceInfoList
//                }
//                else ->{}
//            }
//        }
//    }

    //ui State listen
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (state) {
        is UiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }

        is UiState.Error -> {
            Text((state as UiState.Error).message, style = MaterialTheme.typography.headlineSmall)
        }
        is UiState.Empty -> {
            Text(
                stringResource(R.string.add_empty_content),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        is UiState.Success -> {
            var installApp by remember { mutableStateOf<AppInfo?>(null) }

            AppListScreen((state as UiState.Success).data.toLetterMap()) {
                installApp = it
            }
            if (installApp != null) {
                val installAvaListState by viewModel.getInstallAvailableSpace(installApp!!).collectAsStateWithLifecycle(UiState.Loading)
                if (installAvaListState is UiState.Success) {
                    ConfirmInstallDialog(installApp!!, (installAvaListState as UiState.Success).data,{
                        //cancel click
                        installApp = null
                    }, {
                        //confirm click
                        viewModel.confirmInstall(installApp!!, it)
                        installApp = null
                    })
                } else if (installAvaListState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                } else {
                    // TODO: handle error
                }
            }
        }
    }
}

private fun List<AppInfo>.toLetterMap(): Map<Char, List<AppInfo>> {
    return this.groupBy { it.appName[0].uppercaseChar() }.toSortedMap()
}

@Composable
fun AppListScreen(appMap: Map<Char, List<AppInfo>>, onItemClick: (AppInfo) -> Unit) {
    val letters = appMap.keys.toList()// 英文字母列表
    val letterApps = appMap.values.toList()// 英文字母列表
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    var selectedLetter by remember { mutableStateOf<Char?>(null) } // 当前选中的字母
    val firstVisibleItem by remember { derivedStateOf { scrollState.firstVisibleItemIndex } }
    // 滚动时高亮字母
    LaunchedEffect(firstVisibleItem) {
        // 计算当前字母
        val currentLetter = letters.getOrNull(scrollState.firstVisibleItemIndex)
        selectedLetter = currentLetter
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // 左侧应用列表
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {

            appMap.forEach { (letter, apps) ->
                item{
                    Text(
                        text = letter.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                gridItems(
                    data = apps,
                    columnCount = 4,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){ app->
                    AppItem(app, onClick = { onItemClick(app)})
                }
            }
        }

        // 右侧字母滚动条
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .width(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            letters.forEach { letter ->
                // 根据选中的字母来改变样式
                val isSelected = selectedLetter == letter
//                val fontSize: Float by animateFloatAsState(
//                    targetValue = if (isSelected) 20f else 14f, // 选中时字体变大
//                    animationSpec = tween(durationMillis = 300)
//                )
                val color by animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                    animationSpec = tween(durationMillis = 300)
                )

                Text(
                    text = letter.toString(),
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                // 滚动到相应的字母分组
                                val index = letters.indexOf(letter)
                                scrollState.animateScrollToItem(index)
                            }
                        }
                        .padding(4.dp),
//                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = fontSize.sp),
                    color = color
                )
            }
        }
    }
}

@Composable
fun ConfirmInstallDialog(appInfo: AppInfo, availableSpaceInfoList: List<VirtualSpaceInfo>, onCancel: () -> Unit, onConfirm: (List<VirtualSpaceInfo>) -> Unit) {
    Dialog(onDismissRequest = {}) {
        Surface (
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 5.dp
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.add_dialog_confirm_title), style = MaterialTheme.typography.headlineMedium)
                AppItem(appInfo)
                Text(modifier = Modifier.padding(top = 24.dp), text = stringResource(R.string.add_dialog_confirm_subtitle), style = MaterialTheme.typography.headlineSmall)

                val selectedList = remember { mutableStateListOf<VirtualSpaceInfo>() }
                LazyColumn(Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)) {
                    items(availableSpaceInfoList){ spaceInfo->
                        SpaceItem(isSelected = selectedList.contains(spaceInfo), spaceInfo = spaceInfo) {
                            if (it) selectedList.add(spaceInfo) else selectedList.remove(spaceInfo)
                        }
                    }
                }

                Row(Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)) {
                    Button(onClick = onCancel) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(enabled = selectedList.isNotEmpty(), onClick = { onConfirm(selectedList) }) {
                        Text("Confirm")
                    }
                }

            }
        }
    }
}

@Composable
fun SpaceItem(isSelected: Boolean, spaceInfo: VirtualSpaceInfo, onStateChange: (Boolean) -> Unit) {
//    val (checkedState, onStateChange) = remember { mutableStateOf(true) }
    Row (Modifier
        .fillMaxWidth()
        .toggleable(
            value = isSelected,
            onValueChange = { onStateChange(it) },
            role = Role.Checkbox
        )
        .padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment. CenterVertically) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = null
        )
        Text(
            text = spaceInfo.spaceName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}


@Preview(
    showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_APPLIANCE
)
@Composable
fun ConfirmInstallDialogPreview() {
    ParallelSpaceAppTheme {
        ConfirmInstallDialog(AppInfo("", "aaaa", AppCompatResources.getDrawable(LocalContext.current, R.mipmap.ic_launcher)!!),
            List(50){ VirtualSpaceInfo(it, emptyList()) }
            ,{}, {})
    }
}

