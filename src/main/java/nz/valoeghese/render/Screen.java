package nz.valoeghese.render;

import javax.swing.*;
import java.awt.*;

/**
 * The game screen.
 */
public interface Screen {
	void drawImage(Image image, int x, int y);
	void setColour(Color colour);
	void drawOutline(int x, int y, int width, int height);
	void drawRect(int x, int y, int width, int height);
	void write(String text, int x, int y);
	void stencil(int x, int y, int width, int height);
	void endStencil();
	// width and height for drawing
	int width();
	int height();
	int fontWidth(String text);
	int fontHeight();
	// swap buffers
	void swapBuffers();

	static Screen getScreen(JFrame frame, int scale) {
		PanelScreen screen = new PanelScreen(scale);
		frame.add(screen);
		return screen;
	}
}
