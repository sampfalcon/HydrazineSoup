# Pig Saddle Speed

This server-side Fabric mod gives saddled pigs, horses, donkeys, camels, happy ghasts, and mules a configurable movement speed multiplier. Each mount type gets its own movement speed modifier. The default is `1.5`, or 150% of the mount's normal movement speed.

On first launch, the mod creates `config/pig_saddle_speed.properties` in the Minecraft game directory. Edit `movement_speed_multiplier` to a value from `1.0` to `10.0`; `1.0` disables the boost. Restart the game for changes to take effect.

Build on Windows with Java 25 using `gradlew.bat build`. The mod JAR is created in `build/libs`.
