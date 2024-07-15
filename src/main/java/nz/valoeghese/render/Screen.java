package nz.valoeghese.render;

import javax.swing.*;
import java.awt.*;

/**
 * The game screen.
 */
public interface Screen {
	void draw(Image image, int x, int y);
	// width and height for drawing
	int width();
	int height();
	void swapBuffers();

	static Screen getScreen(JFrame frame, int scale) {
		PanelScreen screen = new PanelScreen(scale);
		frame.add(screen);
		return screen;
	}
}
