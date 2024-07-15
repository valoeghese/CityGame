package nz.valoeghese.render;

import nz.valoeghese.util.OOBAccessor;
import nz.valoeghese.world.Terrain;
import nz.valoeghese.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldRenderer {
	public WorldRenderer(World world) {
		this.terrain = drawComposite(world.getTerrain());
	}

	private final BufferedImage terrain;

	public void render(Screen screen) {
		screen.draw(this.terrain, 0, 0);
	}

	public static BufferedImage drawComposite(Terrain terrain) {
		int width = terrain.getWidth();
		int height = terrain.getHeight();

		// Create new image
		BufferedImage composite = new BufferedImage(width * 8, height * 8, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = composite.createGraphics();

		// stitch image
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Terrain.Tile tile = terrain.tiles[x][y];

				g.drawImage(
						tile.image.getSubimage((x & 1) * 8, (y & 1) * 8, 8, 8),
						x * 8, y * 8, null);
			}
		}

		// add shadows and highlights
		Color shadow = new Color(0, 30, 30, 64);
		Color highlight = new Color(200, 200, 200, 64);
		OOBAccessor<Byte> heightmap = terrain.heightmap;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int h = heightmap.get(x, y);

				// Shadow on right and bottom of hills
				if (heightmap.get(x-1, y) > h) {
					// left block is higher
					// draw shadow on left of block
					g.setColor(shadow);
					g.drawRect(x * 8, y * 8, 0, 7);
				}

				if (heightmap.get(x, y-1) > h) {
					// above block is higher
					// draw shadow on top of block
					g.setColor(shadow);
					g.drawRect(x * 8, y * 8, 7, 0);
				}

				// Highlight on top and right of hills
				// Default heightmap is -1 so this will never draw out of bounds as that's always lower
				if (heightmap.get(x, y+1) > h) {
					// top of next block
					g.setColor(highlight);
					g.drawRect(x * 8, (y+1) * 8, 7, 0);
				}

				if (heightmap.get(x+1, y) > h) {
					// right of previous block
					g.setColor(highlight);
					g.drawRect((x+1) * 8, y * 8, 0, 7);
				}
			}
		}

		return composite;
	}
}
