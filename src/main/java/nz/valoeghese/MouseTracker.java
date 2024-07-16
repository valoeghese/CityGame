package nz.valoeghese;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;

public class MouseTracker extends MouseAdapter {
	MouseTracker(int scale) {
		this.scale = scale;
	}

	private final int scale;
	private volatile int mouseX;
	private volatile int mouseY;
	private final AtomicInteger clicks = new AtomicInteger(0);

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mouseX = e.getX() / scale;
		this.mouseY = (e.getY() - 48) / scale;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.clicks.incrementAndGet();
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public int consumeClicks() {
		return this.clicks.getAndSet(0);
	}
}
