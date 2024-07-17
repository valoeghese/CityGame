package nz.valoeghese.render;

import nz.valoeghese.util.OOBAccessor;
import nz.valoeghese.world.Position;
import nz.valoeghese.world.Terrain;
import nz.valoeghese.world.World;
import nz.valoeghese.world.object.GameObject;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class WorldRenderer {
	public WorldRenderer(World world, ResourceLoader resourceLoader) {
		this.world = world;
		this.resourceLoader = resourceLoader;

		this.terrain = drawComposite(world.getTerrain(), resourceLoader);
		this.objects = new BufferedImage(this.terrain.getWidth(), this.terrain.getHeight(), BufferedImage.TYPE_INT_ARGB);
	}

	private final World world;
	private final ResourceLoader resourceLoader;
	private final BufferedImage terrain;
	private final BufferedImage objects;

	public void render(Screen screen) {
		if (!world.dirtyObjects.isEmpty()) {
			this.updateObjects(this.world, this.resourceLoader);
		}
		screen.drawImage(this.terrain, 0, 0);
		screen.drawImage(this.objects, 0, 0);
	}

	public void updateObjects(World world, ResourceLoader loader) {
		Graphics2D graphics2D = this.objects.createGraphics();
		Iterator<Position> iterator = world.dirtyObjects.iterator();

		while (iterator.hasNext()) {
			Position position = iterator.next();
			iterator.remove();

			@Nullable GameObject object = world.getObject(position.x(), position.y());

			if (object == null) {
				graphics2D.clearRect(object.x, object.y, 7, 7);
			} else {
				Image image = object.getImage(loader, position.x(), position.y());
				int w = image.getHeight(null);
				int h = image.getWidth(null);
				if (w > 8 || h > 8) {
					throw new RuntimeException("Image too large (" + w + "x" + h + ") for object " + object);
				}

				graphics2D.drawImage(image, position.x() * 8, position.y() * 8, null);
			}
		}
	}

	public static BufferedImage drawComposite(Terrain terrain, ResourceLoader resourceLoader) {
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
						resourceLoader.getTexture(tile.imageLocation).getSubimage((x & 1) * 8, (y & 1) * 8, 8, 8),
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
					g.drawRect(x * 8, y * 8, 1, 7);
				}

				if (heightmap.get(x, y-1) > h) {
					// above block is higher
					// draw shadow on top of block
					g.setColor(shadow);
					g.drawRect(x * 8, y * 8, 7, 1);
				}

				// Highlight on top and right of hills
				// Default heightmap is -1 so this will never draw out of bounds as that's always lower
				if (heightmap.get(x, y+1) > h) {
					// top of next block
					g.setColor(highlight);
					g.drawRect(x * 8, (y+1) * 8, 7, 1);
				}

				if (heightmap.get(x+1, y) > h) {
					// right of previous block
					g.setColor(highlight);
					g.drawRect((x+1) * 8, y * 8, 1, 7);
				}
			}
		}

		return composite;
	}
}
