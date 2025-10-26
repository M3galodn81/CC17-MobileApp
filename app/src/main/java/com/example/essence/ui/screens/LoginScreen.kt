package com.example.essence.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.essence.data.local.SessionManager
import com.example.essence.data.model.UserRole
import com.example.essence.data.sample.SampleData


// --- Navigation Routes must be defined in one place, so we place them here ---
object Routes {
    const val LOGIN = "login_route"
    const val REGISTRATION = "register_route"
    const val DASHBOARD = "dashboard_route"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(navController: NavController,
                onLoginSuccess: (UserRole) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Light gray background for contrast
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var emailUserInput by remember { mutableStateOf("user@example.com") }
        var passwordUserInput by remember { mutableStateOf("Password") }
        var passwordVisible by remember { mutableStateOf(false) }

        Text(
            text = "ESSence Login",
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Mock Input Fields (for visual representation)
        OutlinedTextField(
            value = emailUserInput,
            onValueChange = { emailUserInput = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Unspecified
            ),
        )

        OutlinedTextField(
            value = passwordUserInput,
            onValueChange = { passwordUserInput = it},
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 32.dp),

            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,

            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Outlined.Menu
                else Icons.Outlined.Star

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector  = image, contentDescription = "Toggle password visibility")
                }
            },

        )
        val context = LocalContext.current.applicationContext

        Button(
            onClick = {
                val user = SampleData.userMapByEmail[emailUserInput]

                if (user != null && user.password == passwordUserInput) {

                    SessionManager.saveSession(context, user)
                    onLoginSuccess(user.role)
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
                else {

                }
            },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Transparent background for navigation bar item
                contentColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Text("LOG IN", fontSize = 16.sp)
        }

        // Go to REGISTER
//        TextButton(onClick = { navController.navigate(Routes.REGISTRATION) }) {
//            Text("First time here? Sign up.", color = MaterialTheme.colorScheme.primary)
//        }
    }
}
