# Nether Roof Portal Blocker

Prevents entities from entering Nether portals whose portal blocks are at Y=128 or higher in the Nether, and breaks the portal block an entity attempts to enter. Minecraft's normal neighbor updates clear the rest of the portal. Portals below the roof and portals in other dimensions are unaffected. This is a server-side Fabric mod that also loads in singleplayer.

## Build

Run `gradlew.bat build` on Windows. The built mod JAR will be in `build/libs`.

Requires Java 25 and Minecraft 26.3.
