package com.companheirovirtual.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.companheirovirtual.app.R
import com.companheirovirtual.app.character.CharacterAssets
import com.companheirovirtual.app.ui.theme.InkSoft
import com.companheirovirtual.app.ui.theme.LavenderDeep
import kotlinx.coroutines.delay

@Composable
fun CharacterStage(
    reactionEmoji: String?,
    reactionId: Int,
    modifier: Modifier = Modifier
) {
    val imageId = rememberDrawableId(CharacterAssets.BASE_DRAWABLE)
    val stageShape = RoundedCornerShape(36.dp)

    val transition = rememberInfiniteTransition(label = "breathing")
    val breath by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )

    val bounce = remember { Animatable(1f) }
    var showBubble by remember { mutableStateOf(false) }

    // Guarda o contador de reações já existente quando a tela abre (ex.: após girar a tela),
    // para só animar ações feitas depois disso.
    val firstReactionId = remember { reactionId }

    LaunchedEffect(reactionId) {
        if (reactionId > firstReactionId) {
            showBubble = true
            bounce.animateTo(1.1f, tween(durationMillis = 120))
            bounce.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            delay(1000)
            showBubble = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(stageShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.95f), Color(0xFFFFE6F1).copy(alpha = 0.75f))
                )
            )
            .border(3.dp, Color.White, stageShape)
    ) {
        // Decoração do palco (separada do personagem)
        Text("✨", fontSize = 20.sp, modifier = Modifier.align(Alignment.TopStart).padding(18.dp))
        Text("💗", fontSize = 18.sp, modifier = Modifier.align(Alignment.BottomEnd).padding(18.dp))
        Text("✨", fontSize = 14.sp, modifier = Modifier.align(Alignment.BottomStart).padding(22.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .graphicsLayer {
                    val s = breath * bounce.value
                    scaleX = s
                    scaleY = s
                    transformOrigin = TransformOrigin(0.5f, 1f)
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageId != 0) {
                Image(
                    painter = painterResource(imageId),
                    contentDescription = stringResource(R.string.character_desc),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                CharacterPlaceholder()
            }
        }

        AnimatedVisibility(
            visible = showBubble && reactionEmoji != null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.White,
                shadowElevation = 6.dp
            ) {
                Text(
                    text = reactionEmoji.orEmpty(),
                    fontSize = 34.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun CharacterPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRoundRect(
                    color = LavenderDeep.copy(alpha = 0.5f),
                    cornerRadius = CornerRadius(28.dp.toPx()),
                    style = Stroke(
                        width = 3.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f))
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎨", fontSize = 36.sp)
            Text(
                text = stringResource(R.string.character_placeholder),
                color = InkSoft,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, start = 12.dp, end = 12.dp)
            )
        }
    }
}
