package nz.valoeghese.render.gui;

import nz.valoeghese.render.Screen;

/**
 * A gui element that can be rendered and ticked.
 */
public interface GuiElement {
	default void tick() {
		// Do nothing
	}

	void render(Screen screen);
}
