package com.falon.feed.presentation.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.R
import com.falon.feed.presentation.utils.encodeUrl
import com.falon.feed.presentation.viewmodel.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FeedScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val projects: LazyPagingItems<TrendingProject> =
        viewModel.trendingProjects.collectAsLazyPagingItems()
    var isRefreshing = rememberSaveable { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
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
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(20) { index ->
                    ShimmerPlaceholder()
                }
            }
        }
        loadState is Error && projects.itemCount == 0 -> {
            ErrorState(
                modifier = modifier,
                onRefresh = {
                    projects.refresh()
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
                    projects.refresh()
                },
                painter = painterResource(R.drawable.search),
                reasonText = stringResource(R.string.no_results),
                tryAgainText = stringResource(R.string.adjust_search)
            )
        }
        else -> {
            PullToRefreshBox(
                modifier = Modifier.padding(),
                state = pullToRefreshState,
                isRefreshing = isRefreshing.value,
                onRefresh = onRefresh,
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(projects.itemCount) { index ->
                        val project = projects[index]
                        if (project != null) {
                            val starRes = when (index % 5) {
                                0 -> R.drawable.star0
                                1 -> R.drawable.star1
                                2 -> R.drawable.star2
                                3 -> R.drawable.star3
                                else -> R.drawable.star4
                            }
                            TrendingProjectCard(
                                project = project,
                                onClick = {
                                    val title =
                                        "${project.ownerLogin} / ${project.repositoryName}".encodeUrl()
                                    val desc = project.description.encodeUrl()
                                    val ownerAvatarUrl = project.ownerAvatarUrl.encodeUrl()
                                    val stars = project.stars
                                    val id = project.id
                                    navController.navigate(
                                        "details/$id/$title/$ownerAvatarUrl/$desc/$starRes/$stars"
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
}
