package com.example.android.presentation.screens.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.presentation.screens.home.HomeState
import kotlinx.coroutines.isActive
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ─── Model ────────────────────────────────────────────────────────────────────
// Class thường (không phải data class) để mutate trực tiếp,
// tránh copy object mỗi frame
private class ShootingStar(
    val startX: Float,
    val startY: Float,
    val cosA: Float,          // cos(angle) tính sẵn 1 lần
    val sinA: Float,          // sin(angle) tính sẵn 1 lần
    val tailLength: Float,
    val size: Float,
    val progressSpeed: Float, // lượng progress tăng mỗi ms
    var progress: Float = 0f
)

// Sao tĩnh nền — tính 1 lần, không bao giờ thay đổi
private val STATIC_STARS = listOf(
    Offset(0.08f, 0.15f), Offset(0.20f, 0.60f), Offset(0.35f, 0.20f),
    Offset(0.50f, 0.75f), Offset(0.65f, 0.10f), Offset(0.75f, 0.55f),
    Offset(0.85f, 0.25f), Offset(0.92f, 0.70f), Offset(0.45f, 0.45f),
    Offset(0.15f, 0.85f), Offset(0.55f, 0.35f), Offset(0.78f, 0.82f)
)

// Gradient tạo 1 lần, reuse mãi — không allocate mỗi frame
private val BG_GRADIENT = Brush.linearGradient(
    colors = listOf(Color(0xFF0F2557), Color(0xFF1A3A8F), Color(0xFF1565C0))
)
private val BOLT_GRADIENT = Brush.linearGradient(
    colors = listOf(Color(0xFFFFD54F), Color(0xFFFFA000))
)

// ─── Composable ───────────────────────────────────────────────────────────────
@Composable
fun StreakCard(state: HomeState) {
    // Plain list, KHÔNG phải State — chỉ Canvas đọc, không trigger recompose
    val stars = remember { ArrayList<ShootingStar>(8) }

    // Một Long State duy nhất để trigger Canvas redraw.
    // Chỉ Canvas lambda subscribe → UI tĩnh (Text, Icon) KHÔNG recompose.
    var frameTime by remember { mutableLongStateOf(0L) }

    // withFrameMillis sync với Choreographer — không tạo timer riêng,
    // tự dừng khi composable rời khỏi màn hình (isActive = false)
    LaunchedEffect(Unit) {
        var lastMs = 0L
        var nextSpawnMs = 0L

        while (isActive) {
            withFrameMillis { ms ->
                val delta = if (lastMs == 0L) 16L else (ms - lastMs).coerceIn(1L, 64L)
                lastMs = ms

                // Update từng sao — mutate trực tiếp, zero allocation
                val iter = stars.iterator()
                while (iter.hasNext()) {
                    val s = iter.next()
                    s.progress += s.progressSpeed * delta
                    if (s.progress >= 1.2f) iter.remove()
                }

                // Sinh sao mới khi đến lúc và còn slot (tối đa 6)
                if (ms >= nextSpawnMs && stars.size < 6) {
                    val angleRad = Math.toRadians(30.0 + Random.nextFloat() * 20.0).toFloat()
                    stars.add(
                        ShootingStar(
                            startX        = Random.nextFloat() * 0.7f,
                            startY        = Random.nextFloat() * 0.5f,
                            cosA          = cos(angleRad),
                            sinA          = sin(angleRad),
                            tailLength    = 60f + Random.nextFloat() * 80f,
                            size          = 2f + Random.nextFloat() * 2f,
                            // progress đi từ 0→1.2 trong khoảng 1200–2000ms
                            progressSpeed = 1.2f / (1200f + Random.nextFloat() * 800f)
                        )
                    )
                    nextSpawnMs = ms + 600L + Random.nextLong(700L)
                }

                // Chỉ dòng này trigger recompose — và chỉ Canvas đọc nó
                frameTime = ms
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BG_GRADIENT)
        ) {
            // Canvas lambda đọc frameTime → chỉ nó invalidate khi frame đến
            // Row/Text/Icon bên dưới không đọc frameTime → không recompose
            @Suppress("UNUSED_EXPRESSION") frameTime
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawStaticStars(size.width, size.height)
                stars.forEach { drawShootingStar(it, size.width, size.height) }
            }

            // ── UI tĩnh — không bao giờ recompose vì animation ──────────────
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "HÀNH TRÌNH CỦA BẠN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${state.streakDays}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        lineHeight = 48.sp
                    )
                    Text(
                        text = "Days Streak",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        Column {
                            Text(
                                "Total Cards",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.55f),
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = String.format("%,d", state.totalCardsLearned),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64B5F6)
                            )
                        }
                        Column {
                            Text(
                                "Last Session",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.55f),
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = state.lastSessionText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFFBC02D).copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(BOLT_GRADIENT),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = null,
                            tint = Color(0xFF3E2723),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

// ─── Draw helpers (không allocate object trong hot path) ──────────────────────

private fun DrawScope.drawStaticStars(w: Float, h: Float) {
    STATIC_STARS.forEach { pos ->
        drawCircle(
            color  = Color.White.copy(alpha = 0.4f),
            radius = 1.2f,
            center = Offset(pos.x * w, pos.y * h)
        )
    }
}

private fun DrawScope.drawShootingStar(star: ShootingStar, w: Float, h: Float) {
    val travel = w * 1.2f
    val headX  = star.startX * w + star.cosA * star.progress * travel
    val headY  = star.startY * h + star.sinA * star.progress * travel

    if (headX < -80f || headX > w + 80f || headY < -80f || headY > h + 80f) return

    val alpha = when {
        star.progress < 0.10f -> (star.progress / 0.10f).coerceIn(0f, 1f)
        star.progress > 0.85f -> ((1f - star.progress) / 0.15f).coerceIn(0f, 1f)
        else -> 1f
    }
    if (alpha < 0.01f) return

    val tailX = headX - star.cosA * star.tailLength
    val tailY = headY - star.sinA * star.tailLength

    // Brush.linearGradient ở đây vẫn cần tạo mới vì start/end thay đổi mỗi frame.
    // Đây là chi phí nhỏ nhất không thể tránh với tail gradient.
    drawLine(
        brush = Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.White.copy(alpha = alpha * 0.85f)),
            start  = Offset(tailX, tailY),
            end    = Offset(headX, headY)
        ),
        start       = Offset(tailX, tailY),
        end         = Offset(headX, headY),
        strokeWidth = star.size * 0.8f,
        cap         = StrokeCap.Round
    )
    drawCircle(
        color  = Color.White.copy(alpha = alpha),
        radius = star.size,
        center = Offset(headX, headY)
    )
    drawCircle(
        color  = Color(0xFFB3E5FC).copy(alpha = alpha * 0.35f),
        radius = star.size * 2.5f,
        center = Offset(headX, headY)
    )
}