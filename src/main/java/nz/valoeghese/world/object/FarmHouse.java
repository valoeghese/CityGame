package nz.valoeghese.world.object;

import nz.valoeghese.render.ResourceLoader;

import java.awt.*;

public class FarmHouse extends GameObject {
	public FarmHouse(int x, int y) {
		super(x, y);
	}

	@Override
	public Image getImage(ResourceLoader loader, int x, int y) {
		return loader.getTexture("build/farmhouse.png").getSubimage((x & 1) * 8, (y & 1) * 8, 8, 8);
	}
}
