package nz.valoeghese.render.gui;

import nz.valoeghese.render.Screen;

import java.awt.*;
import java.util.function.Consumer;

public class Button extends AbstractWidget {
	public Button(String message, int width, int height, Consumer<Button> onPress) {
		super(0, 0, width, height);
		this.width = width;
		this.height = height;
		this.onPress = onPress;
		this.message = message;
	}

	private String message;
	private final Consumer<Button> onPress;

	@Override
	public void render(Screen screen, int mouseX, int mouseY) {
		boolean hover = this.contains(mouseX, mouseY);

		screen.setColour(hover ? Color.LIGHT_GRAY : Color.DARK_GRAY);
		screen.drawRect(this.x, this.y, getWidth(), getHeight());

		screen.setColour(hover ? Color.WHITE : Color.LIGHT_GRAY);
		screen.write(this.message, this.x, this.y + (this.height + screen.fontHeight()) / 2);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY) {
		this.onPress.accept(this);
	}

	/**
	 * Move the button to the given position.
	 * @return this button.
	 */
	public Button move(int x, int y)  {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * @return the specified width of the button, without any animations.
	 */
	public final int getSpecifiedWidth() {
		return this.width;
	}

	/**
	 * @return the current actual width of the button.
	 */
	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * @return the specified height of the button, without any animations.
	 */
	public final int getSpecifiedHeight() {
		return this.height;
	}

	/**
	 * @return the current actual height of the button.
	 */
	@Override
	public int getHeight() {
		return this.height;
	}
}
