package nz.valoeghese.render;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelScreen extends JPanel implements Screen {
	PanelScreen(int scale) {
		this.scale = scale;
	}

	private int scale;
	private volatile boolean painting;

	private BufferedImage frame = new BufferedImage(720, 480, BufferedImage.TYPE_INT_ARGB);
	private BufferedImage aFrame = new BufferedImage(720, 480, BufferedImage.TYPE_INT_ARGB);
	private Graphics2D aGraphics = aFrame.createGraphics();

	@Override
	public void swapBuffers() {
		if (painting) {
			return; // skip frame. we are still drawing previous frame!
		}

		BufferedImage oldFrame = this.frame;
		this.frame = this.aFrame;

		if (oldFrame.getWidth() != width() || oldFrame.getHeight() != height()) {
			this.aFrame = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_ARGB);
		} else {
			this.aFrame = oldFrame;
		}

		this.aGraphics = this.aFrame.createGraphics();
		painting = true;
		this.repaint();
	}

	@Override
	public void draw(Image image, int x, int y) {
		this.aGraphics.drawImage(image, x, y, null);
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		this.aGraphics.drawRect(x, y, width, height);
	}

	@Override
	public void setColour(Color colour) {
		this.aGraphics.setColor(colour);
	}

	@Override
	public void paint(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.drawImage(this.frame, 0, 0, getWidth(), getHeight(), null);
		painting = false;
	}

	@Override
	public int width() {
		return this.getWidth() / scale;
	}

	@Override
	public int height() {
		return this.getHeight() / scale;
	}
}
