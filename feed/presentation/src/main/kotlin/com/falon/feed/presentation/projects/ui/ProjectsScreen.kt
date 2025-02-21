package com.falon.feed.presentation.projects.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.R
import com.falon.feed.presentation.projects.viewmodel.ProjectsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProjectsScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: ProjectsViewModel = hiltViewModel(),
) {
    val projects: LazyPagingItems<TrendingProject> =
        viewModel.trendingProjects.collectAsLazyPagingItems()
    val state = viewModel.viewState.collectAsStateWithLifecycle()
    var isRefreshing = rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        isRefreshing.value = true
        projects.refresh()
    }

    LaunchedEffect(projects.loadState.refresh) {
        if (projects.loadState.refresh != Loading) {
            isRefreshing.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.trending_projects)) },
                actions = {
                    Switch(
                        checked = state.value.isDarkMode,
                        onCheckedChange = { viewModel.onToggleTheme() }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = {
                        viewModel.onDateClicked()
                    }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = stringResource(R.string.date_picker)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    ) { padding ->
        if (state.value.showDatePicker) {
            DatePickerDialog(
                onDismissRequest = viewModel::onDismissRequest,
                confirmButton = {
                    TextButton(onClick = viewModel::onDatePickerConfirmButtonClicked) {
                        Text(stringResource(R.string.ok))
                    }
                }
            ) {
                val state =
                    rememberDatePickerState(initialSelectedDateMillis = state.value.initialSelectedDateMillis)
                DatePicker(state = state)
                LaunchedEffect(state.selectedDateMillis) {
                    viewModel.onDateSelected(state.selectedDateMillis)
                }
            }
        }
        when {
            projects.loadState.refresh is Loading -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                        .padding(padding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(20) { index ->
                        ShimmerPlaceholder()
                    }
                }
            }

            projects.loadState.refresh is Error -> {
                ErrorState(
                    modifier = modifier.padding(padding),
                    onRefresh = {
                        projects.refresh()
                    },
                    painter = painterResource(R.drawable.no_wifi_icon),
                    reasonText = stringResource(R.string.something_went_wrong),
                    tryAgainText = stringResource(R.string.try_again),
                )
            }

            projects.loadState.refresh is NotLoading && projects.itemCount == 0 -> {
                ErrorState(
                    modifier = modifier.padding(padding),
                    onRefresh = {
                        projects.refresh()
                    },
                    painter = painterResource(R.drawable.search),
                    reasonText = stringResource(R.string.no_results),
                    tryAgainText = stringResource(R.string.adjust_search)
                )
            }

            else -> {
                LoadedState(
                    modifier = modifier.padding(padding),
                    pullToRefreshState = pullToRefreshState,
                    isRefreshing = isRefreshing.value,
                    onRefresh = onRefresh,
                    navController = navController,
                    viewModel = viewModel,
                    projects = projects,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.LoadedState(
    modifier: Modifier = Modifier,
    pullToRefreshState: PullToRefreshState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    navController: NavController,
    viewModel: ProjectsViewModel,
    projects: LazyPagingItems<TrendingProject>,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    key(MaterialTheme.colorScheme) {
        PullToRefreshBox(
            modifier = modifier,
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = listState,
            ) {
                items(projects.itemCount) { index ->
                    val project = projects[index]
                    if (project != null) {
                        val starRes = getStarResource(index = index)
                        TrendingProjectCard(
                            project = project,
                            onClick = {
                                viewModel.onTrendingProjectCardClicked(project)
                                navController.navigate(
                                    "details/${project.id}/$starRes"
                                )
                            },
                            starPainter = painterResource(starRes),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                    }
                }

                item {
                    if (projects.loadState.append is Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

private fun getStarResource(index: Int) =
    when (index % 5) {
        0 -> R.drawable.star0
        1 -> R.drawable.star1
        2 -> R.drawable.star2
        3 -> R.drawable.star3
        else -> R.drawable.star4
    }
