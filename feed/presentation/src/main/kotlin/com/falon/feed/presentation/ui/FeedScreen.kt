package com.falon.feed.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.R
import com.falon.feed.presentation.viewmodel.FeedViewModel
import com.falon.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val projects: LazyPagingItems<TrendingProject> =
        viewModel.trendingProjects.collectAsLazyPagingItems()
    var isRefreshing = rememberSaveable { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val onRefresh: () -> Unit = {
        isRefreshing.value = true
        projects.refresh()
    }

    val loadState = projects.loadState.refresh
    LaunchedEffect(loadState) {
        if (loadState != Loading) {
            isRefreshing.value = false
        }
    }

    when {
        loadState is Loading -> {
            LazyColumn(modifier = modifier) {
                items(30) { index ->
                    ShimmerPlaceholder()
                }
            }
        }

        loadState is Error && projects.itemCount == 0 -> {
            ErrorState(
                modifier = modifier,
                onRefresh = {
                    projects.retry()
                },
                painter = painterResource(R.drawable.no_wifi_icon),
                reasonText = stringResource(R.string.something_went_wrong),
                tryAgainText = stringResource(R.string.try_again),

                )
        }

        loadState is NotLoading && projects.itemCount == 0 -> {
            ErrorState(
                modifier = modifier,
                onRefresh = {
                    projects.retry()
                },
                painter = painterResource(R.drawable.search),
                reasonText = stringResource(R.string.no_results),
                tryAgainText = stringResource(R.string.adjust_search)
            )
        }
        else -> {
            PullToRefreshBox(
                modifier = Modifier.padding(),
                state = state,
                isRefreshing = isRefreshing.value,
                onRefresh = onRefresh,
            ) {
                LazyColumn(modifier = modifier) {
                    items(projects.itemCount) { index ->
                        val project = projects[index]
                        if (project != null) {
                            Text(text = project.name)
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
                        } else if (projects.loadState.append is Error) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.errorContainer)
                                    .padding(16.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                            projects.retry()
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    AppTheme {
        FeedScreen()
    }
}
