package com.falon.feed.presentation.details.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.R
import com.falon.feed.presentation.details.viewmodel.ProjectDetailsViewModel
import com.falon.feed.presentation.projects.model.ProjectSharedElementKey
import com.falon.feed.presentation.projects.model.ProjectSharedElementType
import com.falon.feed.presentation.projects.ui.AvatarImage
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
    val projectDetailsState by viewModel.projectDetails.collectAsState()
    val trendingProject = projectDetailsState.selectedProject
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
            MarkdownText(
                markdown = projectDetailsState.readmeContent ?: "",
                syntaxHighlightColor = Color.Transparent,
                style = MaterialTheme.typography.bodyLarge,
            )
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.LogoAndNameCard(
    project: TrendingProject,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(project.htmlUrl)
                )
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AvatarImage(
                    imageUrl = project.ownerAvatarUrl,
                    modifier = Modifier
                        .size(40.dp)
                        .sharedElement(
                            state = rememberSharedContentState(
                                key = ProjectSharedElementKey(
                                    projectId = project.id.toString(),
                                    type = ProjectSharedElementType.AvatarImage,
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${project.ownerLogin} / ${project.repositoryName}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .weight(1f)
                        .sharedElement(
                            state = rememberSharedContentState(
                                key = ProjectSharedElementKey(
                                    projectId = project.id,
                                    type = ProjectSharedElementType.Title,
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.DescriptionAndStarsCard(
    starResource: Int,
    projectDetails: TrendingProject,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = projectDetails.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 28.sp,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = starResource),
                    contentDescription = stringResource(R.string.stars),
                    modifier = Modifier
                        .size(24.dp)
                        .sharedElement(
                            state = rememberSharedContentState(
                                key = ProjectSharedElementKey(
                                    projectId = projectDetails.id,
                                    type = ProjectSharedElementType.StarImage,
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = projectDetails.stars.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(
                            key = ProjectSharedElementKey(
                                projectId = projectDetails.id,
                                type = ProjectSharedElementType.Stars,
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                )
            }
        }
    }
}
