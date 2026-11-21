package com.itsmemal.nexusmotion.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itsmemal.nexusmotion.engine.timeline.KeyframeInterpolation
import com.itsmemal.nexusmotion.engine.timeline.ScalarKeyframe
import com.itsmemal.nexusmotion.engine.timeline.TimelineCurve
import com.itsmemal.nexusmotion.ui.theme.NexusPalette
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private enum class WorkspaceMode(
    val label: String,
    val subtitle: String,
    val code: String,
    val accent: Color,
    val description: String,
    val tools: List<String>
) {
    MOTION(
        label = "Motion",
        subtitle = "2D / KEYFRAMES",
        code = "02D",
        accent = NexusPalette.lime,
        description = "Animate type, shapes and layered motion with precise timing.",
        tools = listOf("Select", "Transform", "Text", "Shape", "Mask", "Audio")
    ),
    VFX(
        label = "Composite",
        subtitle = "VFX / LAYERS",
        code = "VFX",
        accent = NexusPalette.cyan,
        description = "Build a shot from footage, masks, keys and effect passes.",
        tools = listOf("Select", "Mask", "Chroma", "Blend", "Grade", "Track")
    ),
    THREE_D(
        label = "3D Scene",
        subtitle = "MESH / LIGHT / CAMERA",
        code = "3D",
        accent = NexusPalette.violet,
        description = "Block out a scene, place lights and prepare animated cameras.",
        tools = listOf("Select", "Mesh", "Material", "Light", "Camera", "Rig")
    )
}

@Composable
fun NexusMotionApp() {
    var activeMode by remember { mutableStateOf<WorkspaceMode?>(null) }

    if (activeMode == null) {
        HomeScreen(onCreate = { activeMode = it })
    } else {
        EditorScreen(
            mode = activeMode!!,
            onBack = { activeMode = null },
            onSwitchMode = { activeMode = it }
        )
    }
}

@Composable
private fun HomeScreen(onCreate: (WorkspaceMode) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusPalette.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NexusPalette.lime),
                contentAlignment = Alignment.Center
            ) {
                Text("N", color = NexusPalette.background, fontSize = 23.sp, fontWeight = FontWeight.Black)
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text("NEXUSMOTION", color = NexusPalette.text, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.2.sp)
                Text("CREATIVE WORKSPACE", color = NexusPalette.muted, fontSize = 8.sp, letterSpacing = 1.4.sp)
            }
            Spacer(Modifier.weight(1f))
            StatusPill(label = "ANDROID / BETA", color = NexusPalette.cyan)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Spacer(Modifier.height(18.dp))
            Eyebrow("WORKSPACE  /  001")
            Text(
                "Make something\nmove.",
                color = NexusPalette.text,
                fontSize = 35.sp,
                lineHeight = 37.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-1.3).sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                "One project space for motion, visual effects and 3D scenes.",
                color = NexusPalette.muted,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .width(290.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Eyebrow("START A PROJECT")
                Spacer(Modifier.weight(1f))
                Text("03 WORKSPACES", color = NexusPalette.dim, fontSize = 8.sp, letterSpacing = 1.sp)
            }

            WorkspaceMode.entries.forEachIndexed { index, mode ->
                WorkspaceCard(
                    mode = mode,
                    index = index + 1,
                    onClick = { onCreate(mode) },
                    modifier = Modifier.padding(bottom = 9.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 23.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Eyebrow("RECENT PROJECTS")
                Spacer(Modifier.weight(1f))
                Text("LOCAL", color = NexusPalette.dim, fontSize = 8.sp, letterSpacing = 1.sp)
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = NexusPalette.panel,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, NexusPalette.line)
            ) {
                Row(
                    modifier = Modifier.padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NexusPalette.panelRaised),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = NexusPalette.muted, fontSize = 22.sp)
                    }
                    Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                        Text("No projects yet", color = NexusPalette.text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Your local projects will appear here.", color = NexusPalette.muted, fontSize = 9.sp, modifier = Modifier.padding(top = 4.dp))
                    }
                    Text("EMPTY", color = NexusPalette.dim, fontSize = 7.sp, letterSpacing = 1.sp)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 23.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("NEXUSMOTION / BUILD 0001", color = NexusPalette.dim, fontSize = 7.sp, letterSpacing = .8.sp)
                Text("LOCAL FIRST", color = NexusPalette.dim, fontSize = 7.sp, letterSpacing = .8.sp)
            }
        }
    }
}

