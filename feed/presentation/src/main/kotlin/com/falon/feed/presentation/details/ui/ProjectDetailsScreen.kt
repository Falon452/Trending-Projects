package com.falon.feed.presentation.details.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.falon.feed.presentation.R
import com.falon.feed.presentation.details.viewmodel.ProjectDetailsViewModel
import com.falon.feed.presentation.projects.ui.ErrorState
import dev.jeziellago.compose.markdowntext.MarkdownText

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProjectDetailsScreen(
    starResource: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectDetailsViewModel = hiltViewModel<ProjectDetailsViewModel>(),
) {
    val state by viewModel.viewState.collectAsState()
    val trendingProject = state.selectedProject
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            LogoAndNameCard(trendingProject, animatedVisibilityScope)
            Spacer(modifier = Modifier.height(16.dp))
            DescriptionAndStarsCard(starResource, trendingProject, animatedVisibilityScope)
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = stringResource(R.string.readme),
                style = MaterialTheme.typography.titleLarge,
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            if (state.isReadmeErrorVisible) {
                ErrorState(
                    onRefresh = {
                        viewModel.onRefresh()
                    },
                    painter = painterResource(R.drawable.no_wifi_icon),
                    reasonText = stringResource(R.string.something_went_wrong),
                    tryAgainText = stringResource(R.string.try_again),
                )
            } else {
                MarkdownText(
                    markdown = state.readmeContent,
                    syntaxHighlightColor = Color.Transparent,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Spacer(modifier = Modifier.height(64.dp))
        }

        FloatingActionButton(
            onClick = {
                viewModel.onExitProjectDetails()
                onExit()
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.exit)
            )
        }
    }
}
