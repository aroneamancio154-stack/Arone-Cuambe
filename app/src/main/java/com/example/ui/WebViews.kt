package com.example.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.White

@Composable
fun WebMainView(appState: AppState) {
    var webActiveTab by remember { mutableStateOf("Dashboard") }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F7FF)) // Pastel Ice Blue browser canvas
    ) {
        
        // SIDEBAR NAVIGATION FOR DESKTOP SCREEN
        Column(
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
                .background(Color(0xFF0F172A)) // Deep Sleek Midnight Dark Sidebar
                .padding(20.dp)
        ) {
            // Enterprise Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = "Logo",
                    tint = Color(0xFF4EA8DE),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "AutoVenda",
                        color = White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Social Web v2.1",
                        color = Color(0xFF57C5B6),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Navigation Links
            val menuItems = listOf(
                Triple("Dashboard", "Painel Geral", Icons.Default.Dashboard),
                Triple("Queue", "Fila de Postagens", Icons.Default.ListAlt),
                Triple("Calendar", "Agenda & Calendário", Icons.Default.CalendarToday),
                Triple("Billing", "Assinatura Anual (1000MT)", Icons.Default.MonetizationOn),
                Triple("SystemSettings", "Configurações Gerais", Icons.Default.Settings),
                Triple("AdminBackstage", "Painel Admin Geral", Icons.Default.SupervisorAccount)
            )

            menuItems.forEach { item ->
                val isSelected = webActiveTab == item.first
                NavigationSidebarLink(
                    title = item.second,
                    icon = item.third,
                    isSelected = isSelected,
                    onClick = { webActiveTab = item.first }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // User Info Badge Footer
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF57C5B6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (appState.currentUser.email.isNotEmpty()) appState.currentUser.email.first().uppercase() else "A",
                            color = Color(0xFF0F172A),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Arone Amâncio",
                            color = White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = appState.currentUser.email,
                            color = Color.LightGray.copy(alpha = 0.6f),
                            fontSize = 9.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // MAIN CONTENT AREA IN THE BROWSER VIEWPORT
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(24.dp)
        ) {
            
            // WEB TOP PANEL
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = when (webActiveTab) {
                            "Dashboard" -> "Painel Bento de Negócios"
                            "Queue" -> "Fila Sincronizada de Anúncios"
                            "Calendar" -> "Planeamento Calendário"
                            "Billing" -> "Assinatura e Licença de Moçambique"
                            "SystemSettings" -> "Painel de Preferências Técnicas"
                            else -> "Administração Central / Backoffice"
                        },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "AutoVenda Social • Sincronização activada em tempo real com o seu smartphone",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Sincronized state indicator
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (appState.currentUser.isSubscribed) Color(0xFFDCFCE7) else Color(0xFFFEF9C3),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (appState.currentUser.isSubscribed) Color(0xFF16A34A) else Color(0xFFD97706))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (appState.currentUser.isSubscribed) "Licença Anual Ativa" else "Período Experimental Activo",
                                color = if (appState.currentUser.isSubscribed) Color(0xFF15803D) else Color(0xFFB45309),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // ACTIVE SCREEN STATE CONTENT
            Box(modifier = Modifier.weight(1f)) {
                when (webActiveTab) {
                    "Dashboard" -> {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            // BENTO GRID - LEFT BLOCK (Analytics and post speed)
                            Column(
                                modifier = Modifier
                                    .weight(2f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                
                                // BENTO GRID: MULTI SOCIAL ANALYTICS CARD
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = White),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                                ) {
                                    Column(modifier = Modifier.padding(20.dp)) {
                                        Text(
                                            text = "Alcance Multicanal e Engajamento Integrado",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))

                                        // SVG MOCK COLUMN CHARTS (Weekly reach levels)
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp),
                                            horizontalArrangement = Arrangement.SpaceAround,
                                            verticalAlignment = Alignment.Bottom
                                        ) {
                                            val statisticsData = listOf(4500, 7800, 11200, 9300, 14200, 16800, 15300)
                                            val days = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
                                            
                                            statisticsData.forEachIndexed { idx, value ->
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    val barHeightPct = value / 18000f
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(0.35f)
                                                            .fillMaxHeight(barHeightPct)
                                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 0.dp, bottomEnd = 0.dp))
                                                            .background(
                                                                if (idx == 5) Color(0xFF0077B6) // Highlight max
                                                                else Color(0xFF57C5B6)
                                                            )
                                                    )
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text(text = days[idx], fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Divider()
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "ℹ️ Destaque: Pico de visualização de anúncios registado aos sábados nas províncias de Maputo e Sofala.",
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                // BENTO GRID: FREQUENCY SPEED MANAGER (As requested in design html: "integrated interactive controllers + and - adjustments")
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = White),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                                ) {
                                    Column(modifier = Modifier.padding(20.dp)) {
                                        Text(
                                            text = "Configuração Geral: Frequência Diária",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = "Mínimo: 1 post/dia • Máximo: 100 posts/dia",
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B)
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            IconButton(
                                                onClick = { if (appState.postsPerDaySetting > 1) appState.postsPerDaySetting-- },
                                                modifier = Modifier.background(Color(0xFFE0F2FE), CircleShape)
                                            ) {
                                                Icon(Icons.Default.Remove, "Frequência menor", tint = Color(0xFF0077B6))
                                            }

                                            Spacer(modifier = Modifier.width(20.dp))

                                            Text(
                                                text = "${appState.postsPerDaySetting} Publicações Diárias Ativas",
                                                fontWeight = FontWeight.Black,
                                                fontSize = 20.sp,
                                                color = Color(0xFF1E6091)
                                            )

                                            Spacer(modifier = Modifier.width(20.dp))

                                            IconButton(
                                                onClick = { if (appState.postsPerDaySetting < 100) appState.postsPerDaySetting++ },
                                                modifier = Modifier.background(Color(0xFFE0F2FE), CircleShape)
                                            ) {
                                                Icon(Icons.Default.Add, "Frequência maior", tint = Color(0xFF0077B6))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Slider(
                                            value = appState.postsPerDaySetting.toFloat(),
                                            onValueChange = { appState.postsPerDaySetting = it.toInt() },
                                            valueRange = 1f..100f,
                                            colors = SliderDefaults.colors(
                                                thumbColor = Color(0xFF52B788),
                                                activeTrackColor = Color(0xFF0077B6)
                                            )
                                        )
                                    }
                                }
                            }

                            // BENTO GRID - RIGHT BLOCK (Connected platforms status, quick posts slot)
                            Column(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                
                                // BENTO GRID: APP PREMIUM PLAN STATUS
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Midnight black Premium
                                    border = BorderStroke(1.dp, Color(0xFF1E293B)),
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Text(text = "ASSINATURA ANUAL AUTORIZADA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4EA8DE))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "Valor: 1.000 MT / ano", color = White, fontSize = 20.sp, fontWeight = FontWeight.Black)

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = if (appState.currentUser.isSubscribed) "Sua empresa está ativa no plano anual. Divulgação máxima e ilimitada."
                                            else "Teste Gratuito: ${appState.trialDaysRemaining} dias restantes. Evite que as suas republicações parem!",
                                            color = Color.LightGray.copy(alpha = 0.8f),
                                            fontSize = 11.sp
                                        )

                                        Spacer(modifier = Modifier.height(14.dp))
                                        Button(
                                            onClick = { webActiveTab = "Billing" },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF57C5B6)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = if (appState.currentUser.isSubscribed) "Gerir Assinatura" else "Ativar Anuidade Já!",
                                                color = Color(0xFF0F172A),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                // BENTO GRID: CONNECTED ACCOUNTS OVERVIEW
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = White),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "Canais Sociais Conectados", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(10.dp))

                                        appState.socialAccounts.forEach { acc ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 5.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(10.dp)
                                                            .clip(CircleShape)
                                                            .background(if (acc.isConnected) Color(0xFF38B000) else Color.Red)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(text = acc.platform, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                                }
                                                Text(
                                                    text = if (acc.isConnected) acc.username else "Desconectado",
                                                    fontSize = 11.sp,
                                                    color = if (acc.isConnected) Color(0xFF0077B6) else Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "Queue" -> {
                        // DETAILED GRAPHIC QUEUE VIEW
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Campanhas Sincronizadas na Fila de Disparo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            if (appState.scheduledPosts.isEmpty()) {
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Fila vazia. Adicione produtos na versão do telemóvel para vê-los sincronizados aqui instantaneamente!",
                                        modifier = Modifier.padding(32.dp),
                                        textAlign = TextAlign.Center,
                                        color = Color.Gray
                                    )
                                }
                            } else {
                                appState.scheduledPosts.forEach { post ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        colors = CardDefaults.cardColors(containerColor = White),
                                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Campaign,
                                                contentDescription = "Post Icon",
                                                tint = Color(0xFF0077B6),
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = post.title,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp
                                                    )
                                                    Text(
                                                        text = "${post.price} MT",
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = Color(0xFF0077B6),
                                                        fontSize = 15.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = post.description,
                                                    fontSize = 12.sp,
                                                    color = Color.DarkGray
                                                )
                                                Spacer(modifier = Modifier.height(10.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "⏱ Agendamento Técnico: Às ${post.postingTime} • Repetição: ${post.repeatType.uppercase()} • Expiração: ${post.daysCount} dias",
                                                        fontSize = 11.sp,
                                                        color = Color(0xFF64748B)
                                                    )

                                                    Button(
                                                        onClick = { appState.deletePost(post.id) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.08f), contentColor = Color.Red),
                                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
                                                        modifier = Modifier.height(30.dp)
                                                    ) {
                                                        Text("Remover Post", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "Calendar" -> {
                        // COMPREHENSIVE CALENDAR BOARD (MAIO 2026)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Calendário Integrado de Divulgações Automáticas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Maio de 2026", fontWeight = FontWeight.Black, fontSize = 18.sp, color = Color(0xFF0F172A))
                                        Row {
                                            AssistChip(onClick = {}, label = { Text("Mensal") })
                                            Spacer(modifier = Modifier.width(6.dp))
                                            AssistChip(onClick = {}, label = { Text("Semanal") })
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Let's print out rows of the Calendar weeks
                                    val calendarHeaders = listOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo")
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                        calendarHeaders.forEach { h ->
                                            Text(
                                                text = h,
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Center,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = Color(0xFF0077B6)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.height(10.dp))

                                    val matrixWeeks = listOf(
                                        listOf("", "", "", "", "1\n• Sapatilha", "2\n• Vestido", "3\n• Vestido"),
                                        listOf("4", "5\n• Sapatilha", "6", "7", "8", "9", "10"),
                                        listOf("11", "12\n• Vestido", "13", "14", "15", "16", "17"),
                                        listOf("18\n• Anúncio", "19", "20", "21", "22\n• Sapatilha", "23", "24"),
                                        listOf("25", "26", "27\n• Hoje", "28\n• Vestido", "29", "30", "31")
                                    )

                                    matrixWeeks.forEach { week ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(60.dp)
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            week.forEach { cell ->
                                                val isPlanned = cell.contains("•")
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .fillMaxHeight()
                                                        .padding(horizontal = 4.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(
                                                            if (isPlanned) Color(0xFFE0F2FE)
                                                            else Color.Transparent
                                                        )
                                                        .border(
                                                            1.dp,
                                                            if (cell.contains("Hoje")) Color(0xFF38B000) else Color.Transparent,
                                                            RoundedCornerShape(8.dp)
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = cell,
                                                        fontSize = 10.sp,
                                                        fontWeight = if (isPlanned) FontWeight.Bold else FontWeight.Normal,
                                                        textAlign = TextAlign.Center,
                                                        color = if (isPlanned) Color(0xFF0077B6) else Color.DarkGray
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "Billing" -> {
                        // SECURE BILLING GATEWAY (1000 MT/ANO)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Cobrança Corporativa Moçambicana",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(0.6f),
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text("PLANO PROFISSIONAL ANUAL", fontWeight = FontWeight.Bold, color = Color(0xFF0077B6))
                                    Text("1.000 MT / ano", fontSize = 32.sp, fontWeight = FontWeight.Black, color = Color(0xFF0077B6))
                                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                                    Text(
                                        text = "A sua licença ativa expira todos os anos. Utilize o M-Pesa, e-Mola ou transferência bancária BIM para manter o postador automático activo nos seus canais de negócio.",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))

                                    if (appState.currentUser.isSubscribed) {
                                        Text("✅ O seu plano anual já se encontra ASSINADO E ATIVO!", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                                    } else if (appState.currentUser.paymentPending) {
                                        Text("⏳ Transação de 1000 MT pendente. Por favor, aceda ao Painel Admin para validar e libertar o seu login.", color = Color(0xFFD97706), fontWeight = FontWeight.Bold)
                                    } else {
                                        Button(
                                            onClick = {
                                                appState.submitSimulatedPayment("841234567", "mpesa")
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38B000)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Efectuar Pagamento Seguro (1000MT)", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "SystemSettings" -> {
                        // PREFERENCES STATE
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Ajustes Tecnológicos da Plataforma",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 14.dp)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(0.5f),
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Simulação de Redundância Técnica", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("O app funciona parcialmente em cache off-line salvando edições de posts para disparar automaticamente logo que houver cobertura.", fontSize = 11.sp, color = Color.Gray)

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Simular Conexão de Internet MZ", fontSize = 12.sp)
                                        Switch(
                                            checked = appState.activeInternet,
                                            onCheckedChange = { appState.activeInternet = it }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    "AdminBackstage" -> {
                        // ADMIN BACKSTAGE OVERRIDE
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Supervisão e Controlo Administrativo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = White),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text("Simular Expiração de Licenças e Depósitos", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Button(
                                            onClick = { appState.triggerTrialExpiry() },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Expirar Teste Gratuito de Arone", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { appState.resetTrial() },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38B000)),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Restaurar Teste (3 dias de graça)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text("Pedidos de Ativações Premium (Anual de 1000 MT)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Render users
                                    listOf(appState.currentUser).forEach { usr ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF8FAFC))
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(text = usr.email, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                Text(
                                                    text = "Metódo: ${usr.pendingPaymentMethod.uppercase()} | Telemóvel: ${usr.pendingPaymentNumber}",
                                                    fontSize = 11.sp,
                                                    color = Color.Gray
                                                )
                                            }

                                            Row {
                                                if (usr.paymentPending) {
                                                    Button(
                                                        onClick = { appState.adminApprovePayment(usr.email) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38B000))
                                                    ) {
                                                        Text("Aprovar", fontSize = 11.sp)
                                                    }
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Button(
                                                        onClick = { appState.adminRejectPayment(usr.email) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                                    ) {
                                                        Text("Recusar", fontSize = 11.sp)
                                                    }
                                                } else if (usr.isSubscribed) {
                                                    Text("Ativo Premium Plano Anual ✅", color = Color(0xFF16A34A), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                } else {
                                                    Text("Pendente Inscrição", color = Color.Gray, fontSize = 11.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationSidebarLink(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF1E293B) else Color.Transparent
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color(0xFF57C5B6) else Color.LightGray.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                color = if (isSelected) White else Color.LightGray.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
