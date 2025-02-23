package com.test.video

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.test.video.ui.theme.VideoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoAppTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "video_list") {
                    composable("video_list") {
                        VideoListScreen(navController)
                    }
                    composable(
                        "video_player/{videoUrl}",
                        arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                        VideoPlayerScreen(videoUrl, navController)
                    }
                }
            }
        }
    }
}
