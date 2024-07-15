package nz.valoeghese;

import nz.valoeghese.render.ResourceLoader;
import nz.valoeghese.render.Screen;
import nz.valoeghese.render.ToolbarOverlay;
import nz.valoeghese.render.WorldRenderer;
import nz.valoeghese.util.Logger;
import nz.valoeghese.world.World;

import java.util.Random;

public class CityGame {
	public CityGame(Screen screen) {
		this.screen = screen;
		this.resourceLoader = new ResourceLoader();
		this.world = new World(new Random().nextLong());
		this.worldRenderer = new WorldRenderer(this.world, this.resourceLoader);
		this.toolbar = new ToolbarOverlay(this.resourceLoader);
	}

	private final Screen screen;
	private final ResourceLoader resourceLoader;
	private final World world;
	private final WorldRenderer worldRenderer;
	private final ToolbarOverlay toolbar;
	private final Logger logger = new Logger("CityGame");

	private volatile boolean running;
	private long lastTick = System.currentTimeMillis();
	private long tickCount = 0;

	/** Called to run the main loop of the game.
	 */
	public void run() {
		this.running = true;

		while (this.running) {
			long time = System.currentTimeMillis();
			long ticks = (time - lastTick) / TICK_DELAY;

			while (ticks --> 0) {
				tick();
				tickCount++;
				lastTick = time;
			}

			render();
		}
	}

	public long getTickCount() {
		return this.tickCount;
	}

	private void render() {
		// draw world terrain
		this.worldRenderer.render(this.screen);
		// draw gui
		this.toolbar.render(this.screen);

		// done
		this.screen.swapBuffers();
	}

	private void tick() {
//		this.logger.info("tick count {}", getTickCount());
	}

	/** Called upon game shutdown. Clean up resources.
	 */
	public void shutdown() {
		this.running = false;
	}

	private static final long TICK_DELAY = 1000L / 20;
}
