package com.defey.testapp.presentation

import android.content.res.Resources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.defey.testapp.R
import com.defey.testapp.domain.model.Door
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = stringResource(R.string.title_home),
                    color = Color.Black
                )
            })
        },
    ) { paddingValues ->
        val tabs = listOf(stringResource(R.string.cameras), stringResource(R.string.doors))
        var selectedTabIndex by remember { mutableIntStateOf(0) }

        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color(0XFF03A9F4)
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                text = title,
                                fontSize = 20.sp
                            )
                        },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }
            val categoryList = state.value.cameraList.map {
                Category(
                    name = it.key.toString(),
                    items = it.value
                )
            }
            when (selectedTabIndex) {
                0 -> {
                    CameraList(categoryList, state.value.loadCamera, viewModel)
                }

                1 -> {
                    DoorList(state.value.doorList, state.value.loadDoor, viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CameraList(camerasCategory: List<Category>, loadCamera: Boolean, viewModel: HomeViewModel) {
    val swipeRefreshState = rememberPullRefreshState(loadCamera, { viewModel.syncCamera() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(swipeRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 21.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            camerasCategory.forEach { category ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = category.name ?: "No Category"
                        )
                    }
                }

                items(category.items) { camera ->
                    var expanded by remember { mutableStateOf(false) }
                    var offsetX by remember { mutableFloatStateOf(0f) }
                    Box(modifier = Modifier.fillMaxSize()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset { IntOffset(offsetX.roundToInt(), 0) }
                                .draggable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberDraggableState { delta ->
                                        when {
                                            offsetX + delta < 0 && offsetX >= (-26).toPx() -> {
                                                offsetX += (-26).toPx()
                                                expanded = true
                                            }

                                            delta > 0 -> {
                                                offsetX = 0f
                                                expanded = false
                                            }
                                        }
                                    },
                                ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = camera.snapshot,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(4f / 3f),
                                    contentScale = ContentScale.FillWidth
                                )
                                if (camera.rec) {
                                    Icon(
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(8.dp),
                                        imageVector = ImageVector.vectorResource(R.drawable.video_recording),
                                        contentDescription = null,
                                        tint = Color(0xFFFF0000)
                                    )
                                }
                                if (camera.favorites) {
                                    Icon(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp),
                                        imageVector = ImageVector.vectorResource(R.drawable.star_fill),
                                        contentDescription = null,
                                        tint = Color(0XFFE0BE35)
                                    )
                                }
                            }

                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = camera.name
                            )
                        }
                        AnimatedVisibility(
                            visible = expanded,
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier
                                    .padding(bottom = 60.dp)
                                    .size(40.dp),
                                shape = CircleShape,
                                border = BorderStroke(1.dp, Color(0XFFDBDBDB)),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(
                                        0XFFE0BE35
                                    )
                                )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.star),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier)
            }
        }
        PullRefreshIndicator(loadCamera, swipeRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorList(doors: List<Door>, loadDoor: Boolean, viewModel: HomeViewModel) {
    val swipeRefreshState = rememberPullRefreshState(loadDoor, { viewModel.syncDoor() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(swipeRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(doors) { door ->
                var expanded by remember { mutableStateOf(false) }
                var offsetX by remember { mutableFloatStateOf(0f) }
                Box(modifier = Modifier.fillMaxSize()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset { IntOffset(offsetX.roundToInt(), 0) }
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state = rememberDraggableState { delta ->
                                    when {
                                        offsetX + delta < 0 && offsetX >= (-46).toPx() -> {
                                            offsetX += (-46).toPx()
                                            expanded = true
                                        }

                                        delta > 0 -> {
                                            offsetX = 0f
                                            expanded = false
                                        }
                                    }

                                },
                            ),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (door.snapshot != null) {
                                AsyncImage(
                                    model = door.snapshot,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(4f / 3f),
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = door.name
                            )
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(end = 16.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.lock),
                                contentDescription = null,
                                tint = Color(0XFF03A9F4)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Row {
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp),
                                shape = CircleShape,
                                border = BorderStroke(1.dp, Color(0XFFDBDBDB)),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0XFF03A9F4)
                                )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.edit_alt),
                                    contentDescription = null
                                )
                            }
                            OutlinedButton(
                                onClick = { },
                                modifier = Modifier
                                    .padding()
                                    .size(40.dp),
                                shape = CircleShape,
                                border = BorderStroke(1.dp, Color(0XFFDBDBDB)),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0XFFE0BE35)
                                )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.star),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier)
            }
        }

        PullRefreshIndicator(loadDoor, swipeRefreshState, Modifier.align(Alignment.TopCenter))
    }
}

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()