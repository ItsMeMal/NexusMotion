# NexusMotion

NexusMotion is an Android-first creative editing application focused on three connected workflows:

- **Motion** — keyframe animation, typography, shapes, timing and easing.
- **VFX** — layered compositing, masks, chroma key, effects and color work.
- **3D** — scene assembly, mesh/material editing, lighting, camera and animation.

The goal is a single project format and a shared timeline, with dedicated 2D, compositing and 3D render services behind the editor UI.

## Current status

**Foundation / editor-shell stage.** The repository starts from an empty Android project. This first implementation establishes the Android app, distinct workspace entry points, an initial editor shell and a small, renderer-independent timeline model. It is not yet a full Alight Motion, After Effects or Blender replacement: the current preview and controls are editor scaffolding, not a production renderer or export pipeline.

## Technical direction

- Android application: Kotlin + Jetpack Compose.
- Minimum Android version: API 26.
- Build: Android Gradle Plugin 8.13.x, Gradle 8.14.8, JDK 17.
- Timeline/document model: pure Kotlin, independent from the UI.
- 2D/VFX rendering: dedicated GPU render graph using Android graphics APIs.
- 3D preview: evaluate and integrate Google's Filament renderer.
- Media import/export: Android MediaCodec / MediaExtractor pipeline, with proxy media for mobile editing.

The rendering services will be separate modules so the UI does not own frame evaluation, decoding or GPU resource lifetimes.

## Build

On Windows:

```powershell
.\gradlew.bat assembleDebug
```

On Linux/macOS:

```bash
./gradlew assembleDebug
```

The bootstrap scripts download the pinned Gradle distribution from the official Gradle distribution host and verify its SHA-256 using the published checksum file before extraction. Android SDK Platform 35 and Build Tools 35.0.0 are used by the initial app module.

APK output:

`app/build/outputs/apk/debug/app-debug.apk`

## Initial milestones

1. **Foundation** — app shell, project/document model, timeline and local project persistence.
2. **Motion editor** — layers, transform tracks, keyframes, easing, text and shape layers.
3. **VFX editor** — compositing graph, masks, blend modes, chroma key and effect stack.
4. **3D editor** — scene graph, GLTF import, materials, lights, cameras and animation.
5. **Render/export** — preview proxies, frame renderer, audio/video muxing and export presets.

## Project principles

- Do not imitate the UI or copy proprietary implementation code from other creative applications.
- Keep the project format documented and versioned.
- Make editing operations non-destructive.
- Treat preview quality and export quality as separate render paths.
- Be explicit about unsupported codecs, effects and 3D formats rather than silently degrading them.
