package nz.valoeghese;

import nz.valoeghese.render.Screen;
import nz.valoeghese.util.Logger;
import nz.valoeghese.world.World;

import java.util.Random;

public class CityGame {
	public CityGame(Screen screen) {
		this.screen = screen;
		this.world = new World(new Random().nextLong());
	}

	private final Screen screen;
	private final World world;
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
