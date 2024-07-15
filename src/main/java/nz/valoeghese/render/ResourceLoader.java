package nz.valoeghese.render;

import nz.valoeghese.world.Terrain;

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

	public InputStream openStream(String location) {
		return Objects.requireNonNull(ResourceLoader.class.getClassLoader().getResourceAsStream(location), "Could not load stream for " + location);
	}

	public BufferedImage getTexture(String location) {
		return this.cachedImages.computeIfAbsent(location, this::loadTexture);
	}

	private BufferedImage loadTexture(String location) {
		try (InputStream stream = new BufferedInputStream(openStream("assets/textures/" + location))) {
			return ImageIO.read(stream);
		} catch (IOException e) {
			throw new UncheckedIOException("Unable to read texture " + location, e);
		}
	}
}
