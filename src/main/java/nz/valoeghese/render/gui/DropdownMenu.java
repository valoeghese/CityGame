package nz.valoeghese.render.gui;

import nz.valoeghese.render.Screen;

import java.util.List;
import java.util.stream.Stream;

public class DropdownMenu extends AbstractWidget {
	/**
	 * Create a new dropdown menu at the given position.
	 * @param x the x position to spawn the menu from.
	 * @param y the y position to spawn the menu from.
	 * @param buttons the buttons, from top to bottom, to put int he dropdown menu.
	 */
	public DropdownMenu(int x, int y, Button... buttons) {
		super(x, y,
				Stream.of(buttons).mapToInt(Button::getSpecifiedWidth).max().orElseThrow(() -> new RuntimeException("Empty dropdown menu")),
				Stream.of(buttons).mapToInt(Button::getSpecifiedHeight).sum() + ITEM_GAP * (buttons.length - 1)
		);

		this.buttons = buttons;
	}

	private final Button[] buttons;

	@Override
	public void tick() {
		for (Button button : this.buttons) {
			button.tick();
		}
	}

	@Override
	public void render(Screen screen, int mouseX, int mouseY) {
		// find position
		final int renderX;
		boolean drawBackwards;

		if (x + this.width + MENU_OFFSET > screen.width()) {
			renderX = x - MENU_OFFSET;
			drawBackwards = true;
		} else {
			renderX = x + MENU_OFFSET;
			drawBackwards = false;
		}

		int buttonY;

		if (y - MENU_OFFSET + this.height > screen.height()) {
			buttonY = y + MENU_OFFSET - this.height;
		} else {
			buttonY = Math.max(0, y - MENU_OFFSET);
		}

		// draw
		for (Button button : this.buttons) {
			int buttonX = drawBackwards ? renderX - button.getWidth() : renderX;
			button.move(buttonX, buttonY).render(screen, mouseX, mouseY);
			buttonY += button.getSpecifiedHeight() + ITEM_GAP;
		}
	}

	@Override
	public Iterable<AbstractWidget> getChildren() {
		return List.of(this.buttons);
	}

	private static final int MENU_OFFSET = 8;
	private static final int ITEM_GAP = 2;
}
