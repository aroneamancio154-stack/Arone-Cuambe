package com.example.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import com.example.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// -------------------------------------------------------------
// MODELS
// -------------------------------------------------------------

data class PostItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val price: Double,
    val phone: String,
    val whatsappLink: String,
    val postingTime: String, // HH:mm
    val repeatType: String, // "diario", "semanal", "mensal", "1ano"
    val daysCount: Int,
    val postsPerDay: Int,
    val imageFilter: String = "Normal",
    val imageScale: Float = 1.0f,
    val connectedNetworks: List<String>, // e.g., ["Facebook", "Instagram"]
    val createdDate: Date = Date(),
    val hasAdsCampaign: Boolean = false,
    val adsBudget: Double = 0.0,
    val adsAudience: String = "Maputo Cidade",
    val adsDurationDays: Int = 0
)

data class SocialAccount(
    val id: String,
    val platform: String, // "Facebook", "Instagram", "WhatsApp", "X"
    val username: String,
    val isConnected: Boolean,
    val avatarColorHex: Long
)

data class UserSession(
    val email: String = "aroneamancio154@gmail.com",
    val verified: Boolean = false,
    val hasTrialStarted: Boolean = true,
    val trialStartedAt: Long = System.currentTimeMillis() - 2 * 24 * 3600 * 1000L, // 2 days ago (expires tomorrow)
    val isSubscribed: Boolean = false,
    val paymentPending: Boolean = false,
    val pendingPaymentNumber: String = "",
    val pendingPaymentMethod: String = "" // "mpesa", "emola", "bim"
)

// -------------------------------------------------------------
// MAIN STATE HOLDER (SINGLETON)
// -------------------------------------------------------------

