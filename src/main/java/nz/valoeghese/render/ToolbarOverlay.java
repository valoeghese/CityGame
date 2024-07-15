package nz.valoeghese.render;

import java.awt.image.BufferedImage;

public class ToolbarOverlay {
	public ToolbarOverlay(ResourceLoader loader) {
		this.toolbarBackground = loader.getTexture("gui/toolbaritem.png");
	}

	private final BufferedImage toolbarBackground;

	public void render(Screen screen) {
		int centre = screen.width() / 2;
		int height = screen.height() - 32;

		final int spacing = 16;
		int x = centre - 32 - spacing * 2;

		drawItem(screen, x, height);
		x += spacing + 32;

		drawItem(screen, x, height);
		x += spacing + 32;

		drawItem(screen, x, height);
		x += spacing + 32;

		drawItem(screen, x, height);
	}

	private void drawItem(Screen screen, int x, int y) {
		screen.draw(this.toolbarBackground, x - this.toolbarBackground.getWidth()/2, y);
	}
}
