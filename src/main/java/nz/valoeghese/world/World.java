package nz.valoeghese.world;

import nz.valoeghese.world.object.GameObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class World {
	public World(long seed) {
		this.terrain = Terrain.generate(seed, 720/8, 480/8);
		this.objects = new GameObject[this.terrain.getWidth()][this.terrain.getHeight()];
	}

	private final Terrain terrain;
	private final GameObject[][] objects;
	public List<Position> dirtyObjects; // used by the renderer to know whether it should update the objects layer

	public Terrain getTerrain() {
		return this.terrain;
	}

	public void addObject(GameObject object) {
		for (int dx = 0; dx < object.width; dx++) {
			int x = object.x + dx;

			for (int dy = 0; dy < object.height; dy++) {
				int y = object.y + dy;

				this.objects[x][y] = object;
				this.dirtyObjects.add(new Position(x, y));
			}
		}
	}

	public void removeObject(GameObject object) {
		for (int dx = 0; dx < object.width; dx++) {
			int x = object.x + dx;

			for (int dy = 0; dy < object.height; dy++) {
				int y = object.y + dy;

				this.objects[x][y] = null;
				this.dirtyObjects.add(new Position(x, y));
			}
		}
	}

	public @Nullable GameObject getObject(int x, int y) {
		return this.objects[x][y];
	}
}
