package com.falon.feed.presentation.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.model.ProjectSharedElementKey
import com.falon.feed.presentation.model.ProjectSharedElementType

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TrendingProjectCard(
    project: TrendingProject,
    modifier: Modifier = Modifier,
    starPainter: Painter,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
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
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(1f)
                            .sharedElement(
                                state = rememberSharedContentState(
                                    key = ProjectSharedElementKey(
                                        projectId = project.id.toString(),
                                        type = ProjectSharedElementType.Title,
                                    )
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = starPainter,
                            contentDescription = "Star",
                            modifier = Modifier
                                .size(32.dp)
                                .sharedElement(
                                    state = rememberSharedContentState(
                                        key = ProjectSharedElementKey(
                                            projectId = project.id.toString(),
                                            type = ProjectSharedElementType.StarImage,
                                        )
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                ),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = project.stars.toString(),
                            modifier = Modifier.sharedElement(
                                state = rememberSharedContentState(
                                    key = ProjectSharedElementKey(
                                        projectId = project.id.toString(),
                                        type = ProjectSharedElementType.Stars,
                                    )
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = project.description,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(
                            key = ProjectSharedElementKey(
                                projectId = project.id.toString(),
                                type = ProjectSharedElementType.Description,
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
