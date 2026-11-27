package pigsaddlespeed;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

public final class PigSaddleSpeedMod implements ModInitializer {
	public static final String MOD_ID = "pig_saddle_speed";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Path CONFIG_PATH = FabricLoader.getInstance()
			.getConfigDir()
			.resolve("pig_saddle_speed.properties");
	private static final String LEGACY_MULTIPLIER_KEY = "movement_speed_multiplier";
	private static final double DEFAULT_MULTIPLIER = 1.5;
	private static Map<MountType, Double> movementSpeedMultipliers = Map.of();

	@Override
	public void onInitialize() {
		movementSpeedMultipliers = Map.copyOf(loadConfig());
		for (MountType mountType : MountType.values()) {
			LOGGER.info("{} movement speed multiplier: {}x",
					mountType.getConfigName(),
					getMovementSpeedMultiplier(mountType));
		}
	}

	public static double getMovementSpeedMultiplier(MountType mountType) {
		Double multiplier = movementSpeedMultipliers.get(mountType);
		if (multiplier == null) {
			throw new IllegalStateException("Movement speed multiplier has not been loaded for " + mountType);
		}
		return multiplier;
	}

	private static EnumMap<MountType, Double> loadConfig() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			Properties properties = new Properties();
			boolean configExists = Files.exists(CONFIG_PATH);

			if (configExists) {
				try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
					properties.load(reader);
				}
			}

			String legacyValue = properties.getProperty(LEGACY_MULTIPLIER_KEY);
			boolean needsMigration = legacyValue != null;
			EnumMap<MountType, Double> multipliers = new EnumMap<>(MountType.class);
			Double legacyMultiplier = null;

			for (MountType mountType : MountType.values()) {
				String configuredValue = properties.getProperty(mountType.getConfigKey());
				if (configuredValue == null) {
					if (legacyValue != null) {
						if (legacyMultiplier == null) {
							legacyMultiplier = parseMultiplier(LEGACY_MULTIPLIER_KEY, legacyValue);
						}
						multipliers.put(mountType, legacyMultiplier);
					} else {
						multipliers.put(mountType, DEFAULT_MULTIPLIER);
					}
					properties.setProperty(
							mountType.getConfigKey(),
							Double.toString(multipliers.get(mountType)));
					needsMigration = true;
				} else {
					multipliers.put(mountType, parseMultiplier(mountType.getConfigKey(), configuredValue));
				}
			}

			if (legacyValue != null) {
				properties.remove(LEGACY_MULTIPLIER_KEY);
			}

			if (needsMigration) {
				try (Writer writer = Files.newBufferedWriter(
						CONFIG_PATH,
						StandardCharsets.UTF_8,
						StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING,
						StandardOpenOption.WRITE
				)) {
					properties.store(writer, "Per-mount movement speed multipliers (1.0-10.0).");
				}
			}

			return multipliers;
		} catch (IOException exception) {
			throw new IllegalStateException("Could not read or create config file " + CONFIG_PATH, exception);
		}
	}

	private static double parseMultiplier(String key, String configuredValue) {
		double multiplier;
		try {
			multiplier = Double.parseDouble(configuredValue.trim());
		} catch (NumberFormatException exception) {
			throw new IllegalStateException("Invalid " + key + " in " + CONFIG_PATH, exception);
		}

		if (!Double.isFinite(multiplier) || multiplier < 1.0 || multiplier > 10.0) {
			throw new IllegalStateException(key + " must be between 1.0 and 10.0 in " + CONFIG_PATH);
		}

		return multiplier;
	}

	public enum MountType {
		PIG("pig"),
		HORSE("horse"),
		DONKEY("donkey"),
		CAMEL("camel"),
		HAPPY_GHAST("happy_ghast"),
		MULE("mule");

		private final String configName;

		MountType(String configName) {
			this.configName = configName;
		}

		public String getConfigName() {
			return configName;
		}

		public String getConfigKey() {
			return configName + "_movement_speed_multiplier";
		}
	}
}
