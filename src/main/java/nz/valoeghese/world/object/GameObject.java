package nz.valoeghese.world.object;

import nz.valoeghese.render.ResourceLoader;

import java.awt.*;

public abstract class GameObject {
	public GameObject(int x, int y) {
		this(x, y, 1, 1);
	}

	public GameObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public final int x, y, width, height;

	abstract public Image getImage(ResourceLoader loader, int x, int y);
}
