package com.tianyi.parallelspace.ui.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tianyi.parallelspace.R
import com.tianyi.parallelspace.data.model.AppInfo
import com.tianyi.parallelspace.data.model.UiState
import com.tianyi.parallelspace.data.model.VirtualSpaceInfo
import com.tianyi.parallelspace.ui.ParallelSpaceViewModel
import com.tianyi.parallelspace.ui.widget.AppItem
import com.tianyi.parallelspace.ui.widget.DeletableAppItem
import com.tianyi.parallelspace.ui.widget.ExtendableFloatingActionButton
import com.tianyi.parallelspace.ui.widget.MiniFabItems
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ParallelSpaceHomeScreen(
    mainViewModel: ParallelSpaceViewModel,
    viewModel: ParallelSpaceHomeViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
            return ParallelSpaceHomeViewModel(mainViewModel) as T
        }
    }),
    onNavToAddScreen: () -> Unit
) {
  val state by viewModel.uiState.collectAsState()
//    var state: UiState<List<VirtualSpaceInfo>> by remember { mutableStateOf(UiState.Loading) }
//    LaunchedEffect(Unit) {
//        viewModel.uiState.collectLatest {
//            state = it
//        }
//    }
    var deleteState by remember { mutableIntStateOf(DELETE_STATE_NORMAL) }

    when (state) {
        is UiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
        }

        is UiState.Error -> {
            Text((state as UiState.Error).message, style = MaterialTheme.typography.headlineSmall)
        }

        is UiState.Empty -> {
            Text(
                stringResource(R.string.home_empty_content),
                style = MaterialTheme.typography.headlineSmall
            )
            Button(
                modifier = Modifier.wrapContentSize(),
                onClick = onNavToAddScreen
            ) {
                Text(
                    stringResource(R.string.home_empty_button),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        is UiState.Success -> {
            val spaceList = (state as UiState.Success<List<VirtualSpaceInfo>>).data
            if (spaceList.isNotEmpty()) {

                    VirtualSpacesPage( deleteState, spaceList,
                        onItemClick = { appInfo, spaceInfo ->
                            viewModel.onItemClick(appInfo, spaceInfo)
                        },
                        onDeleteClick = { appInfo, spaceInfo ->
                            viewModel.onAppDeleteClick(appInfo, spaceInfo)
                        },
                        onDeletePageClick = { spaceInfo ->
                            viewModel.onPageDeleteClick(spaceInfo)
                        },
                        onDeleteStateClick = {
                            deleteState = it
                        })


            }
        }
    }
}

const val DELETE_STATE_NORMAL = 0
const val DELETE_STATE_APP = 1
const val DELETE_STATE_PAGE = 2

@Composable
fun VirtualSpacesPage(
    deleteState: Int,
    spaceList: List<VirtualSpaceInfo>,
    onItemClick: ((AppInfo, VirtualSpaceInfo) -> Unit),
    onDeleteClick: ((AppInfo, VirtualSpaceInfo) -> Unit),
    onDeletePageClick: ((VirtualSpaceInfo) -> Unit),
    onDeleteStateClick: (Int)-> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val selectedSpaceInfo = remember(spaceList, selectedTab) {
        if (spaceList.size == selectedTab) {
            selectedTab -= 1
        }
        spaceList[selectedTab]
    }
    val expandList = remember {
        listOf(
            MiniFabItems(Icons.Filled.Home, "Delete App") to {
                onDeleteStateClick(DELETE_STATE_APP)
            },
            MiniFabItems(Icons.Filled.Person, "Delete Page") to {
                onDeleteStateClick(DELETE_STATE_PAGE)
            })
    }

    Scaffold(floatingActionButton = {
        if (deleteState != DELETE_STATE_NORMAL) {
            FloatingActionButton(onClick = {
                onDeleteStateClick(DELETE_STATE_NORMAL) }) {
                Text("Done")
            }
        } else {
            ExtendableFloatingActionButton(items = expandList)
        }

    }){ paddingVaules->
    Column(modifier = Modifier
        .padding(paddingVaules)
        .fillMaxSize()) {
        // Scrollable Tab Row with 10 tabs
        ScrollableTabRow(selectedTabIndex = selectedTab) {
            spaceList.forEachIndexed { index, spaceInfo ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = spaceInfo.spaceName)
                            if (deleteState == DELETE_STATE_PAGE) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { onDeletePageClick(spaceInfo) })
                            }
                        }
                    }
                )
            }
        }

        // Grid of Apps
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {

            items(selectedSpaceInfo.virtualAppList) { appInfo ->
                if (deleteState == DELETE_STATE_APP) {
                    DeletableAppItem(appInfo) {
                        onDeleteClick(appInfo, selectedSpaceInfo)
                    }
                } else {
                    AppItem(appInfo) {
                        onItemClick(appInfo, selectedSpaceInfo)
                    }
                }

            }
        }
    }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewParallelSpaceScreen() {
}
