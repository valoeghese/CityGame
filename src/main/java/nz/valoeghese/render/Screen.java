package nz.valoeghese.render;

import javax.swing.*;
import java.awt.*;

/**
 * The game screen.
 */
public interface Screen {
	void draw(Image image, int x, int y);
	int width();
	int height();
	void swapBuffers();

	static Screen getScreen(JFrame frame) {
		PanelScreen screen = new PanelScreen();
		frame.add(screen);
		return screen;
	}
}
