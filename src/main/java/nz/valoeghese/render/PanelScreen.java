package nz.valoeghese.render;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelScreen extends JPanel implements Screen {
	PanelScreen(int scale) {
		this.scale = scale;

		this.drawContext = this.aGraphics = aFrame.createGraphics();
	}

	private int scale;
	private volatile boolean painting;

	private BufferedImage frame = new BufferedImage(720, 480, BufferedImage.TYPE_INT_ARGB);
	private BufferedImage aFrame = new BufferedImage(720, 480, BufferedImage.TYPE_INT_ARGB);
	private BufferedImage stencil;
	private final int[] stencilOffset = {0, 0};

	private Graphics2D aGraphics;
	private Graphics2D drawContext;

	@Override
	public void swapBuffers() {
		this.endStencil();

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

		this.drawContext = this.aGraphics = this.aFrame.createGraphics();
		painting = true;
		this.repaint();
	}

	@Override
	public void draw(Image image, int x, int y) {
		this.drawContext.drawImage(image, x - this.stencilOffset[0], y - this.stencilOffset[1], null);
	}

	@Override
	public void drawOutline(int x, int y, int width, int height) {
		this.drawContext.drawRect(x - this.stencilOffset[0], y - this.stencilOffset[1], width, height);
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		this.drawContext.fillRect(x - this.stencilOffset[0], y - this.stencilOffset[1], width, height);
	}

	@Override
	public void write(String text, int x, int y) {
		this.drawContext.drawString(text, x - this.stencilOffset[0], y - this.stencilOffset[1]);
	}

	@Override
	public void setColour(Color colour) {
		this.drawContext.setColor(colour);
	}

	@Override
	public void stencil(int x, int y, int width, int height) {
		if (this.stencil != null) {
			this.endStencil();
		}

		this.stencil = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		this.stencilOffset[0] = x;
		this.stencilOffset[1] = y;
		this.drawContext = this.stencil.createGraphics();
	}

	@Override
	public void endStencil() {
		if (this.stencil != null) {
			this.aGraphics.drawImage(this.stencil, this.stencilOffset[0], this.stencilOffset[1], null);
		}

		// revert to full screen
		this.drawContext = this.aGraphics;
		this.stencilOffset[0] = 0;
		this.stencilOffset[1] = 0;
		this.stencil = null;
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

	@Override
	public int fontWidth(String text) {
		return this.drawContext.getFontMetrics().stringWidth(text);
	}

	@Override
	public int fontHeight() {
		return this.drawContext.getFontMetrics().getAscent();
	}
}
