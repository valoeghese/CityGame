package nz.valoeghese;

import nz.valoeghese.render.ResourceLoader;
import nz.valoeghese.render.Screen;
import nz.valoeghese.render.gui.Button;
import nz.valoeghese.render.gui.CascadeButton;
import nz.valoeghese.render.gui.DropdownMenu;
import nz.valoeghese.render.WorldRenderer;
import nz.valoeghese.render.gui.GuiElement;
import nz.valoeghese.util.Logger;
import nz.valoeghese.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CityGame {
	public CityGame(Screen screen, MouseTracker tracker) {
		this.screen = screen;
		this.resourceLoader = new ResourceLoader();
		this.world = new World(new Random().nextLong());
		this.tracker = tracker;
		this.worldRenderer = new WorldRenderer(this.world, this.resourceLoader);

		// debug gui
		CascadeButton a = new CascadeButton(null, 40, 16, bx -> {});
		CascadeButton b = new CascadeButton(a, 35, 16, bx -> {});
		CascadeButton c = new CascadeButton(b, 30, 16, bx -> {});
		this.guiElements.add(new DropdownMenu(0, 0, a, b, c));
	}

	private final Screen screen;
	private final ResourceLoader resourceLoader;
	private final MouseTracker tracker;
	private final World world;
	private final WorldRenderer worldRenderer;
	private final List<GuiElement> guiElements = new ArrayList<>();

	private volatile boolean running;
	private long lastTick = System.currentTimeMillis();
	private long tickCount = 0;

	/**
	 * Called to run the main loop of the game.
	 */
	public void run() {
		this.running = true;

		while (this.running) {
			long time = System.currentTimeMillis();
			long ticks = (time - lastTick) / TICK_DELAY;

			int mouseX = this.tracker.getMouseX();
			int mouseY = this.tracker.getMouseY();

			while (ticks --> 0) {
				tick(mouseX, mouseY);
				tickCount++;
				lastTick = time;
			}

			render(mouseX, mouseY);
		}
	}

	public long getTickCount() {
		return this.tickCount;
	}

	private void render(int mouseX, int mouseY) {
		// draw world terrain
		this.worldRenderer.render(this.screen);

		// draw selected tile. we are only selecting tile if no gui in the way
		boolean selectedTerrain = true;
		for (GuiElement element : this.guiElements) {
			if (element.contains(mouseX, mouseY)) {
				selectedTerrain = false;
				break;
			}
		}

		if (selectedTerrain) {
			// pulse hovering item
			float sinusoid = (float)Math.sin((this.tickCount % 20) / 20.0f * 2 * Math.PI);
			this.screen.setColour(new Color(1.0f, 1.0f, 1.0f, 0.5f * sinusoid + 0.5f));
			int tileX = mouseX / 8;
			int tileY = mouseY / 8;
			this.screen.drawRect(tileX * 8, tileY * 8, 7, 7);
		}

		// draw gui
		for (GuiElement element : this.guiElements) {
			element.render(this.screen, mouseX, mouseY);
		}

		// done
		this.screen.swapBuffers();
	}

	private void tick(int mouseX, int mouseY) {
		// handle mouse events
		int clicks = this.tracker.consumeClicks();

		while (clicks --> 0) {
			for (GuiElement element : this.guiElements) {
				if (element.contains(mouseX, mouseY)) {
					element.mouseClicked(mouseX, mouseY);
				}
			}
		}

		// tick
		for (GuiElement element : this.guiElements) {
			element.tick();
		}
	}

	/** Called upon game shutdown. Clean up resources.
	 */
	public void shutdown() {
		this.running = false;
	}

	private static final long TICK_DELAY = 1000L / 20;
	private static final Logger LOGGER = new Logger("CityGame");
}
