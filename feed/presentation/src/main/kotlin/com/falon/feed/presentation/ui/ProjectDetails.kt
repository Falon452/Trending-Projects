package com.falon.feed.presentation.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.falon.feed.presentation.model.ProjectSharedElementKey
import com.falon.feed.presentation.model.ProjectSharedElementType

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TrendingProjectDetails(
    id: String,
    title: String,
    ownerAvatarUrl: String,
    description: String,
    starsResId: Int,
    stars: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            AvatarImage(
                imageUrl = ownerAvatarUrl,
                modifier = Modifier
                    .size(40.dp)
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = ProjectSharedElementKey(
                                projectId = id.toString(),
                                type = ProjectSharedElementType.AvatarImage,
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .weight(1f)
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = ProjectSharedElementKey(
                                projectId = id.toString(),
                                type = ProjectSharedElementType.Title,
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .sharedElement(
                    state = rememberSharedContentState(
                        key = ProjectSharedElementKey(
                            projectId = id.toString(),
                            type = ProjectSharedElementType.Description,
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = starsResId),
                contentDescription = "Stars",
                modifier = Modifier
                    .size(24.dp)
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = ProjectSharedElementKey(
                                projectId = id.toString(),
                                type = ProjectSharedElementType.StarImage,
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$stars stars",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(
                        key = ProjectSharedElementKey(
                            projectId = id.toString(),
                            type = ProjectSharedElementType.Stars,
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onExit,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Exit")
        }
    }
}
