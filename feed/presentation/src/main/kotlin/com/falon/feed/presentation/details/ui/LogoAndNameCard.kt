package com.falon.feed.presentation.details.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.projects.model.ProjectSharedElementKey
import com.falon.feed.presentation.projects.model.ProjectSharedElementType
import com.falon.feed.presentation.projects.ui.AvatarImage

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun SharedTransitionScope.LogoAndNameCard(
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
