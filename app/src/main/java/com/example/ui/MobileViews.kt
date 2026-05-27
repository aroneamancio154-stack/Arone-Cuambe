package com.example.ui

import android.graphics.Bitmap
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MobileMainView(appState: AppState) {
    var currentScreen by remember { mutableStateOf("Splash") }
    val scope = rememberCoroutineScope()

    // Temp variables for creating a new post
    var newPostTitle by remember { mutableStateOf("") }
    var newPostDesc by remember { mutableStateOf("") }
    var newPostPrice by remember { mutableStateOf("") }
    var newPostPhone by remember { mutableStateOf("841234567") }
    var newPostTime by remember { mutableStateOf("08:00") }
    var newPostRepeat by remember { mutableStateOf("diario") }
    var newPostDays by remember { mutableStateOf("30") }
    var selectedPhotoFilter by remember { mutableStateOf("Normal") }
    var selectedPhotoCrop by remember { mutableStateOf(1.0f) }
    var isPhotoUploaded by remember { mutableStateOf(false) }
    var selectedNetworksToPost = remember { mutableStateListOf("Facebook") }

    // Register temp data
    var regEmail by remember { mutableStateOf("") }
    var regPass by remember { mutableStateOf("") }
    var regName by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var enteredPin by remember { mutableStateOf("") }

    // Login temp data
    var loginEmail by remember { mutableStateOf("aroneamancio154@gmail.com") }
    var loginPass by remember { mutableStateOf("123456") }

    // Ads promotion temp data
    var selectedPostForAds by remember { mutableStateOf<PostItem?>(null) }
    var adsBudgetStr by remember { mutableStateOf("1500") }
    var adsAudience by remember { mutableStateOf("Maputo Cidade") }
    var adsDuration by remember { mutableStateOf(7) }

    // Payment/Subscription modal options
    var paymentMethodSelected by remember { mutableStateOf("mpesa") }
    var mpesaNumberSelected by remember { mutableStateOf("841234567") }

    // Auto-launch Splash timeout
    LaunchedEffect(currentScreen) {
        if (currentScreen == "Splash") {
            delay(1800)
            currentScreen = "Login"
        }
    }

    // INTERCEPT: If expired, FORCE the blocking premium signature paywall (except if User is in Splash or Login or AdminPanel)
    val needsBlock = appState.isBannedDueToSubscriptionExp
    val displayScreen = if (needsBlock && currentScreen != "Splash" && currentScreen != "Login" && currentScreen != "Cadastro" && currentScreen != "VerifyPin" && currentScreen != "AdminPanel") {
        "Assinatura"
    } else {
        currentScreen
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("mobile_app_container"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // SIMULATED PHONE HEADER (Signal, Wifi, Battery, Time)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                    .padding(horizontal = 14.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Connection and carrier status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (appState.activeInternet) Icons.Default.Wifi else Icons.Default.WifiOff,
                        contentDescription = "Wifi",
                        modifier = Modifier.size(14.dp),
                        tint = if (appState.activeInternet) MaterialTheme.colorScheme.primary else Color.Red
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "VodaCom MZ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                // Middle title indicator
                Text(
                    text = "AutoVenda Celular",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Right side: Local Time and System battery
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "12:02 CAT", // Moçambique timezone GMT+2
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = "Battery",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when (displayScreen) {
                    "Splash" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = "Logo",
                                    tint = White,
                                    modifier = Modifier.size(90.dp)
                                )
                                Spacer(modifier = Modifier.height(18.dp))
                                Text(
                                    text = "AutoVenda Social",
                                    color = White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "Postador e Republicador Inteligente",
                                    color = White.copy(alpha = 0.8f),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary, strokeWidth = 3.dp)
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "1.000 MT / ano",
                                    color = White.copy(alpha = 0.7f),
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                )
                            }
                        }
                    }

                    "Login" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Lock",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(64.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Aceder ao AutoVenda",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Text(
                                text = "Simplifique a divulgação dos seus produtos no Facebook, Instagram, X e WhatsApp",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = loginEmail,
                                onValueChange = { loginEmail = it },
                                label = { Text("Seu E-mail Comercial") },
                                leadingIcon = { Icon(Icons.Default.Email, "Email") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_email"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = loginPass,
                                onValueChange = { loginPass = it },
                                label = { Text("Palavra-passe") },
                                leadingIcon = { Icon(Icons.Default.Lock, "Lock") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_pass"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (loginEmail.isNotEmpty() && loginPass.length >= 6) {
                                        appState.currentUser = appState.currentUser.copy(
                                            email = loginEmail,
                                            verified = true
                                        )
                                        appState.logEvent("SESSÃO: Login efetuado com sucesso por $loginEmail")
                                        currentScreen = "Dashboard"
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("login_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Login, "Login")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Entrar de Forma Segura", fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            TextButton(
                                onClick = { currentScreen = "Cadastro" },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Não tem uma conta? Registe-se grátis")
                            }

                            TextButton(
                                onClick = { currentScreen = "Recovery" },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Esqueceu a palavra-passe? Recuperar")
                            }
                        }
                    }

                    "Cadastro" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Criar Conta AutoVenda",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                text = "Preencha as informações para registar a sua empresa ou perfil comercial.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            OutlinedTextField(
                                value = regName,
                                onValueChange = { regName = it },
                                label = { Text("Nome Comercial / Marca") },
                                leadingIcon = { Icon(Icons.Default.Store, "Store") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = regEmail,
                                onValueChange = { regEmail = it },
                                label = { Text("Endereço de E-mail") },
                                leadingIcon = { Icon(Icons.Default.Email, "Email") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = regPhone,
                                onValueChange = { regPhone = it },
                                label = { Text("Telemóvel (WhatsApp - ex: 84xxxxxxx)") },
                                leadingIcon = { Icon(Icons.Default.Phone, "Phone") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = regPass,
                                onValueChange = { regPass = it },
                                label = { Text("Escolha uma Palavra-passe") },
                                leadingIcon = { Icon(Icons.Default.Password, "Lock") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (regEmail.isNotEmpty() && regPass.isNotEmpty()) {
                                        // Send validation code simulation
                                        appState.activePinCode = "4839" // Simple mocked code
                                        appState.logEvent("SEGURANÇA: Código de verificação PIN (4839) enviado para $regEmail")
                                        currentScreen = "VerifyPin"
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Solicitar Código por E-mail", fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            TextButton(
                                onClick = { currentScreen = "Login" },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Já possui conta? Faça Login")
                            }
                        }
                    }

                    "VerifyPin" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.MarkEmailRead,
                                contentDescription = "Pin Check",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Confirme o seu E-mail",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Enviámos um PIN de 4 dígitos para o seu email corporativo. Introduza abaixo para ativar a conta:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = enteredPin,
                                onValueChange = { enteredPin = it },
                                label = { Text("Código de Segurança (4 dígitos)") },
                                textStyle = LocalTextStyle.current.copy(
                                    textAlign = TextAlign.Center,
                                    letterSpacing = 8.sp,
                                    fontSize = 18.sp
                                ),
                                placeholder = { Text("4839", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                                modifier = Modifier.width(220.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (enteredPin == "4839" || enteredPin == "1234") {
                                        appState.currentUser = appState.currentUser.copy(
                                            email = regEmail.ifEmpty { "aroneamancio154@gmail.com" },
                                            verified = true,
                                            hasTrialStarted = true,
                                            trialStartedAt = System.currentTimeMillis() // 3 days starts now!
                                        )
                                        appState.logEvent("SEGURANÇA: E-mail verificado com sucesso. Teste gratuito ativo.")
                                        currentScreen = "ConectarRedes"
                                    } else {
                                        appState.logEvent("Aviso: PIN incorreto introduzido.")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Confirmar e Activar Registro", fontWeight = FontWeight.Bold)
                            }

                            TextButton(
                                onClick = { appState.logEvent("SEGURANÇA: Código reenviado para o email.") }
                            ) {
                                Text("Reenviar código PIN")
                            }
                        }
                    }

                    "Recovery" -> {
                        var recEmail by remember { mutableStateOf("") }
                        var statusMsg by remember { mutableStateOf("") }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Exclusivo: Recuperar Acesso",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Insira seu email para enviarmos instruções seguras para redefinir o seu PIN secreto.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = recEmail,
                                onValueChange = { recEmail = it },
                                label = { Text("E-mail registado") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (statusMsg.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = statusMsg, color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (recEmail.contains("@")) {
                                        statusMsg = "Sucesso! Link de recuperação enviado para $recEmail"
                                        appState.logEvent("SEGURANÇA: E-mail de recuperação de palavra-passe solicitado por $recEmail")
                                    } else {
                                        statusMsg = "Por favor, insira um e-mail válido."
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Enviar Link de Recuperação", fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            TextButton(
                                onClick = { currentScreen = "Login" },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Voltar para o Login")
                            }
                        }
                    }

                    "ConectarRedes" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Conectar Redes Sociais",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            Text(
                                text = "Para publicar automaticamente, selecione uma ou mais contas. Sincronize as suas APIs com 1 clique.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            // Network list matching requirements (múltiplas contas ao mesmo tempo)
                            appState.socialAccounts.forEachIndexed { index, account ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (account.isConnected) MaterialTheme.colorScheme.primary.copy(alpha = 0.04f)
                                        else MaterialTheme.colorScheme.surface
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        if (account.isConnected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(account.avatarColorHex)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = when (account.platform) {
                                                        "Facebook" -> Icons.Default.Share
                                                        "Instagram" -> Icons.Default.PhotoCamera
                                                        "WhatsApp Business" -> Icons.Default.Sms
                                                        else -> Icons.Default.AlternateEmail
                                                    },
                                                    contentDescription = account.platform,
                                                    tint = White,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(text = account.platform, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                                Text(
                                                    text = if (account.isConnected) account.username else "Disponível para conexão",
                                                    fontSize = 11.sp,
                                                    color = if (account.isConnected) MaterialTheme.colorScheme.primary
                                                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                                )
                                            }
                                        }

                                        Switch(
                                            checked = account.isConnected,
                                            onCheckedChange = { isChecked ->
                                                appState.socialAccounts[index] = account.copy(isConnected = isChecked)
                                                appState.logEvent(
                                                    if (isChecked) "API: Canal ${account.platform} conectado com sucesso."
                                                    else "API: Canal ${account.platform} desconectado."
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { currentScreen = "Dashboard" },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Aceder ao Painel (Dashboard)", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    "Dashboard" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Header Banner info
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(horizontal = 20.dp, vertical = 22.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Olá, Empreendedor MZ! 🇲🇿",
                                        color = White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Campanhas ativas de republicação e anúncios",
                                        color = White.copy(alpha = 0.8f),
                                        fontSize = 12.sp
                                    )

                                    // Trial Indicator Banner
                                    val trialDays = appState.trialDaysRemaining
                                    val isSubscribed = appState.currentUser.isSubscribed
                                    
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSubscribed) MaterialTheme.colorScheme.secondary
                                            else MaterialTheme.colorScheme.tertiary
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = if (isSubscribed) Icons.Default.VerifiedUser else Icons.Default.Timer,
                                                    contentDescription = "Subscription",
                                                    tint = White,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = if (isSubscribed) "Assinatura Anual Ativa" else "Teste Gratuito: $trialDays dias restantes",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = White
                                                )
                                            }
                                            TextButton(
                                                onClick = { currentScreen = "Assinatura" },
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                                            ) {
                                                Text(
                                                    text = if (isSubscribed) "Ver Detalhes" else "Ativar Anual",
                                                    color = White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Stats Grid Mock for Mobile design
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Card(modifier = Modifier.weight(1f)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Icon(Icons.Default.CloudQueue, "Active", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Produtos Ativos", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                        Text("${appState.scheduledPosts.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Card(modifier = Modifier.weight(1f)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Icon(Icons.Default.Group, "Connected", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Redes Ativas", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                        Text("${appState.socialAccounts.filter { it.isConnected }.size}/4", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Card(modifier = Modifier.weight(1f)) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Icon(Icons.Default.TrendingUp, "Views", tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Alcance Local", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                        Text("14.2K", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Active Posts Scroll view
                            Text(
                                text = "Próximos Envios Agendados",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )

                            if (appState.scheduledPosts.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Nenhum produto programado. Clique em '+' no rodapé para criar sua primeira campanha!",
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                    )
                                }
                            } else {
                                appState.scheduledPosts.forEach { post ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 6.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(text = post.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                                    Text(text = "${post.price} MT", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                }
                                                Row {
                                                    IconButton(onClick = {
                                                        selectedPostForAds = post
                                                        currentScreen = "PromoAds"
                                                    }) {
                                                        Icon(Icons.Default.Campaign, "Ads Boost", tint = if (post.hasAdsCampaign) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                                    }
                                                    IconButton(onClick = { appState.deletePost(post.id) }) {
                                                        Icon(Icons.Default.Delete, "Delete", tint = Color.Red.copy(alpha = 0.7f))
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = post.description,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                                maxLines = 2
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))
                                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.HourglassEmpty, "Time", modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Envio: Às ${post.postingTime} • ${post.repeatType.uppercase()}",
                                                        fontSize = 10.sp,
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                    )
                                                }

                                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    post.connectedNetworks.forEach { network ->
                                                        Box(
                                                            modifier = Modifier
                                                                .background(
                                                                    color = when (network) {
                                                                        "Facebook" -> Color(0xFF1877F2)
                                                                        "Instagram" -> Color(0xFFE1306C)
                                                                        "WhatsApp Business" -> Color(0xFF25D366)
                                                                        else -> Color(0xFF1DA1F2)
                                                                    },
                                                                    shape = RoundedCornerShape(4.dp)
                                                                )
                                                                .padding(horizontal = 5.dp, vertical = 2.dp)
                                                        ) {
                                                            Text(text = network, color = White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                }
                                            }

                                            if (post.hasAdsCampaign) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(Icons.Default.CheckCircle, "Ads On", tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(14.dp))
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = "Anúncio Activo: Orçamento ${post.adsBudget} MT p/ ${post.adsAudience}",
                                                            fontSize = 10.sp,
                                                            color = MaterialTheme.colorScheme.onBackground,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Logs scroll
                            Text(
                                text = "Historial de Atividade Local",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    appState.eventsLog.take(4).forEach { event ->
                                        Text(
                                            text = "• $event",
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }

                    "NovaPublicacao" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Nova Campanha de Venda",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Carregue fotos, configure preço, e use a Inteligência Artificial Gemini para escrever o seu texto de alta atracção.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            // UPLOAD IMAGES SLOT with Filter customization
                            Text(text = "Fotos do Produto (Suba até 5 imagens)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Upload trigger buttons
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { isPhotoUploaded = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.PhotoLibrary, "Gallery", tint = MaterialTheme.colorScheme.primary)
                                        Text("Galeria", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .background(
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { isPhotoUploaded = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.PhotoCamera, "Camera", tint = MaterialTheme.colorScheme.secondary)
                                        Text("Câmara", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // Photo Preview box with filters applied
                                if (isPhotoUploaded) {
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.LightGray)
                                    ) {
                                        // Simple Simulated product Image with matrix color simulation filter
                                        Image(
                                            imageVector = Icons.Default.Inventory2,
                                            contentDescription = "Uploaded Specimen",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(14.dp),
                                            colorFilter = ColorFilter.colorMatrix(
                                                ColorMatrix().apply {
                                                    when (selectedPhotoFilter) {
                                                        "PB (P&B)" -> setToSaturation(0f)
                                                        "Vintage" -> {
                                                            setToSaturation(0.6f)
                                                            // Give sepia tint
                                                        }
                                                        "Brilhante" -> {
                                                            // Simulate high exposure
                                                        }
                                                        else -> reset()
                                                    }
                                                }
                                            )
                                        )
                                        
                                        // Delete badge
                                        IconButton(
                                            onClick = { isPhotoUploaded = false },
                                            modifier = Modifier
                                                .size(24.dp)
                                                .align(Alignment.TopEnd)
                                                .background(Color.Red, CircleShape)
                                        ) {
                                            Icon(Icons.Default.Close, "Clear", tint = White, modifier = Modifier.size(12.dp))
                                        }

                                        // Size compression simulated badge
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomCenter)
                                                .background(Color.Black.copy(alpha = 0.7f))
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "Compressed (145 KB)",
                                                fontSize = 7.sp,
                                                color = White,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }

                            if (isPhotoUploaded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Filtro Cromático Rápido", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    listOf("Normal", "Vintage", "PB (P&B)", "Brilhante").forEach { filter ->
                                        AssistChip(
                                            onClick = { selectedPhotoFilter = filter },
                                            label = { Text(filter, fontSize = 9.sp) },
                                            colors = AssistChipDefaults.assistChipColors(
                                                containerColor = if (selectedPhotoFilter == filter) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                                else Color.Transparent
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = newPostTitle,
                                onValueChange = { newPostTitle = it },
                                label = { Text("Nome do Produto (ex: Calçado Social Puma)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // GEMINI WRITER PANEL
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.AutoAwesome, "AI", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Legendas Inteligentes Gemini", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                        }

                                        Button(
                                            onClick = {
                                                if (newPostTitle.isEmpty()) {
                                                    newPostDesc = "⚠️ Por favor, preencha primeiro o Nome do Produto para servir de base à IA."
                                                } else {
                                                    val pr = newPostPrice.toDoubleOrNull() ?: 1500.0
                                                    appState.runGeminiCaptionGeneration(newPostTitle, pr, newPostPhone) { result ->
                                                        newPostDesc = result
                                                    }
                                                }
                                            },
                                            enabled = !appState.isGeneratingCaption,
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                                            modifier = Modifier.height(30.dp)
                                        ) {
                                            Text(
                                                text = if (appState.isGeneratingCaption) "A Gerar..." else "Gerar Legenda IA",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    if (appState.isGeneratingCaption) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newPostDesc,
                                onValueChange = { newPostDesc = it },
                                label = { Text("Descrição / Legenda do Post") },
                                minLines = 4,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = newPostPrice,
                                    onValueChange = { newPostPrice = it },
                                    label = { Text("Preço (MT)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = newPostPhone,
                                    onValueChange = { newPostPhone = it },
                                    label = { Text("WhatsApp (Contacto)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.weight(1.3f)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // REPUBLICATION FREQUENCY PREFERENCE (horas, semanas, meses, 1 ano inteiro)
                            Text(text = "Definição de Republicação Automática", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val repOptions = listOf(
                                    Triple("diario", "Diário", "Útil diário"),
                                    Triple("semanal", "Semanal", "Por semana"),
                                    Triple("mensal", "Mensal", "Por mês"),
                                    Triple("1ano", "1 Ano Inteiro", "Total anual")
                                )
                                repOptions.forEach { opt ->
                                    FilterChip(
                                        selected = newPostRepeat == opt.first,
                                        onClick = { newPostRepeat = opt.first },
                                        label = { Text(opt.second, fontSize = 10.sp) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = newPostTime,
                                    onValueChange = { newPostTime = it },
                                    label = { Text("Hora Automática (HH:mm)") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = newPostDays,
                                    onValueChange = { newPostDays = it },
                                    label = { Text("Durante x dias") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // MULTI REDES SELECTOR
                            Text(text = "Canais de Destino Associados", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val platforms = listOf("Facebook", "Instagram", "WhatsApp Business", "X (Twitter)")
                                platforms.forEach { plat ->
                                    val isSelected = selectedNetworksToPost.contains(plat)
                                    val accountConnected = appState.socialAccounts.find { it.platform == plat }?.isConnected ?: false
                                    
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            if (isSelected) {
                                                selectedNetworksToPost.remove(plat)
                                            } else {
                                                selectedNetworksToPost.add(plat)
                                            }
                                        },
                                        enabled = accountConnected, // Must connect first to select!
                                        label = { Text(plat, fontSize = 9.sp) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (newPostTitle.isNotEmpty()) {
                                        val pDouble = newPostPrice.toDoubleOrNull() ?: 0.0
                                        val daysInt = newPostDays.toIntOrNull() ?: 30
                                        
                                        val post = PostItem(
                                            title = newPostTitle,
                                            description = newPostDesc,
                                            price = pDouble,
                                            phone = newPostPhone,
                                            whatsappLink = "https://wa.me/258${newPostPhone.replace(" ", "")}",
                                            postingTime = newPostTime,
                                            repeatType = newPostRepeat,
                                            daysCount = daysInt,
                                            postsPerDay = appState.postsPerDaySetting,
                                            connectedNetworks = selectedNetworksToPost.toList()
                                        )
                                        appState.addPost(post)
                                        
                                        // Reset variables
                                        newPostTitle = ""
                                        newPostDesc = ""
                                        newPostPrice = ""
                                        isPhotoUploaded = false
                                        
                                        currentScreen = "Dashboard"
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Icon(Icons.Default.CalendarMonth, "Schedule")
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Activar Programação Automática", fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    "Calendar" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Agenda de Republicações",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Maio de 2026",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Text(
                                text = "Acompanhe visualmente a grade cronológica dos disparos automáticos de campanhas nas redes configuradas.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // CALENDAR MATRIX DRAWN VIA JETPACK COMPOSE GRID
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    // Week headers
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        val days = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sáb", "Dom")
                                        days.forEach { d ->
                                            Text(
                                                text = d,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Divider()
                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Matrix weeks
                                    val weeks = listOf(
                                        listOf("", "", "", "", 1, 2, 3),
                                        listOf(4, 5, 6, 7, 8, 9, 10),
                                        listOf(11, 12, 13, 14, 15, 16, 17),
                                        listOf(18, 19, 20, 21, 22, 23, 24),
                                        listOf(25, 26, 27, 28, 29, 30, 31)
                                    )

                                    weeks.forEach { week ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                        ) {
                                            week.forEach { dayNum ->
                                                val isPlannedDay = dayNum in listOf(5, 12, 18, 27, 28) // Mark some interactive planned post days
                                                Box(
                                                    modifier = Modifier
                                                        .size(36.dp)
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(
                                                            if (isPlannedDay) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                                            else Color.Transparent
                                                        )
                                                        .border(
                                                            1.dp,
                                                            if (dayNum == 27) MaterialTheme.colorScheme.secondary // Highlight current local day
                                                            else Color.Transparent,
                                                            RoundedCornerShape(6.dp)
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                        Text(
                                                            text = dayNum.toString(),
                                                            fontSize = 11.sp,
                                                            fontWeight = if (isPlannedDay) FontWeight.ExtraBold else FontWeight.Normal,
                                                            color = if (dayNum == 27) MaterialTheme.colorScheme.secondary
                                                            else MaterialTheme.colorScheme.onBackground
                                                        )
                                                        if (isPlannedDay) {
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(4.dp)
                                                                    .clip(CircleShape)
                                                                    .background(MaterialTheme.colorScheme.primary)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Lançamentos e Promoções Planeadas",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            appState.scheduledPosts.forEach { post ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.secondary)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(text = post.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text(
                                                text = "Programado para todos os dias às ${post.postingTime} nos canais selecionados.",
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    "PromoAds" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "Promover Publicação (Ads)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Conecte diretamente as campanhas do seu negócio ao Facebook Ads e Instagram Ads para garantir tráfego pago ultra-segmentado em Moçambique.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Text(text = "Selecione a Publicação Activa:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            
                            if (appState.scheduledPosts.isEmpty()) {
                                Text("Nenhuma publicação elegível para promover. Crie uma nova campanha primeiro.", color = Color.Red, fontSize = 11.sp)
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                                        .border(1.dp, MaterialTheme.colorScheme.outline)
                                        .padding(10.dp)
                                ) {
                                    // Use first post or selected post
                                    val activePost = selectedPostForAds ?: appState.scheduledPosts.first()
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = activePost.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text(text = "${activePost.price} MT", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                        }
                                        Icon(Icons.Default.CheckCircle, "Active", tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(text = "Orçamento Diário Recomendado (MT)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = adsBudgetStr,
                                onValueChange = { adsBudgetStr = it },
                                label = { Text("Valor Total do Orçamento (Meticais)") },
                                leadingIcon = { Text("💰 ", modifier = Modifier.padding(start = 6.dp)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(text = "Duração de Anúncios Activos (Dias)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(3, 7, 14, 30).forEach { days ->
                                    FilterChip(
                                        selected = adsDuration == days,
                                        onClick = { adsDuration = days },
                                        label = { Text("$days dias", fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(text = "Público-Alvo Segmentado (Província)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val locations = listOf("Maputo Cidade", "Matola", "Beira (Sofala)", "Nampula")
                                locations.forEach { loc ->
                                    FilterChip(
                                        selected = adsAudience == loc,
                                        onClick = { adsAudience = loc },
                                        label = { Text(loc, fontSize = 9.sp) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    val activePost = selectedPostForAds ?: if (appState.scheduledPosts.isNotEmpty()) appState.scheduledPosts.first() else null
                                    if (activePost != null) {
                                        val idx = appState.scheduledPosts.indexOfFirst { it.id == activePost.id }
                                        if (idx != -1) {
                                            appState.scheduledPosts[idx] = activePost.copy(
                                                hasAdsCampaign = true,
                                                adsBudget = adsBudgetStr.toDoubleOrNull() ?: 1500.0,
                                                adsAudience = adsAudience,
                                                adsDurationDays = adsDuration
                                            )
                                            appState.logEvent("ADS ADS: Campanha paga integrada lançada com sucesso no Facebook/Instagram Ads para '${activePost.title}'")
                                        }
                                        currentScreen = "Dashboard"
                                    }
                                },
                                enabled = appState.scheduledPosts.isNotEmpty(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Lançar Promoção Paga Imediata", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    "Configuracoes" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Ajustes Globais",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            // CONTROL OF POSTS PER DAY - SLIDER + ADD/MINUS CHANNELS (as per requirements: "Quantidade de posts por dia mínimo 1, máximo 100")
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "Controle de Disparos Diários",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Controle a frequência máxima de republicação para evitar política de spam das redes.",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                if (appState.postsPerDaySetting > 1) {
                                                    appState.postsPerDaySetting--
                                                }
                                            },
                                            modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                                        ) {
                                            Icon(Icons.Default.Remove, "Less", tint = MaterialTheme.colorScheme.primary)
                                        }

                                        Text(
                                            text = "${appState.postsPerDaySetting} Publicações / Dia",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )

                                        IconButton(
                                            onClick = {
                                                if (appState.postsPerDaySetting < 100) {
                                                    appState.postsPerDaySetting++
                                                }
                                            },
                                            modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                                        ) {
                                            Icon(Icons.Default.Add, "More", tint = MaterialTheme.colorScheme.primary)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Slider(
                                        value = appState.postsPerDaySetting.toFloat(),
                                        onValueChange = { appState.postsPerDaySetting = it.toInt() },
                                        valueRange = 1f..100f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = MaterialTheme.colorScheme.secondary,
                                            activeTrackColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Theme toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.DarkMode, "Dark")
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Visualização em Tema Escuro", fontSize = 13.sp)
                                }
                                Switch(
                                    checked = appState.isDarkMode,
                                    onCheckedChange = { appState.isDarkMode = it }
                                )
                            }

                            Divider(modifier = Modifier.padding(vertical = 10.dp))

                            // Language toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Language, "Lang")
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Idioma de Interface (Moçambique)", fontSize = 13.sp)
                                }
                                Row {
                                    Button(
                                        onClick = { appState.currentLanguage = "PT" },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (appState.currentLanguage == "PT") MaterialTheme.colorScheme.primary else Color.LightGray
                                        ),
                                        modifier = Modifier.height(30.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp)
                                    ) {
                                        Text("PT", fontSize = 10.sp)
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Button(
                                        onClick = { appState.currentLanguage = "EN" },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (appState.currentLanguage == "EN") MaterialTheme.colorScheme.primary else Color.LightGray
                                        ),
                                        modifier = Modifier.height(30.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp)
                                    ) {
                                        Text("EN", fontSize = 10.sp)
                                    }
                                }
                            }

                            Divider(modifier = Modifier.padding(vertical = 10.dp))

                            // Network Status check
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CellWifi, "Offline State")
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Sincronização Offline Ativa", fontSize = 13.sp)
                                }
                                Switch(
                                    checked = appState.activeInternet,
                                    onCheckedChange = { appState.activeInternet = it }
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { currentScreen = "AdminPanel" },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.SupervisorAccount, "Admin")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Aceder Backoffice Geral (Admin)", fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    appState.currentUser = UserSession() // Logout
                                    currentScreen = "Login"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ExitToApp, "Exit")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Terminar Sessão", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    "Assinatura" -> {
                        // PAYWALL BLOCK - as requested in instructions: "bloquear acesso ao app ate pagamento ser realizado"
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.LockClock,
                                contentDescription = "Locked Premium",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(72.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Assinatura Requerida!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            // Highlight price 1.000 MT / ano (Anual as per checkpoint edit)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("SEU PLANO ANUAL PROFISSIONAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Text("1.000 MT / ano", fontSize = 28.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                                    Text("Teste gratuito de 3 dias expirado ou inativo.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                }
                            }

                            Text(
                                text = "Para libertar o republicador automático multicanal e as legendas Gemini IA, efetue o pagamento seguro através das opções móveis locais moçambicanas abaixo:",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val methods = listOf(
                                    Pair("mpesa", "M-Pesa 🇲🇿"),
                                    Pair("emola", "e-Mola 🇲🇿"),
                                    Pair("bim", "Banco BIM")
                                )
                                methods.forEach { m ->
                                    FilterChip(
                                        selected = paymentMethodSelected == m.first,
                                        onClick = { paymentMethodSelected = m.first },
                                        label = { Text(m.second, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = mpesaNumberSelected,
                                onValueChange = { mpesaNumberSelected = it },
                                label = { Text("Número de Telemóvel (${paymentMethodSelected.uppercase()})") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (appState.currentUser.paymentPending) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))) {
                                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("Pagamento enviado na rede ${appState.currentUser.pendingPaymentMethod.uppercase()}! Confirme via USSD no seu telemóvel.", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Flow: "Efetuar Pagamento" sem exibir directamente os números
                            Button(
                                onClick = {
                                    if (mpesaNumberSelected.isNotEmpty()) {
                                        appState.submitSimulatedPayment(mpesaNumberSelected, paymentMethodSelected)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lock, "Secure")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Efetuar Pagamento Seguro", fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Test indicator link to bypass via admin dashboard
                            TextButton(
                                onClick = { currentScreen = "AdminPanel" }
                            ) {
                                Text("Aviso de Testadores: Aceder ao Admin para aprovar pagamento", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    "AdminPanel" -> {
                        // ADMIN BOARD VIEW TO OVERRIDE LIMITS/TRIAL BYPASS
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Backoffice do Administrador",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Button(
                                    onClick = { currentScreen = "Configuracoes" },
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Sair", fontSize = 10.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.08f))) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Área Técnica Exclusiva:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Use estes controlos rápidos para simular o comportamento de licenças e expiração sem ter de esperar 3 dias de facto ou fazer pagamentos reais.", fontSize = 10.sp)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { appState.triggerTrialExpiry() },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f)),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Expirar Teste", fontSize = 10.sp)
                                        }

                                        Button(
                                            onClick = { appState.resetTrial() },
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Resetar 3 Dias", fontSize = 10.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Utilizadores e Pedidos de Assinaturas (1.000 MT)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Item current user check
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = "CONTA ACTIVA: ${appState.currentUser.email}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(
                                        text = "Status: " + when {
                                            appState.currentUser.isSubscribed -> "✅ Premium Assinado"
                                            appState.currentUser.paymentPending -> "⏳ Pagamento Pendente (${appState.currentUser.pendingPaymentMethod.uppercase()} ${appState.currentUser.pendingPaymentNumber})"
                                            else -> "Teste Ativo (${appState.trialDaysRemaining} dias restam)"
                                        },
                                        fontSize = 11.sp
                                    )

                                    if (appState.currentUser.paymentPending) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = { appState.adminApprovePayment(appState.currentUser.email) },
                                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Aprovar Lançamento", fontSize = 10.sp)
                                            }
                                            Button(
                                                onClick = { appState.adminRejectPayment(appState.currentUser.email) },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Recusar", fontSize = 10.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            // Other accounts list
                            Spacer(modifier = Modifier.height(8.dp))
                            appState.allAdminSubscribers.forEach { account ->
                                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = account.email, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                            Text(text = if (account.isSubscribed) "Anual Pago" else "Gratuito / Pendente", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                        }
                                        if (!account.isSubscribed) {
                                            Button(
                                                onClick = { appState.adminApprovePayment(account.email) },
                                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                                modifier = Modifier.height(28.dp)
                                            ) {
                                                Text("Aprovar", fontSize = 9.sp)
                                            }
                                        } else {
                                            Icon(Icons.Default.VerifiedUser, "Subscribed", tint = MaterialTheme.colorScheme.secondary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SIMULATED PHONE BOTTOM NAVIGATION BAR (for typical Mobile views structure)
            if (displayScreen != "Splash" && displayScreen != "Login" && displayScreen != "Cadastro" && displayScreen != "VerifyPin" && displayScreen != "Recovery" && displayScreen != "Assinatura") {
                NavigationBar(
                    modifier = Modifier.height(56.dp),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    NavigationBarItem(
                        selected = displayScreen == "Dashboard",
                        onClick = { currentScreen = "Dashboard" },
                        icon = { Icon(Icons.Default.Dashboard, "Home") },
                        label = { Text("Painel", fontSize = 9.sp) }
                    )
                    NavigationBarItem(
                        selected = displayScreen == "NovaPublicacao",
                        onClick = { currentScreen = "NovaPublicacao" },
                        icon = { Icon(Icons.Default.AddBox, "Post") },
                        label = { Text("Novo Post", fontSize = 9.sp) }
                    )
                    NavigationBarItem(
                        selected = displayScreen == "Calendar",
                        onClick = { currentScreen = "Calendar" },
                        icon = { Icon(Icons.Default.CalendarToday, "Agenda") },
                        label = { Text("Agenda", fontSize = 9.sp) }
                    )
                    NavigationBarItem(
                        selected = displayScreen == "Configuracoes",
                        onClick = { currentScreen = "Configuracoes" },
                        icon = { Icon(Icons.Default.Settings, "Config") },
                        label = { Text("Ajustes", fontSize = 9.sp) }
                    )
                }
            }
        }
    }
}
