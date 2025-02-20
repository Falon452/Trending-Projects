package com.falon.trendingprojects

import android.os.Build
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.falon.feed.presentation.R
import com.falon.feed.presentation.details.ui.TrendingProjectDetails
import com.falon.feed.presentation.details.viewmodel.ProjectDetailsViewModel.Companion.STAR_RESOURCE_ARG
import com.falon.feed.presentation.projects.ui.FeedScreen
import com.falon.theme.AppTheme
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
                    "details/{${STAR_RESOURCE_ARG}}}",
                    arguments = listOf(
                        navArgument(STAR_RESOURCE_ARG) { type = NavType.IntType },
                    )
                ) { backStackEntry ->
                    val starResource =
                        backStackEntry.arguments?.getInt(STAR_RESOURCE_ARG) ?: R.drawable.star0
                    TrendingProjectDetails(
                        starResource = starResource,
                        animatedVisibilityScope = this,
                        onExit = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