class AppState private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: AppState? = null

        fun getInstance(context: Context): AppState {
            return INSTANCE ?: synchronized(this) {
                val instance = AppState(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    private val postsKey = "autovenda_posts_pref"
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    // Active View Mode (📱 Celular / 💻 Web-Desktop computador)
    var viewMode by mutableStateOf("Celular") // "Celular" or "Web"
    
    // Theme options
    var isDarkMode by mutableStateOf(false)
    var currentLanguage by mutableStateOf("PT") // "PT" or "EN"
    var postsPerDaySetting by mutableStateOf(5) // Default general post rate

    // User Session
    var currentUser by mutableStateOf(UserSession())
    var activePinCode by mutableStateOf("")

    // List of connected Social networks
    var socialAccounts = mutableStateListOf<SocialAccount>(
        SocialAccount("1", "Facebook", "Loja Virtual Maputo", true, 0xFF1877F2),
        SocialAccount("2", "Instagram", "@lojavirtual_mz", true, 0xFFE1306C),
        SocialAccount("3", "WhatsApp Business", "+258 84 123 4567", false, 0xFF25D366),
        SocialAccount("4", "X (Twitter)", "@vendas_express", false, 0xFF1DA1F2)
    )

    // Initial Posts Queue to make dashboard vibrant immediately
    var scheduledPosts = mutableStateListOf<PostItem>(
        PostItem(
            title = "Sapatilhas Desportivas Nike Air",
            description = "🇲🇿 Super sapatilhas para corrida, material confortável e duradouro. Entrega grátis em Maputo!\n\n📞 Contactos: 841234567\n🔗 Adquira via WhatsApp!",
            price = 4500.0,
            phone = "841234567",
            whatsappLink = "https://wa.me/258841234567",
            postingTime = "08:00",
            repeatType = "diario",
            daysCount = 365,
            postsPerDay = 3,
            connectedNetworks = listOf("Facebook", "Instagram"),
            hasAdsCampaign = true,
            adsBudget = 1500.0,
            adsAudience = "Maputo, Matola, Nampula",
            adsDurationDays = 7
        ),
        PostItem(
            title = "Vestido de Verão Floral",
            description = "Lindos vestidos leves para usar no dia-a-dia a preços imbatíveis. Moda moçambicana elegante!\n\n🚚 Enviamos para todo o país.",
            price = 1200.0,
            phone = "869876543",
            whatsappLink = "https://wa.me/258869876543",
            postingTime = "12:30",
            repeatType = "semanal",
            daysCount = 30,
            postsPerDay = 2,
            connectedNetworks = listOf("Facebook")
        )
    )

    // Backoffice accounts lists
    var allAdminSubscribers = mutableStateListOf<UserSession>(
        UserSession("aroneamancio154@gmail.com", true, true, System.currentTimeMillis() - 2500000, true),
        UserSession("comercioMZ@outlook.com", true, false, 0, false),
        UserSession("beira_fashion@gmail.com", true, true, System.currentTimeMillis() - 4 * 86400000L, false) // EXPIRED
    )

    // Gemini API Action
    var isGeneratingCaption by mutableStateOf(false)
    var generatedCaptionResult by mutableStateOf("")

    // Simulated local events logs
    var eventsLog = mutableStateListOf<String>(
        "Sistema inicializado às ${getCurrentFormattedTime()}",
        "Post 'Nike Air' republicado automaticamente do cache local às 08:00.",
        "Sincronização com as APIs do Facebook Concluída."
    )

    // Offline auto save toggle
    var activeInternet by mutableStateOf(true)

    fun logEvent(msg: String) {
        val time = getCurrentFormattedTime()
        eventsLog.add(0, "[$time] $msg")
    }

    private fun getCurrentFormattedTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    // Trial checker
    val trialDaysRemaining: Int
        get() {
            val elapsed = System.currentTimeMillis() - currentUser.trialStartedAt
            val days = 3 - (elapsed / (24 * 3600 * 1000)).toInt()
            return if (days < 0) 0 else days
        }

    val isBannedDueToSubscriptionExp: Boolean
        get() {
            // Expired trial and premium not bought
            return (trialDaysRemaining <= 0 || !currentUser.hasTrialStarted) && !currentUser.isSubscribed
        }

    // Toggle simulated parameters
    fun triggerTrialExpiry() {
        currentUser = currentUser.copy(
            hasTrialStarted = true,
            trialStartedAt = System.currentTimeMillis() - 4 * 24 * 3600 * 1000L, // 4 days ago
            isSubscribed = false
        )
        logEvent("ADMIN: Teste experimental expirou manualmente.")
    }

    fun resetTrial() {
        currentUser = currentUser.copy(
            hasTrialStarted = true,
            trialStartedAt = System.currentTimeMillis(), // reset to today
            isSubscribed = false,
            paymentPending = false
        )
        logEvent("ADMIN: Teste experimental reiniciado por 3 dias.")
    }

    fun submitSimulatedPayment(number: String, method: String) {
        currentUser = currentUser.copy(
            paymentPending = true,
            pendingPaymentNumber = number,
            pendingPaymentMethod = method
        )
        logEvent("PAGAMENTO: Reisição enviada via $method para o número $number. Aguardando aprovação USSD push.")
    }

    fun adminApprovePayment(email: String) {
        if (email == currentUser.email) {
            currentUser = currentUser.copy(
                isSubscribed = true,
                paymentPending = false
            )
            logEvent("PAGAMENTO: Assinatura de 1.000 MT/ano aprovada com sucesso!")
        }
        val idx = allAdminSubscribers.indexOfFirst { it.email == email }
        if (idx != -1) {
            allAdminSubscribers[idx] = allAdminSubscribers[idx].copy(isSubscribed = true, paymentPending = false)
        }
    }

    fun adminRejectPayment(email: String) {
        if (email == currentUser.email) {
            currentUser = currentUser.copy(
                paymentPending = false
            )
            logEvent("PAGAMENTO: Recusado ou cancelado.")
        }
        val idx = allAdminSubscribers.indexOfFirst { it.email == email }
        if (idx != -1) {
            allAdminSubscribers[idx] = allAdminSubscribers[idx].copy(paymentPending = false)
        }
    }

    // ADD / REMOVE POSTS
    fun addPost(post: PostItem) {
        scheduledPosts.add(0, post)
        logEvent("CAMPANHA: Nova campanha '${post.title}' programada (${post.repeatType}) durante ${post.daysCount} dias.")
    }

    fun deletePost(id: String) {
        val item = scheduledPosts.find { it.id == id }
        if (item != null) {
            scheduledPosts.remove(item)
            logEvent("CAMPANHA: Campanha '${item.title}' removida.")
        }
    }

    // -------------------------------------------------------------
    // GEMINI CAPTION GENERATOR WITH REAL WEB RECOVERY
    // -------------------------------------------------------------
    fun runGeminiCaptionGeneration(title: String, price: Double, phone: String, onFinished: (String) -> Unit) {
        isGeneratingCaption = true
        generatedCaptionResult = ""

        val apiKey = BuildConfig.GEMINI_API_KEY
        val hasKey = apiKey.isNotEmpty() && apiKey != "MY_GEMINI_API_KEY"

        CoroutineScope(Dispatchers.IO).launch {
            if (hasKey) {
                try {
                    val prompt = """
                        Você é uma IA de marketing profissional especialista em vendas pelas redes sociais em Moçambique.
                        Crie uma publicação persuasiva, impactante e vendedora para o seguinte anúncio:
                        Produto: "$title"
                        Preço: $price MT (Meticais)
                        Contacto Directo WhatsApp: $phone
                        
                        Diretrizes:
                        1. Use emojis chamativos no início dos parágrafos.
                        2. Crie uma introdução que envolva a atenção ("Atenção Maputo", "Novidade", "Preço incrivel").
                        3. Destaque os benefícios e faça uma CTA clara indicando para ligar ou enviar mensagem no WhatsApp direct.
                        4. Adicione e formate um link direto de WhatsApp como: wa.me/258${phone.replace(" ", "")}
                        5. Adicione hashtags populares e relevantes como #Moçambique, #Maputo, #ComercioMZ, #ModaMZ, #AutoVenda.
                        6. Mantenha o tom profissional, limpo e atraente.
                    """.trimIndent()

                    val mediaType = "application/json; charset=utf-8".toMediaType()
                    val jsonRequest = JSONObject().apply {
                        put("contents", org.json.JSONArray().put(JSONObject().apply {
                            put("parts", org.json.JSONArray().put(JSONObject().apply {
                                put("text", prompt)
                            }))
                        }))
                    }

                    val requestBody = jsonRequest.toString().toRequestBody(mediaType)
                    val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

                    val request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()

                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            val bodyText = response.body?.string() ?: ""
                            val responseJson = JSONObject(bodyText)
                            val textResult = responseJson
                                .getJSONArray("candidates")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text")

                            withContext(Dispatchers.Main) {
                                isGeneratingCaption = false
                                generatedCaptionResult = textResult
                                onFinished(textResult)
                                logEvent("GEMINI AI: Legenda gerada com sucesso para o artigo '$title' via API oficial.")
                            }
                        } else {
                            throw Exception("HTTP Error: ${response.code}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AppState", "Gemini error", e)
                    withContext(Dispatchers.Main) {
                        // Fallback to high-quality local generation
                        val text = getFallbackMozambiqueCaption(title, price, phone)
                        isGeneratingCaption = false
                        generatedCaptionResult = text
                        onFinished(text)
                        logEvent("GEMINI AI: Falha na API. Usando gerador criativo local otimizado para Moçambique.")
                    }
                }
            } else {
                // Return high quality synthesized caption immediately
                withContext(Dispatchers.IO) {
                    Thread.sleep(1500) // Realistic delay
                    val text = getFallbackMozambiqueCaption(title, price, phone)
                    withContext(Dispatchers.Main) {
                        isGeneratingCaption = false
                        generatedCaptionResult = text
                        onFinished(text)
                        logEvent("GEMINI AI: Legenda gerada off-line de alta conversão para '$title'.")
                    }
                }
            }
        }
    }

    private fun getFallbackMozambiqueCaption(title: String, price: Double, phone: String): String {
        val emojis = listOf("🔥", "⭐️", "⚡️", "✨", "🛍")
        val expressions = listOf(
            "Novidade fresquinha e de altíssima qualidade a chegar até si!",
            "Grande oportunidade de negócio para quem busca estilo e durabilidade!",
            "Sempre a garantir os melhores artigos do mercado moçambicano!",
            "Qualidade garantida e atendimento rápido garantido!"
        )
        val emoji = emojis.random()
        val comment = expressions.random()
        
        return """
            $emoji *GRANDES VENDAS - AUTOVENDA SOCIAL*
            
            Procura por excelente qualidade com preço justo? Acaba de encontrar!
            
            📍 *ARTIGO:* $title
            💰 *PREÇO:* $price MT (Preço especial Moçambique)
            
            $comment
            
            💬 *Como encomendar?*
            Entre já em contacto connosco agora mesmo e garanta o seu produto:
            📞 WhatsApp Directo: $phone
            🔗 Clique aqui: wa.me/258${phone.replace(" ", "")}
            
            🚚 Entregas ao domicílio em Maputo Cidade e arredores! Enviamos para outras províncias via portador confiável.
            
            #Moçambique #Maputo #AutoVenda #VendasOnline #ComercioMZ #PrecoImbativel #PromoMZ
        """.trimIndent()
    }
}
