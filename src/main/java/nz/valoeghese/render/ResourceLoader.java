package nz.valoeghese.render;

import nz.valoeghese.util.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResourceLoader {
	public ResourceLoader() {
	}

	private final Map<String, BufferedImage> cachedImages = new HashMap<>();

	public BufferedImage getEmptyTexture() {
		return EMPTY_TEXTURE;
	}

	public BufferedImage getMissingTexture() {
		return MISSING_TEXTURE;
	}

	public BufferedImage getTexture(String location) {
		return this.cachedImages.computeIfAbsent(location, ResourceLoader::loadTexture);
	}

	private static final Logger LOGGER = new Logger("Resource Loader");
	public static final BufferedImage MISSING_TEXTURE = loadTexture("missing.png");
	public static final BufferedImage EMPTY_TEXTURE = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);

	private static BufferedImage loadTexture(String location) {
		try (InputStream stream = new BufferedInputStream(openStream("assets/textures/" + location))) {
			return ImageIO.read(stream);
		} catch (IOException e) {
			if (location.equals("missing.png")) {
				throw new UncheckedIOException("Could not load missing.png", e);
			}

			LOGGER.error("Unable to read texture " + location, e);
			return MISSING_TEXTURE;
		}
	}

	public static InputStream openStream(String location) {
		return Objects.requireNonNull(ResourceLoader.class.getClassLoader().getResourceAsStream(location), "Could not load stream for " + location);
	}
}
