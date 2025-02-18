package com.falon.feed.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.falon.feed.presentation.viewmodel.FeedViewModel
import com.falon.theme.AppTheme

@Composable
fun FeedScreen(
    name: String,
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    AppTheme {
        FeedScreen("Android")
    }
}
