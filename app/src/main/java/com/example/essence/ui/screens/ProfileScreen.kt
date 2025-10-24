package com.example.essence.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.essence.data.local.SessionManager
import com.example.essence.data.model.User
import com.example.essence.data.sample.SampleData
import com.example.essence.ui.components.profile.ProfileCard



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileContent() {
    Column(
        modifier = screenModifier()
    ) {
        val context = LocalContext.current.applicationContext
        val user: User = SampleData.userMapById[SessionManager.getUserId(context)] as User
        ProfileCard(user)
    }
}