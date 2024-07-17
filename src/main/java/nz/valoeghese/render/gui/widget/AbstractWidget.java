package nz.valoeghese.render.gui.widget;

import nz.valoeghese.render.Screen;

import java.util.List;

/**
 * A GUI element that can be rendered and ticked.
 */
public abstract class AbstractWidget {
	protected AbstractWidget(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	protected int x, y, width, height;

	public void tick() {
		// Tick children
		for (AbstractWidget element : getChildren()) {
			element.tick();
		}
	}

	public void mouseClicked(int mouseX, int mouseY) {
		// Click children
		for (AbstractWidget element : getChildren()) {
			if (element.contains(mouseX, mouseY)) {
				element.mouseClicked(mouseX, mouseY);
			}
		}
	}

	public Iterable<AbstractWidget> getChildren() {
		return List.of();
	}

	// Dimensions
	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean contains(int x, int y) {
		return x >= getX() && y >= getY()
				&& x < getX() + getWidth() && y < getY() + getHeight();
	}

	// Render Function
	abstract public void render(Screen screen, int mouseX, int mouseY);
}
