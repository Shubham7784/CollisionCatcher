package com.collisioncatcher.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.collisioncatcher.ui.navigation.Routes.SplashScreen
import com.collisioncatcher.ui.screens.AddContact
import com.collisioncatcher.ui.screens.EmergencyContactsScreen
import com.collisioncatcher.ui.screens.ForgotScreen
import com.collisioncatcher.ui.screens.HomeScaffold
import com.collisioncatcher.ui.screens.LoginScreen
import com.collisioncatcher.ui.screens.ProfileScreen
import com.collisioncatcher.ui.screens.RegisterScreen
import com.collisioncatcher.ui.screens.SafetyTipsScreen
import com.collisioncatcher.ui.screens.SplashScreen

object Routes {
    const val SplashScreen = "splash"
    const val Login = "login"
    const val Register = "register"
    const val Forgot = "forgot"
    const val Home = "home"
    const val Contacts = "contacts"
    const val Tips = "tips"
    const val Profile = "profile"

    const val AddContact = "addContact"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SplashScreen) {
        composable(Routes.SplashScreen) {
            SplashScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home) {
                        popUpTo(SplashScreen) {
                            inclusive = true
                        }
                    }
                },
                context = LocalContext.current,
                onLoginFailure = {
                    navController.navigate(Routes.Login)
                    {
                        popUpTo(SplashScreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Routes.Login) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Login) {
                            inclusive = true
                        }
                    }
                },
                onRegister = { navController.navigate(Routes.Register) },
                onForgot = { navController.navigate(Routes.Forgot) },
                context = LocalContext.current
            )
        }
        composable(Routes.Register) {
            RegisterScreen(
                onSignUpSuccess = {
                    navController.navigate(Routes.Login)
                    {
                        popUpTo(Routes.Register) {
                            inclusive = true
                        }
                    }
                },
                onBack = { navController.popBackStack() },
                context = LocalContext.current
            )
        }
        composable(Routes.Forgot) {
            ForgotScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.Home) {
            HomeScaffold(
                onLogout = {
                    navController.navigate(Routes.Login)
                    {
                        popUpTo(0)
                    }
                },
                onOpenContacts = { navController.navigate(Routes.Contacts) },
                onOpenTips = { navController.navigate(Routes.Tips) },
                context = LocalContext.current
            )
        }
        composable(Routes.Contacts) {
            EmergencyContactsScreen(
                LocalContext.current,
                onAddContact = { navController.navigate(Routes.AddContact) },
                onBack = { navController.popBackStack() })
        }
        composable(Routes.Tips) {
            SafetyTipsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.Profile)
        {
            ProfileScreen(context = LocalContext.current)
        }
        composable(Routes.AddContact) {
            AddContact(onSuccess = { navController.popBackStack() }, context = LocalContext.current)
        }
    }
}
