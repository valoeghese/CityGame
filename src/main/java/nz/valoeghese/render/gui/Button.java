package nz.valoeghese.render.gui;

import nz.valoeghese.render.Screen;

import java.awt.*;
import java.util.function.Consumer;

public class Button implements GuiElement {
	public Button(int width, int height, Consumer<Button> onPress) {
		this.width = width;
		this.height = height;
		this.onPress = onPress;
	}

	private int x;
	private int y;
	private final int width;
	private final int height;
	private final Consumer<Button> onPress;

	@Override
	public void render(Screen screen, int mouseX, int mouseY) {
		screen.setColour(Color.BLACK);
		screen.drawRect(this.x, this.y, getWidth(), getHeight());
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

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}
}
