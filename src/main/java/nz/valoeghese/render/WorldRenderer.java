package nz.valoeghese.render;

import nz.valoeghese.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldRenderer {
	public WorldRenderer(World world) {
		this.terrain = world.getTerrain().drawComposite();
	}

	private final BufferedImage terrain;

	public void render(Screen screen) {

	}
}
