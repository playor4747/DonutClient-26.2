# GitHub Actions — builda modden med ett klick

## 1. Lägg projektet på GitHub

Skapa ett nytt GitHub-repository och ladda upp **innehållet i den här mappen** till repositoryts rot.

Det ska bland annat finnas:

- `.github/workflows/build.yml`
- `build.gradle`
- `settings.gradle`
- `gradle.properties`
- `src/`

## 2. Starta bygget

På GitHub: öppna repositoryt → **Actions** → **Build DonutClient 26.2** → **Run workflow** → **Run workflow**.

Workflowen installerar Java 25 och Gradle 9.5.1, kör `gradle build` och laddar upp JAR-filen som en artifact.

## 3. Hämta JAR-filen

När körningen blir grön:

**Actions → klicka på körningen → Artifacts → DonutClient-26.2**

Där kan du hämta ZIP-filen som innehåller `.jar`-filen.

## 4. Viktigt

Modden är avsedd för Fabric och Minecraft 26.2. Serverregler kan förbjuda vissa klientfunktioner, så använd bara funktioner där de är tillåtna.
