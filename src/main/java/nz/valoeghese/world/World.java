package nz.valoeghese.world;

public class World {
	public World(long seed) {
		this.terrain = Terrain.generate(seed, 720/8, 480/8);
	}

	private final Terrain terrain;

	public Terrain getTerrain() {
		return this.terrain;
	}
}
