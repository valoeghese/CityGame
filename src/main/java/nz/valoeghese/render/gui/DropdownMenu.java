package nz.valoeghese.render.gui;

import nz.valoeghese.render.Screen;

import java.util.stream.Stream;

public class DropdownMenu implements GuiElement {
	/**
	 * Create a new dropdown menu at the given position.
	 * @param x the x position to spawn the menu from.
	 * @param y the y position to spawn the menu from.
	 * @param buttons the buttons, from top to bottom, to put int he dropdown menu.
	 */
	public DropdownMenu(int x, int y, Button... buttons) {
		this.x = x;
		this.y = y;
		this.w = Stream.of(buttons).mapToInt(Button::getSpecifiedWidth).max().orElseThrow(() -> new RuntimeException("Empty dropdown menu"));
		this.h = Stream.of(buttons).mapToInt(Button::getSpecifiedHeight).sum() + ITEM_GAP * (buttons.length - 1);
		this.buttons = buttons;
	}

	private final int x;
	private final int y;
	private final int w, h;
	private final Button[] buttons;

	@Override
	public void tick() {
		for (Button button : this.buttons) {
			button.tick();
		}
	}

	@Override
	public void render(Screen screen) {
		// find position
		final int renderX;
		boolean drawBackwards;

		if (x + this.w + MENU_OFFSET > screen.width()) {
			renderX = x - MENU_OFFSET;
			drawBackwards = true;
		} else {
			renderX = x + MENU_OFFSET;
			drawBackwards = false;
		}

		int buttonY;

		if (y - MENU_OFFSET + this.h > screen.height()) {
			buttonY = y + MENU_OFFSET - this.h;
		} else {
			buttonY = Math.max(0, y - MENU_OFFSET);
		}

		// draw
		for (Button button : this.buttons) {
			int buttonX = drawBackwards ? renderX - button.getWidth() : renderX;
			button.move(buttonX, buttonY).render(screen);
			buttonY += button.getSpecifiedHeight() + ITEM_GAP;
		}
	}

	private static final int MENU_OFFSET = 8;
	private static final int ITEM_GAP = 2;
}
