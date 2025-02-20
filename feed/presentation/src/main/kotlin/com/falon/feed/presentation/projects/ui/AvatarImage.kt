package com.falon.feed.presentation.projects.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.falon.feed.presentation.R

@Composable
fun AvatarImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            placeholder = painterResource(R.drawable.placeholder_avatar),
            error = painterResource(R.drawable.avatar_error)
        )
    }
}

@Preview
@Composable
fun AvatarImagePreview() {
    AvatarImage(imageUrl = "")
}
