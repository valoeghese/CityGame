package nz.valoeghese.render;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelScreen extends JPanel implements Screen {
	PanelScreen(int scale) {
		this.scale = scale;
	}

	private int scale;

	private BufferedImage frame = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	private BufferedImage aFrame = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	private Graphics2D aGraphics = aFrame.createGraphics();

	@Override
	public void swapBuffers() {
		this.frame = this.aFrame;
		this.aFrame = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_ARGB);
		this.aGraphics = this.aFrame.createGraphics();
		this.repaint();
	}

	@Override
	public void draw(Image image, int x, int y) {
		this.aGraphics.drawImage(image, x, y, null);
	}

	@Override
	public void paint(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.drawImage(this.frame, 0, 0, getWidth(), getHeight(), null);
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
