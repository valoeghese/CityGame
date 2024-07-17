package nz.valoeghese;

import nz.valoeghese.render.gui.PauseMenu;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyTracker extends KeyAdapter {
	public KeyTracker(CityGame game) {
		this.game = game;
	}

	private final CityGame game;

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			// usually, close the existing menu
			// if no menu is already open, open pause menu.
			if (this.game.getTopLevelMenu() == null) {
				this.game.setTopLevelMenu(new PauseMenu());
			} else {

				this.game.setTopLevelMenu(null);
			}
		}
	}
}
