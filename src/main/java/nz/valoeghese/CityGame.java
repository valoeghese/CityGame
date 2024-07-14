package nz.valoeghese;

import nz.valoeghese.render.Screen;
import nz.valoeghese.util.Logger;

public class CityGame {
	public CityGame(Screen screen) {
		this.screen = screen;
	}

	private final Screen screen;
	private final Logger logger = new Logger("CityGame");

	/** Called to run the main loop of the game.
	 */
	public void run() {
		while (true) {
			logger.info("hi");
			logger.warn("oops");
		}
	}

	/** Called upon game shutdown. Clean up resources.
	 */
	public void shutdown() {

	}
}
