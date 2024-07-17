package nz.valoeghese;

import nz.valoeghese.render.ResourceLoader;
import nz.valoeghese.render.Screen;
import nz.valoeghese.render.gui.TileOptionsMenu;
import nz.valoeghese.render.gui.widget.CascadeButton;
import nz.valoeghese.render.gui.widget.DropdownMenu;
import nz.valoeghese.render.WorldRenderer;
import nz.valoeghese.render.gui.widget.AbstractWidget;
import nz.valoeghese.render.gui.TopLevelMenu;
import nz.valoeghese.util.Logger;
import nz.valoeghese.world.Position;
import nz.valoeghese.world.World;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CityGame {
	public CityGame(Screen screen, MouseTracker tracker) {
		this.screen = screen;
		this.resourceLoader = new ResourceLoader();
		this.world = new World(new Random().nextLong());
		this.tracker = tracker;
		this.worldRenderer = new WorldRenderer(this.world, this.resourceLoader);
	}

	private final Screen screen;
	private final ResourceLoader resourceLoader;
	private final MouseTracker tracker;
	private final World world;
	private final WorldRenderer worldRenderer;

	private final Queue<Runnable> tasks = new ArrayDeque<>();

	private volatile boolean running;
	private long lastTick = System.currentTimeMillis();
	private long tickCount = 0;
	private boolean selectingTerrain;

	private Position selectedTile = null;

	// guis that are rendering and ticking
	private final List<TopLevelMenu> renderingMenus = new ArrayList<>();
	// the currently open menu. this is the only interactable menu.
	private @Nullable TopLevelMenu topLevelMenu;
	private boolean newTopLevelMenu;

	public void setTopLevelMenu(@Nullable TopLevelMenu newMenu) {
		// close existing menu
		if (!this.renderingMenus.isEmpty()) {
			TopLevelMenu currentMenu = this.renderingMenus.getFirst();
			currentMenu.onClose().thenAccept(v -> this.enqueue(() -> this.renderingMenus.remove(currentMenu)));
		}

		this.topLevelMenu = newMenu;
		this.newTopLevelMenu = newMenu != null; // add the new menu to rendering menus

		// no menu implies no selected tile.
		if (newMenu == null) {
			this.selectTile(-1, -1);
		}
	}

	public @Nullable TopLevelMenu getTopLevelMenu() {
		return this.topLevelMenu;
	}

	public void selectTile(int tileX, int tileY) {
		this.selectedTile = new Position(tileX, tileY);
	}

	public World getWorld() {
		return this.world;
	}

	public @Nullable Position getSelectedTile() {
		return this.selectedTile;
	}

	public void enqueue(Runnable task) {
		synchronized (this.tasks) {
			this.tasks.add(task);
		}
	}

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

			this.selectingTerrain = this.computeSelectTerrain(mouseX, mouseY);
			render(mouseX, mouseY);

			while (ticks --> 0) {
				tick(mouseX, mouseY);
				tickCount++;
				lastTick = time;

				if (ticks > 0) this.selectingTerrain = this.computeSelectTerrain(mouseX, mouseY);
			}
		}
	}

	/**
	 * Compute whether the given mouse position is selecting the terrain.
	 * @param mouseX the mouse x on the screen.
	 * @param mouseY the mouse y on the screen.
	 * @return whether the given mouse x and y are selecting terrain.
	 */
	private boolean computeSelectTerrain(int mouseX, int mouseY) {
		// determine if we're selecting terrain
		// we are only selecting a tile if no gui in the way
		return this.topLevelMenu == null || !this.topLevelMenu.contains(mouseX, mouseY);
	}

	/**
	 * Get the ticks elapsed since start.
	 * @return the ticks elapsed since start.
	 */
	public long getTickCount() {
		return this.tickCount;
	}

	private void render(int mouseX, int mouseY) {
		// draw world terrain
		this.worldRenderer.render(this.screen);

		// draw selected tile
		if (this.selectedTile != null) {
			this.screen.setColour(new Color(1.0f, 1.0f, 1.0f, 0.75f));
			this.screen.drawRect(this.selectedTile.x() * 8, this.selectedTile.y() * 8, 7, 7);
		}

		// draw hovering tile
		if (this.selectingTerrain) {
			// pulse hovering item
			float sinusoid = (float)Math.sin((this.tickCount % 30) / 30.0f * 2 * Math.PI);
			this.screen.setColour(new Color(1.0f, 1.0f, 1.0f, 0.5f * sinusoid + 0.5f));
			int tileX = mouseX / 8;
			int tileY = mouseY / 8;
			this.screen.drawRect(tileX * 8, tileY * 8, 7, 7);
		}

		// draw gui
		for (TopLevelMenu menu : this.renderingMenus) {
			menu.render(this.screen, mouseX, mouseY);
		}

		// done
		this.screen.swapBuffers();
	}

	private void tick(int mouseX, int mouseY) {
		// handle mouse events
		int clicks = this.tracker.consumeClicks();

		while (clicks --> 0) {
			if (this.selectingTerrain) {
				this.selectTile(mouseX/8, mouseY/8);
				this.setTopLevelMenu(new TileOptionsMenu(mouseX/8 * 8, mouseY/8 * 8));
			} else for (AbstractWidget element : this.topLevelMenu.getChildren()) {
				if (element.contains(mouseX, mouseY)) {
					element.mouseClicked(mouseX, mouseY);
				}
			}
		}

		// tick
		for (TopLevelMenu menu : this.renderingMenus) {
			menu.tick();
		}

		// tasks
		synchronized (this.tasks) {
			Iterator<Runnable> tasks = this.tasks.iterator();

			while (tasks.hasNext()) {
				tasks.next().run();
				tasks.remove();
			}
		}

		// add new menu to rendering
		if (this.newTopLevelMenu) {
			this.newTopLevelMenu = false;
			this.topLevelMenu.init(this, this.screen, this.resourceLoader);
			this.renderingMenus.addFirst(this.topLevelMenu);
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
