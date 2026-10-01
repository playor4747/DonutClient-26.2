# Build on Windows

1. Make sure this folder is on a normal ASCII-only path, for example `C:\MinecraftMods\DonutClient-26.2`.
2. Double-click `build.bat`.
3. The script installs Microsoft OpenJDK 25 with `winget` when needed, downloads Gradle 9.5.1, then runs `gradle build`.
4. The resulting JAR files are placed in `build\libs`.

The project targets Minecraft 26.2, Fabric Loader 0.19.3, Fabric API 0.158.0+26.2 and Java 25.
