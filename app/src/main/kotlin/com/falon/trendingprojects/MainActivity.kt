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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.falon.feed.presentation.details.ui.ProjectDetailsScreen
import com.falon.feed.presentation.details.viewmodel.ProjectDetailsViewModel.Companion.STAR_RESOURCE_ARG
import com.falon.feed.presentation.projects.ui.FeedScreen
import com.falon.theme.ThemePreferences
import com.falon.theme.ui.AppTheme
import com.falon.trendingprojects.util.shrinkAndFadeAnimationRun
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import com.falon.feed.presentation.R as FeedR

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashScreen()
        enableEdgeToEdge()
        setContent {
            val darkMode =
                themePreferences.observeIsDarkMode().collectAsState(initial = false)
            AppTheme(darkMode.value) {
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
                    FeedScreen(navController, this)
                }
                composable(
                    "${Routes.PROJECT_DETAIL}/{$STAR_RESOURCE_ARG}",
                    arguments = listOf(
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
