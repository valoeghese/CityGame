package nz.valoeghese.render;

import javax.swing.*;

/**
 * The game screen.
 */
public interface Screen {
	static Screen getScreen(JFrame frame) {
		PanelScreen screen = new PanelScreen();
		frame.add(screen);
		return screen;
	}
}
