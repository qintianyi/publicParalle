package com.tianyi.parallelspace.ui.widget

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Css
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.tianyi.parallelspace.R
import com.tianyi.parallelspace.data.model.AppInfo


@Composable
fun ParallelSpacePrivacyPolicyTopAppBar() {

}

@ExperimentalMaterial3Api
@Composable
fun ParallelSpaceTopAppBar() {
    TopAppBar(title = {

    })
}

@Composable
fun AppItem(appInfo: AppInfo,modifier: Modifier = Modifier, onClick: (()->Unit)? = null) {
    Column (
        modifier = modifier
            .size(80.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable { onClick?.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bmpPainter = BitmapPainter(appInfo.appIcon.toBitmap().asImageBitmap())
        Image(modifier = Modifier.size(56.dp),painter = bmpPainter, contentDescription = null)
        Text(text = appInfo.appName, color = Color.White)
    }
}

@Composable
fun DeletableAppItem(appInfo: AppInfo, onDelete: (() -> Unit)? = null) {
    Box{
        AppItem(appInfo, onClick = {})
        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier
            .align(Alignment.TopEnd)
            .size(24.dp)
            .clickable { onDelete?.invoke() })
    }
}


@Composable
fun ExtendableFloatingActionButton(items: List<Pair<MiniFabItems, () -> Unit>>, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
//    val items = listOf(
//        MiniFabItems(Icons.Filled.Home, "Home"),
//        MiniFabItems(Icons.Filled.Person, "Person"),
//        MiniFabItems(Icons.Filled.Build, "Settings")
//    )
    Column(modifier = modifier.wrapContentSize(), horizontalAlignment = Alignment.End) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }) + expandVertically(),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }) + shrinkVertically()
        ) {
            LazyColumn(Modifier.padding(bottom = 8.dp)) {
                items(items.size) {
                    ItemUi(modifier = Modifier.clickable { items[it].second.invoke() }, icon = items[it].first.icon, title = items[it].first.title)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        val transition = updateTransition(targetState = expanded, label = "transition")
        val rotation by transition.animateFloat(label = "rotation") {
            if (it) 315f else 0f
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = Color(0xFFFF9800)
        ) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
private fun ItemUi(icon: ImageVector, title: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .border(2.dp, Color(0xFFFF9800), RoundedCornerShape(10.dp))
                .padding(6.dp)
        ) {
            Text(text = title)
        }
        Spacer(modifier = Modifier.width(10.dp))
        FloatingActionButton(onClick = {}, modifier = Modifier.size(45.dp), containerColor = Color(0xFFFF9800)) {
            Icon(imageVector = icon, contentDescription = "")
        }
    }
}

data class MiniFabItems(
    val icon: ImageVector,
    val title: String
)