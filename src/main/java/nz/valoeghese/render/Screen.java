package nz.valoeghese.render;

import nz.valoeghese.MouseTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

/**
 * The game screen.
 */
public interface Screen {
	void draw(Image image, int x, int y);
	void setColour(Color colour);
	void drawOutline(int x, int y, int width, int height);
	void drawRect(int x, int y, int width, int height);
	void write(String text, int x, int y);
	void stencil(int x, int y, int width, int height);
	void endStencil();
	// width and height for drawing
	int width();
	int height();
	// swap buffers
	void swapBuffers();

	static Screen getScreen(JFrame frame, int scale) {
		PanelScreen screen = new PanelScreen(scale);
		frame.add(screen);
		return screen;
	}
}
