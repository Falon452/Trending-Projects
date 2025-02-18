package com.falon.feed.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.viewmodel.FeedViewModel
import com.falon.theme.AppTheme

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val projects: LazyPagingItems<TrendingProject> =
        viewModel.trendingProjects.collectAsLazyPagingItems()

    when (projects.loadState.refresh) {
        is LoadState.Loading -> {
            LazyColumn(modifier = modifier) {
                items(30) { index ->
                    ShimmerPlaceholder()
                }
            }
        }

        is LoadState.Error -> {
            ErrorState(
                modifier = modifier,
                onRetry = {
                    projects.retry()
                }
            )
        }

        else -> {
            LazyColumn(modifier = modifier) {
                items(projects.itemCount) { index ->
                    val project = projects[index]
                    if (project != null) {
                        Text(text = project.name)
                    }
                }

                item {
                    if (projects.loadState.append is LoadState.Loading) {
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


@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    AppTheme {
        FeedScreen()
    }
}
