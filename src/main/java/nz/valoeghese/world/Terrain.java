package nz.valoeghese.world;

import nz.valoeghese.util.Logger;
import nz.valoeghese.util.OOBAccessor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public class Terrain {
	private Terrain(Tile[][] tiles, OOBAccessor<Byte> heightmap, int width, int height) {
		this.tiles = tiles;
		this.heightmap = heightmap;
		this.width = width;
		this.height = height;
	}

	private final int width, height;
	public final Tile[][] tiles;
	public final OOBAccessor<Byte> heightmap;

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public static Terrain generate(long seed, int width, int height) {
		GradientNoise heightNoise = new GradientNoise(seed);
		GradientNoise hillNoise = new GradientNoise(seed);

		Byte[][] rawHeights = new Byte[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float h = heightNoise.noise(x / 45.0f, y / 45.0f);
				// fold around 0
				h = Math.abs(h);

				// stratiate
				if (h < 0.10f) {
					rawHeights[x][y] = 0;
				} else {
					// add hills detail
					h += 0.5f * Math.max(0, hillNoise.noise(x / 20.0f, y / 20.0f));

					if (h < 0.50f) {
						rawHeights[x][y] = 1;
					} else if (h < 0.65f) {
						rawHeights[x][y] = 2;
					} else {
						rawHeights[x][y] = 3;
					}
				}
			}
		}

		// turn into tile map
		Tile[][] tileMap = new Tile[width][height];
		OOBAccessor<Byte> heightmap = new OOBAccessor<>(rawHeights, (byte)-1);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				byte h = heightmap.get(x, y);

				if (h == 0) {
					tileMap[x][y] = Tile.WATER;
				} else {
					int bottom = heightmap.get(x, y+1);

					// are we low and adjacent to water & in beach zone
					if (h == 1 && hillNoise.noise(x / 20.0f, y / 20.0f) < -0.34f) {
						if (heightmap.get(x, y-1) == 0 || bottom == 0 || heightmap.get(x-1,y) == 0 || heightmap.get(x+1,y) == 0) {
							tileMap[x][y] = Tile.SAND;
							continue;
						}
					}

					if (bottom < h) {
						tileMap[x][y] = Tile.HILL;
					} else {
						tileMap[x][y] = Tile.GRASS;
					}
				}
			}
		}

		return new Terrain(tileMap, heightmap, width, height);
	}

	// each tile is an 8x8 area of the world, using a 16x16 texture
	public enum Tile {
		GRASS("grass.png"),
		HILL("hill.png"),
		ROCK("rock.png"),
		SAND("sand.png"),
		WATER("water.png");

		Tile(String imageLocation) {
			try (InputStream stream = new BufferedInputStream(
					Objects.requireNonNull(Terrain.class.getClassLoader().getResourceAsStream("assets/textures/" + imageLocation), "Could not load stream for " + imageLocation)
			)) {
				this.image = ImageIO.read(stream);
			} catch (IOException e) {
				throw new UncheckedIOException("Unable to load texture " + imageLocation, e);
			}
		}

		public final BufferedImage image;
	}

	private static final Logger LOGGER = new Logger("Worldgen");
}
