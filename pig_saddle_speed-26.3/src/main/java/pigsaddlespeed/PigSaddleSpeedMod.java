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
import java.util.Properties;

public final class PigSaddleSpeedMod implements ModInitializer {
	public static final String MOD_ID = "pig_saddle_speed";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Path CONFIG_PATH = FabricLoader.getInstance()
			.getConfigDir()
			.resolve("pig_saddle_speed.properties");
	private static final String MULTIPLIER_KEY = "movement_speed_multiplier";
	private static final double DEFAULT_MULTIPLIER = 1.5;
	private static double movementSpeedMultiplier;

	@Override
	public void onInitialize() {
		movementSpeedMultiplier = loadConfig();
		LOGGER.info("Saddled mount movement speed multiplier: {}x", movementSpeedMultiplier);
	}

	public static double getMovementSpeedMultiplier() {
		return movementSpeedMultiplier;
	}

	private static double loadConfig() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			Properties properties = new Properties();

			if (Files.notExists(CONFIG_PATH)) {
				properties.setProperty(MULTIPLIER_KEY, Double.toString(DEFAULT_MULTIPLIER));
				try (Writer writer = Files.newBufferedWriter(
						CONFIG_PATH,
						StandardCharsets.UTF_8,
						StandardOpenOption.CREATE_NEW,
						StandardOpenOption.WRITE
				)) {
					properties.store(writer, "Multiplier applied to supported mounts' movement speed while saddled (1.0-10.0).");
				}
				return DEFAULT_MULTIPLIER;
			}

			try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
				properties.load(reader);
			}

			String configuredValue = properties.getProperty(MULTIPLIER_KEY);
			if (configuredValue == null) {
				throw new IllegalStateException("Missing " + MULTIPLIER_KEY + " in " + CONFIG_PATH);
			}

			double multiplier;
			try {
				multiplier = Double.parseDouble(configuredValue.trim());
			} catch (NumberFormatException exception) {
				throw new IllegalStateException("Invalid " + MULTIPLIER_KEY + " in " + CONFIG_PATH, exception);
			}

			if (!Double.isFinite(multiplier) || multiplier < 1.0 || multiplier > 10.0) {
				throw new IllegalStateException(MULTIPLIER_KEY + " must be between 1.0 and 10.0 in " + CONFIG_PATH);
			}

			return multiplier;
		} catch (IOException exception) {
			throw new IllegalStateException("Could not read or create config file " + CONFIG_PATH, exception);
		}
	}
}
