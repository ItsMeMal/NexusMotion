# NexusMotion engine architecture

## Separation of concerns

The application is split into four layers. UI events should describe edits; the engine owns evaluation and rendering.

1. **Document layer** — versioned project data, assets, canvas settings, layers and tracks.
2. **Timeline layer** — frame/time conversion, keyframe ordering, interpolation, playback state and animation evaluation.
3. **Render layer** — a render graph that consumes an immutable evaluated frame and produces preview/export frames.
4. **Platform layer** — Android media decoding, audio playback, file access, GPU surfaces and export/muxing.

The timeline model must remain pure Kotlin so it can be unit-tested without an Android device.

## Workspace responsibilities

### Motion

- Transform tracks: position, anchor, scale, rotation and opacity.
- Text and vector-shape layers.
- Bezier/easing curves, keyframe interpolation and motion paths.
- Blend modes and masks.
- Audio markers and beat-aligned timing.

### VFX compositing

- Footage and image layers.
- Layer order, masks and alpha operations.
- Chroma key, color correction and effect nodes.
- Tracking data and effect parameters.
- Non-destructive effect stack.

### 3D

- Scene graph with objects, transforms, cameras and lights.
- Meshes, materials and texture slots.
- Animation tracks connected to the shared timeline.
- GLB/glTF as the first practical interchange format.
- Filament as the candidate real-time Android renderer.

## Blender interoperability

NexusMotion is not a port of Blender and will not claim to open and edit every Blender native project natively. Blender's native project format contains application-specific scene data and dependencies. The first interoperability target should be exporting assets from Blender to GLB/glTF and importing those assets into NexusMotion. The NexusMotion project then stores its own scene edits and animation data.

## Mobile render strategy

- Separate interactive preview quality from final export quality.
- Use lower-resolution proxy media during editing.
- Decode media with Android platform codecs where possible.
- Keep frame evaluation deterministic so preview and export use the same document state.
- Avoid retaining decoded full-resolution frames or GPU textures longer than necessary.
- Treat unsupported codecs, effects or import features as explicit capability errors.

## Project container

The planned NexusMotion project file is a ZIP-based container:

    project.nsmotion
    ├── manifest.json
    ├── document.json
    ├── assets/
    │   ├── media/
    │   ├── images/
    │   ├── audio/
    │   └── models/
    └── previews/

The manifest records the project format version, app version and asset table. The document references assets by stable IDs rather than filesystem paths. Migrations must be versioned and must preserve unknown metadata where possible.

## Implementation status

The initial repository includes the Android application shell and a small scalar keyframe evaluator. The editor viewport is currently a UI preview illustration. It does not yet decode footage, composite real media, manipulate meshes, or render/export a finished video.
