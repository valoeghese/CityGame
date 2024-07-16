package nz.valoeghese.render.gui;

import nz.valoeghese.render.Screen;

import java.util.List;

/**
 * A gui element that can be rendered and ticked.
 */
public interface GuiElement {
	default void tick() {
		// Tick children
		for (GuiElement element : getChildren()) {
			element.tick();
		}
	}

	default void mouseClicked(int mouseX, int mouseY) {
		// Click children
		for (GuiElement element : getChildren()) {
			if (element.contains(mouseX, mouseY)) {
				element.mouseClicked(mouseX, mouseY);
			}
		}
	}

	default Iterable<GuiElement> getChildren() {
		return List.of();
	}

	// Dimensions
	int getX();
	int getY();
	int getWidth();
	int getHeight();

	default boolean contains(int x, int y) {
		return x >= getX() && y >= getY()
				&& x < getX() + getWidth() && y < getY() + getHeight();
	}

	// Render Function
	void render(Screen screen, int mouseX, int mouseY);
}
