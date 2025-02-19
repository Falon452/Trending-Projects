package com.falon.trendingprojects

import android.os.Build
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.falon.feed.presentation.R
import com.falon.feed.presentation.ui.FeedScreen
import com.falon.feed.presentation.ui.TrendingProjectDetails
import com.falon.theme.AppTheme
import com.falon.trendingprojects.utils.decodeUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashScreenAnimation()
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }

    private fun setupSplashScreenAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.iconView?.animate()?.apply {
                    scaleX(0.8f)
                    scaleY(0.8f)
                    alpha(0f)
                    interpolator = AccelerateInterpolator()
                    duration = 400L
                    withEndAction { splashScreenView.remove() }
                    start()
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        SharedTransitionLayout {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "trendingProjects") {
                composable("trendingProjects") { backStackEntry ->
                    FeedScreen(navController, this)
                }
                composable(
                    "details/{id}/{title}/{ownerAvatarUrl}/{description}/{starsResId}/{stars}",
                    arguments = listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        },
                        navArgument("title") {
                            type = NavType.StringType
                        },
                        navArgument("ownerAvatarUrl") {
                            type = NavType.StringType
                        },
                        navArgument("description") {
                            type = NavType.StringType
                        },
                        navArgument("starsResId") {
                            type = NavType.IntType
                        },
                        navArgument("stars") {
                            type = NavType.IntType
                        },
                    )
                ) { backStackEntry ->
                    val args = backStackEntry.arguments
                    val id = args?.getString("id") ?: ""
                    val title = args?.getString("title")?.decodeUrl() ?: ""
                    val ownerAvatarUrl = args?.getString("ownerAvatarUrl")?.decodeUrl() ?: ""
                    val description = args?.getString("description")?.decodeUrl() ?: ""
                    val starsResId = args?.getInt("starsResId") ?: R.drawable.star0
                    val stars = args?.getInt("stars") ?: 0
                    TrendingProjectDetails(
                        id = id,
                        title = title,
                        ownerAvatarUrl = ownerAvatarUrl,
                        description = description,
                        starsResId = starsResId,
                        stars = stars,
                        animatedVisibilityScope = this,
                        onExit = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        SharedTransitionScope {
//            FeedScreen(rememberNavController())
        }
    }
}
