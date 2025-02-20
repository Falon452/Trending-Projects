package com.falon.feed.presentation.details.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.falon.feed.domain.model.TrendingProject
import com.falon.feed.presentation.R
import com.falon.feed.presentation.projects.model.ProjectSharedElementKey
import com.falon.feed.presentation.projects.model.ProjectSharedElementType

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun SharedTransitionScope.DescriptionAndStarsCard(
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
