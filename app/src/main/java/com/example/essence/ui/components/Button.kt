package com.example.essence.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Plain Button
@Composable
fun FlatButton(
    event:() -> Unit,
    text: String,
    modifier: Modifier
) {
    Button(
        onClick = event,
        modifier = modifier,
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(text)
    }
}

// Button with Icon on Top
@Composable
fun TopIconFlatButton(
    event: () -> Unit,
    text: String,
    modifier: Modifier,
    iconSource: Int? = null
) {
    Button(
        onClick = event,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier //.border(1.dp, Color.Yellow)
                                .fillMaxWidth(),
        ){
            if (iconSource != null) {
                Icon(
                    painter = painterResource(id = iconSource),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
//                            .border(1.dp, Color.Red)
                            ,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text,
                fontSize = 12.sp,
                maxLines = 1,
//                modifier = modifier.border(1.dp, Color.Blue),
                overflow = TextOverflow.Clip,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary

            )
        }
    }
}

@Composable
fun IconOnlyFlatButton(
    event: () -> Unit,
    modifier: Modifier,
    iconSource: Any? = null
) {
    Button(
        onClick = event,
        modifier = modifier,
        shape = RoundedCornerShape(0.dp)
    ) {
        if (iconSource != null) {
            if (iconSource is Int)
                Icon(
                    painter = painterResource(id = iconSource),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            if (iconSource is ImageVector)
                Image(
                    imageVector = iconSource,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
        }
    }
}

@Composable
fun BadgeIconOnlyFlatButton(
    event: () -> Unit,
    modifier: Modifier = Modifier,
    iconSource: Any? = null,
    number: Int = 0
) {
    Button(
        onClick = event,
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        BadgedBox(
            badge = {
                if (number > 0) {
                    Badge {
                        Text(
                            text = if (number > 99) "99+" else number.toString(),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        ) {
            when (iconSource) {
                is Int -> Icon(
                    painter = painterResource(id = iconSource),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                is ImageVector -> Icon(
                    imageVector = iconSource,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
