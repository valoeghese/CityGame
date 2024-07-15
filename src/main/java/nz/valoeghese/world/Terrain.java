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

	public BufferedImage drawComposite() {
		// Create new image
		BufferedImage composite = new BufferedImage(this.width * 8, this.height * 8, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = composite.createGraphics();

		// stitch image
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Tile tile = this.tiles[x][y];

				g.drawImage(
						tile.image.getSubimage((x & 1) * 8, (y & 1) * 8, 8, 8),
						x * 8, y * 8, null);
			}
		}

		// add shadows and highlights
		Color shadow = new Color(0, 30, 30, 64);
		Color highlight = new Color(200, 200, 200, 64);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int h = this.heightmap.get(x, y);

				// Shadow on right and bottom of hills
				if (heightmap.get(x-1, y) > h) {
					// left block is higher
					// draw shadow on left of block
					g.setColor(shadow);
					g.drawRect(x * 8, y * 8, 1, 8);
				}

				if (heightmap.get(x, y-1) > h) {
					// above block is higher
					// draw shadow on top of block
					g.setColor(shadow);
					g.drawRect(x * 8, y * 8, 8, 1);
				}

				// Highlight on top and right of hills
				// Default heightmap is -1 so this will never draw out of bounds as that's always lower
				if (heightmap.get(x, y+1) > h) {
					// top of next block
					g.setColor(highlight);
					g.drawRect(x * 8, (y+1) * 8, 8, 1);
				}

				if (heightmap.get(x+1, y) > h) {
					// right of previous block
					g.setColor(highlight);
					g.drawRect((x+1) * 8, y * 8, 1, 8);
				}
			}
		}

		return composite;
	}

	public static Terrain generate(long seed, int width, int height) {
		GradientNoise heightNoise = new GradientNoise(seed);
		//GradientNoise hillsNoise = new GradientNoise(seed+1);

		Byte[][] rawHeights = new Byte[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float h = heightNoise.noise(x / 20.0f, y / 20.0f);
				// fold around 0
				h = Math.abs(h);
				// stratiate
				if (h < 0.15f) {
					rawHeights[x][y] = 0;
				} else if (h < 0.40f) {
					rawHeights[x][y] = 1;
				} else if (h < 0.55f) {
					rawHeights[x][y] = 2;
				} else {
					rawHeights[x][y] = 3;
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

					// are we low and adjacent to water
					if (h == 1) {
						if (heightmap.get(x, y-1) == 0 || bottom == 0 || heightmap.get(x-1,y) == 0 || heightmap.get(x+1,y) == 0) {
							tileMap[x][y] = Tile.SAND;
						} else {
							tileMap[x][y] = Tile.GRASS;
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
