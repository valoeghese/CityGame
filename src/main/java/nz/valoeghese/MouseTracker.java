package nz.valoeghese;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;

public class MouseTracker extends MouseAdapter {
	private volatile int mouseX;
	private volatile int mouseY;
	private AtomicInteger clicks;

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mouseX = e.getX();
		this.mouseY = e.getY();
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
