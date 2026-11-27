# Pig Saddle Speed

This server-side Fabric mod gives pigs, horses, donkeys, camels, happy ghasts, and mules separately configurable movement speed multipliers. Pigs receive their multiplier while being ridden; other mounts receive theirs while saddled. Each defaults to `1.5` and can be set from `1.0` to `10.0`.

On first launch, the mod creates `config/pig_saddle_speed.properties` in the Minecraft game directory. Configure `pig_movement_speed_multiplier`, `horse_movement_speed_multiplier`, `donkey_movement_speed_multiplier`, `camel_movement_speed_multiplier`, `happy_ghast_movement_speed_multiplier`, and `mule_movement_speed_multiplier` independently. Set a value to `1.0` to disable the boost for that mount. Restart the game for changes to take effect. Existing configs with the old shared `movement_speed_multiplier` setting are migrated to all six settings, preserving their current multiplier.

Build on Windows with Java 25 using `gradlew.bat build`. The mod JAR is created in `build/libs`.
