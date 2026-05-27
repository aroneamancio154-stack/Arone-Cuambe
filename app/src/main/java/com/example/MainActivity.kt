package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppState
import com.example.ui.MobileMainView
import com.example.ui.WebMainView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize State Machine with context
        val appState = AppState.getInstance(this)

        setContent {
            MyApplicationTheme(darkTheme = appState.isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // EXQUISITE APPLET MODE SELECTOR (Switch between native app view & Computer dashboard)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0F172A)) // Sleek Admin Slate Background
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CompassCalibration,
                                    contentDescription = "Switcher logo",
                                    tint = Color(0xFF57C5B6),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "AutoVenda Social",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                            }
                            
                            // Visual Switcher Chips
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(30.dp))
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(30.dp))
                                    .padding(2.dp)
                            ) {
                                // Mobile Chip
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(26.dp))
                                        .background(
                                            if (appState.viewMode == "Celular") Color(0xFF0077B6)
                                            else Color.Transparent
                                        )
                                        .clickable { appState.viewMode = "Celular" }
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneAndroid,
                                        contentDescription = "Celular Mode",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "📱 Celular",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                // Web Computer Chip
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(26.dp))
                                        .background(
                                            if (appState.viewMode == "Web") Color(0xFF0077B6)
                                            else Color.Transparent
                                        )
                                        .clickable { appState.viewMode = "Web" }
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Computer,
                                        contentDescription = "Web Mode",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "💻 Web / Computador",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // SWITCH ACTIVE BODY
                        Box(modifier = Modifier.weight(1f)) {
                            if (appState.viewMode == "Celular") {
                                // RENDER IMMERSIVE MOBILE PHONE SHELL SIMULATOR
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF1E293B)) // Matte slate environment backdrop
                                        .padding(vertical = 14.dp, horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(420.dp) // Perfect mobile screen proportions inside emulator stream
                                            .border(8.dp, Color(0xFF0F172A), RoundedCornerShape(28.dp)) // Phone bezel border
                                            .clip(RoundedCornerShape(22.dp))
                                            .background(MaterialTheme.colorScheme.background)
                                    ) {
                                        MobileMainView(appState = appState)
                                    }
                                }
                            } else {
                                // Full Web screen layout
                                WebMainView(appState = appState)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

