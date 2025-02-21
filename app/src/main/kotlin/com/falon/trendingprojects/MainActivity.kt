package com.falon.trendingprojects

import android.os.Build
import android.os.Bundle
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
import com.falon.feed.presentation.details.ui.ProjectDetailsScreen
import com.falon.feed.presentation.details.viewmodel.ProjectDetailsViewModel.Companion.PROJECT_ID
import com.falon.feed.presentation.details.viewmodel.ProjectDetailsViewModel.Companion.STAR_RESOURCE_ARG
import com.falon.feed.presentation.projects.ui.ProjectsScreen
import com.falon.theme.ui.AppTheme
import com.falon.trendingprojects.util.shrinkAndFadeAnimationRun
import dagger.hilt.android.AndroidEntryPoint
import com.falon.feed.presentation.R as FeedR

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashScreen()
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }

    private fun setupSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                splashScreenView.iconView?.shrinkAndFadeAnimationRun {
                    splashScreenView.remove()
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
            NavHost(navController, startDestination = Routes.PROJECTS) {
                composable(Routes.PROJECTS) {
                    ProjectsScreen(navController, this)
                }
                composable(
                    "${Routes.PROJECT_DETAIL}/{$PROJECT_ID}/{$STAR_RESOURCE_ARG}",
                    arguments = listOf(
                        navArgument(PROJECT_ID) { type = NavType.StringType },
                        navArgument(STAR_RESOURCE_ARG) { type = NavType.IntType },
                    )
                ) { backStackEntry ->
                    val starResource =
                        backStackEntry.arguments?.getInt(STAR_RESOURCE_ARG) ?: FeedR.drawable.star0
                    ProjectDetailsScreen(
                        starResource = starResource,
                        animatedVisibilityScope = this,
                        onExit = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
