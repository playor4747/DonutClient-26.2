# DonutClient — Minecraft 26.2 Fabric

Client-side Fabric project with:

- **Right Shift** — opens the settings/search menu.
- **F7** — toggles Storage ESP.
- **F6** — toggles Freecam.
- Search box — type a block id or name such as `chest`, `barrel`, `shulker`, `hopper`, `diamond_ore` and press **Search**.
- The search result list is also used by **Search ESP** when that setting is enabled.
- Storage ESP scans loaded blocks around the player and highlights chest/barrel/shulker/hopper-style storage blocks.

## Build

Minecraft 26.2 uses the modern non-obfuscated toolchain. Fabric currently documents Java 25 for 26.2 projects. Install a JDK 25 and use Gradle with this project, then run:

```text
gradle build
```

The jar will be under `build/libs/`.

## Important

This is a client-side mod. It does not send custom movement packets or contain an anti-cheat bypass. Whether a server permits ESP/freecam is determined by that server's rules.

The 26.2 rendering API changed substantially. This project intentionally keeps the world highlighting implementation isolated in `StorageEsp.java` so it can be updated independently if the exact Fabric API build installed locally differs from the one in `gradle.properties`.

## GitHub Actions

För att bygga utan att installera Java/Gradle lokalt kan du lägga projektet på GitHub och köra workflowen i `.github/workflows/build.yml`.