@Composable
private fun WorkspaceCard(
    mode: WorkspaceMode,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = NexusPalette.panel,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusPalette.line)
    ) {
        Row(
            modifier = Modifier.padding(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(57.dp)
                    .clip(CircleShape)
                    .background(mode.accent)
            )
            Box(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(45.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(mode.accent.copy(alpha = .12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(mode.code, color = mode.accent, fontSize = if (mode.code.length > 2) 11.sp else 15.sp, fontWeight = FontWeight.Black)
            }
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(mode.subtitle, color = mode.accent, fontSize = 7.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
                Text(mode.label, color = NexusPalette.text, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 3.dp))
                Text(mode.description, color = NexusPalette.muted, fontSize = 8.sp, lineHeight = 12.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 3.dp))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(index.toString().padStart(2, '0'), color = NexusPalette.dim, fontSize = 8.sp, letterSpacing = 1.sp)
                Text("↗", color = mode.accent, fontSize = 21.sp, modifier = Modifier.padding(top = 5.dp))
            }
        }
    }
}

@Composable
private fun EditorScreen(
    mode: WorkspaceMode,
    onBack: () -> Unit,
    onSwitchMode: (WorkspaceMode) -> Unit
) {
    var playing by remember(mode) { mutableStateOf(false) }
    var progress by remember(mode) { mutableFloatStateOf(0f) }
    var layerCount by remember(mode) { mutableIntStateOf(3) }
    var keyframes by remember(mode) { mutableStateOf(emptyList<ScalarKeyframe>()) }
    val evaluatedMotionValue = TimelineCurve.sample(keyframes, progress * 300f) ?: (progress * 100f)
    var selectedTool by remember(mode) { mutableStateOf(mode.tools.first()) }
    var statusMessage by remember(mode) { mutableStateOf("Draft project · changes are not saved yet") }

    LaunchedEffect(playing) {
        while (playing) {
            delay(33)
            progress = if (progress >= 1f) 0f else progress + 0.0045f
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusPalette.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("‹", color = NexusPalette.text, fontSize = 29.sp, modifier = Modifier.clickable(onClick = onBack).padding(end = 13.dp))
            Column {
                Text("UNTITLED PROJECT", color = NexusPalette.text, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = .5.sp)
                Text("NEXUSMOTION / ${mode.subtitle}", color = NexusPalette.muted, fontSize = 7.sp, letterSpacing = .9.sp)
            }
            Spacer(Modifier.weight(1f))
            StatusPill("DRAFT", mode.accent)
            Text("⋮", color = NexusPalette.text, fontSize = 22.sp, modifier = Modifier.padding(start = 12.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 15.dp)
                .padding(bottom = 11.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            WorkspaceMode.entries.forEach { item ->
                ModeTab(
                    label = item.label,
                    selected = item == mode,
                    accent = item.accent,
                    onClick = { onSwitchMode(item) }
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 13.dp)
        ) {
            StagePreview(mode = mode, progress = progress, motionValue = evaluatedMotionValue)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Eyebrow("TIMELINE")
                    Text("00:00:${(progress * 30f).roundToInt().toString().padStart(2, '0')}:00", color = NexusPalette.text, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                }
                Spacer(Modifier.weight(1f))
                Text("30 FPS", color = NexusPalette.muted, fontSize = 8.sp, letterSpacing = .8.sp)
                Spacer(Modifier.width(11.dp))
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(mode.accent)
                        .clickable { playing = !playing },
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (playing) "Ⅱ" else "▶", color = NexusPalette.background, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }

            Slider(
                value = progress,
                onValueChange = { progress = it },
                colors = SliderDefaults.colors(
                    thumbColor = mode.accent,
                    activeTrackColor = mode.accent,
                    inactiveTrackColor = NexusPalette.line
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            )

            TimelineRuler(progress = progress, accent = mode.accent)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, NexusPalette.line, RoundedCornerShape(10.dp))
                    .background(NexusPalette.panel)
            ) {
                repeat(layerCount) { index ->
                    TimelineTrack(
                        index = index,
                        name = when (index) {
                            0 -> "TRANSFORM"
                            1 -> "MEDIA / LAYER"
                            2 -> "TEXT / GRAPHICS"
                            else -> "NEW LAYER $index"
                        },
                        accent = mode.accent,
                        keyframeCount = if (index == 0) keyframes.size else 0,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    if (index != layerCount - 1) {
                        Box(Modifier.fillMaxWidth().height(1.dp).background(NexusPalette.line.copy(alpha = .75f)))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 13.dp, bottom = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SmallToolButton(
                    label = "+  Layer",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        layerCount += 1
                        statusMessage = "Layer $layerCount added"
                    }
                )
                SmallToolButton(
                    label = "◇  Keyframe",
                    modifier = Modifier.weight(1f),
                    accent = mode.accent,
                    onClick = {
                        val frame = (progress * 300f).roundToInt()
                        val value = progress * 100f
                        keyframes = (
                            keyframes.filterNot { it.frame == frame } +
                                ScalarKeyframe(frame, value, KeyframeInterpolation.EASE_IN_OUT)
                            ).sortedBy(ScalarKeyframe::frame)
                        statusMessage = "Transform keyframe created at ${frame}f"
                    }
                )
            }

            Eyebrow("TOOLS  /  ${mode.subtitle}")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 9.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                mode.tools.forEach { tool ->
                    ToolChip(
                        label = tool,
                        selected = selectedTool == tool,
                        accent = mode.accent,
                        onClick = {
                            selectedTool = tool
                            statusMessage = "$tool tool selected"
                        }
                    )
                }
            }

            Text(statusMessage, color = NexusPalette.dim, fontSize = 8.sp, modifier = Modifier.padding(bottom = 12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("PREVIEW QUALITY", color = NexusPalette.muted, fontSize = 7.sp, letterSpacing = .7.sp)
                Spacer(Modifier.weight(1f))
                Text("DRAFT / 720P", color = mode.accent, fontSize = 7.sp, letterSpacing = .7.sp)
                Spacer(Modifier.width(12.dp))
                Text(
                    "EXPORT",
                    color = NexusPalette.background,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .clip(RoundedCornerShape(7.dp))
                        .background(NexusPalette.line)
                        .clickable { statusMessage = "Export is not available until the render pipeline is implemented" }
                        .padding(horizontal = 13.dp, vertical = 9.dp)
                )
            }
        }
    }
}

@Composable
private fun StagePreview(mode: WorkspaceMode, progress: Float, motionValue: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, NexusPalette.line, RoundedCornerShape(12.dp))
            .background(Color(0xFF0D1420))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Eyebrow("VIEWPORT  /  ${mode.code}")
            Spacer(Modifier.weight(1f))
            Text("50%", color = NexusPalette.dim, fontSize = 7.sp)
            Spacer(Modifier.width(9.dp))
            Box(Modifier.size(5.dp).clip(CircleShape).background(mode.accent))
            Text(" PREVIEW", color = mode.accent, fontSize = 7.sp, letterSpacing = .7.sp)
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(238.dp)
                .padding(top = 8.dp)
        ) {
            val grid = 24.dp.toPx()
            var x = 0f
            while (x <= size.width) {
                drawLine(Color(0xFF253246).copy(alpha = .38f), Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                x += grid
            }
            var y = 0f
            while (y <= size.height) {
                drawLine(Color(0xFF253246).copy(alpha = .38f), Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                y += grid
            }

            val stageWidth = when (mode) {
                WorkspaceMode.MOTION -> size.width * .43f
                WorkspaceMode.VFX -> size.width * .78f
                WorkspaceMode.THREE_D -> size.width * .66f
            }
            val stageHeight = when (mode) {
                WorkspaceMode.MOTION -> size.height * .91f
                WorkspaceMode.VFX -> size.height * .62f
                WorkspaceMode.THREE_D -> size.height * .72f
            }
            val left = (size.width - stageWidth) / 2f
            val top = (size.height - stageHeight) / 2f
            drawRoundRect(
                color = Color(0xFF101A28),
                topLeft = Offset(left, top),
                size = Size(stageWidth, stageHeight),
                cornerRadius = CornerRadius(7.dp.toPx())
            )
            drawRoundRect(
                color = Color(0xFF52647C),
                topLeft = Offset(left, top),
                size = Size(stageWidth, stageHeight),
                cornerRadius = CornerRadius(7.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
            )

            val center = Offset(left + stageWidth / 2f, top + stageHeight / 2f)
            when (mode) {
                WorkspaceMode.MOTION -> {
                    val shift = (motionValue / 100f - .5f) * stageWidth * .45f
                    drawCircle(
                        brush = Brush.linearGradient(listOf(mode.accent, Color(0xFF80BBAE))),
                        radius = stageWidth * .19f,
                        center = Offset(center.x + shift, center.y - stageHeight * .08f)
                    )
                    drawRoundRect(
                        color = Color(0xFFEDF2F9),
                        topLeft = Offset(center.x - stageWidth * .27f, center.y + stageHeight * .2f),
                        size = Size(stageWidth * .54f, stageHeight * .045f),
                        cornerRadius = CornerRadius(3.dp.toPx())
                    )
                }
                WorkspaceMode.VFX -> {
                    val radius = stageHeight * .22f
                    drawCircle(color = mode.accent.copy(alpha = .16f), radius = radius * 1.55f, center = center)
                    drawCircle(
                        color = mode.accent,
                        radius = radius,
                        center = Offset(center.x + (progress - .5f) * stageWidth * .25f, center.y),
                        style = Stroke(width = 3.dp.toPx())
                    )
                    drawLine(
                        color = Color(0xFFFFAA68),
                        start = Offset(left + stageWidth * .12f, center.y + radius),
                        end = Offset(left + stageWidth * .88f, center.y - radius),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                WorkspaceMode.THREE_D -> {
                    val edge = stageWidth * .24f
                    val skew = edge * .45f
                    val front = listOf(
                        Offset(center.x - edge / 2, center.y - edge / 2),
                        Offset(center.x + edge / 2, center.y - edge / 2),
                        Offset(center.x + edge / 2, center.y + edge / 2),
                        Offset(center.x - edge / 2, center.y + edge / 2)
                    )
                    val back = front.map { Offset(it.x + skew, it.y - skew) }
                    for (index in 0..3) {
                        val next = (index + 1) % 4
                        drawLine(mode.accent, front[index], front[next], strokeWidth = 2.dp.toPx())
                        drawLine(mode.accent.copy(alpha = .75f), back[index], back[next], strokeWidth = 2.dp.toPx())
                        drawLine(mode.accent.copy(alpha = .85f), front[index], back[index], strokeWidth = 2.dp.toPx())
                    }
                }
            }

            val markerX = left + stageWidth * progress
            drawLine(
                color = mode.accent.copy(alpha = .65f),
                start = Offset(markerX, top),
                end = Offset(markerX, top + stageHeight),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}

@Composable
private fun TimelineRuler(progress: Float, accent: Color) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .padding(horizontal = 7.dp)
    ) {
        val step = size.width / 10f
        for (index in 0..10) {
            val tickX = step * index
            drawLine(
                color = NexusPalette.dim,
                start = Offset(tickX, if (index % 2 == 0) 3.dp.toPx() else 9.dp.toPx()),
                end = Offset(tickX, size.height - 2.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )
        }
        val playhead = step * 10f * progress
        drawLine(accent, Offset(playhead, 0f), Offset(playhead, size.height), strokeWidth = 2.dp.toPx())
        drawCircle(accent, radius = 3.dp.toPx(), center = Offset(playhead, 2.dp.toPx()))
    }
}

@Composable
private fun TimelineTrack(
    index: Int,
    name: String,
    accent: Color,
    keyframeCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(47.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.width(108.dp)) {
            Text("${(index + 1).toString().padStart(2, '0')}  $name", color = NexusPalette.text, fontSize = 7.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(if (index == 0) "POSITION / SCALE / OPACITY" else "LAYER CONTENT", color = NexusPalette.dim, fontSize = 5.sp, letterSpacing = .35.sp, modifier = Modifier.padding(top = 3.dp))
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(21.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(NexusPalette.panelRaised)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (index == 0) .82f else .58f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(accent.copy(alpha = if (index == 0) .24f else .11f))
            )
            repeat(keyframeCount.coerceAtMost(8)) { key ->
                Box(
                    modifier = Modifier
                        .padding(start = (12 + key * 20).dp)
                        .size(7.dp)
                        .align(Alignment.CenterStart)
                        .background(accent, RoundedCornerShape(1.dp))
                )
            }
        }
    }
}

@Composable
private fun ModeTab(label: String, selected: Boolean, accent: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(7.dp),
        color = if (selected) accent.copy(alpha = .13f) else NexusPalette.panel,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) accent.copy(alpha = .55f) else NexusPalette.line)
    ) {
        Text(
            label.uppercase(),
            color = if (selected) accent else NexusPalette.muted,
            fontSize = 7.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = .7.sp,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun SmallToolButton(
    label: String,
    modifier: Modifier = Modifier,
    accent: Color = NexusPalette.text,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = NexusPalette.panelRaised,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NexusPalette.line)
    ) {
        Text(label, color = accent, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp))
    }
}

@Composable
private fun ToolChip(label: String, selected: Boolean, accent: Color, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        color = if (selected) accent.copy(alpha = .15f) else NexusPalette.panel,
        shape = RoundedCornerShape(7.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) accent.copy(alpha = .55f) else NexusPalette.line)
    ) {
        Text(
            label,
            color = if (selected) accent else NexusPalette.muted,
            fontSize = 7.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun StatusPill(label: String, color: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = .1f))
            .border(1.dp, color.copy(alpha = .25f), RoundedCornerShape(20.dp))
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(5.dp).clip(CircleShape).background(color))
        Text(label, color = color, fontSize = 6.sp, letterSpacing = .65.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp))
    }
}

@Composable
private fun Eyebrow(text: String) {
    Text(text, color = NexusPalette.muted, fontSize = 7.sp, letterSpacing = 1.25.sp, fontWeight = FontWeight.Bold)
}
