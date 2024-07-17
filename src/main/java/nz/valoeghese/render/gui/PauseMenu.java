package nz.valoeghese.render.gui;

import nz.valoeghese.render.Screen;
import nz.valoeghese.render.gui.widget.CascadeButton;
import nz.valoeghese.render.gui.widget.DropdownMenu;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

/**
 * The pause menu.
 */
public class PauseMenu extends TopLevelMenu {
	public PauseMenu() {
		super(0, 0);
	}

	private int opacity;
	private CompletableFuture<Void> closing;

	@Override
	protected void addChildren() {
		CascadeButton resume = new CascadeButton(null, "Resume",
				this.measurements.fontWidth("Resume"), 20,
				bx -> this.close());

		CascadeButton options = new CascadeButton(resume, "Options",
				this.measurements.fontWidth("Options"), 20,
				bx -> {});

		CascadeButton exit = new CascadeButton(options, "Exit",
				this.measurements.fontWidth("Exit"), 20,
				bx -> {
					this.cityGame.shutdown();
					System.exit(0);
				});

		this.children.add(new DropdownMenu(this.measurements.width(), this.measurements.height(), resume, options, exit));
	}

	@Override
	public void tick() {
		if (this.closing == null) {
			this.opacity = Math.min(128 + 64, this.opacity + 32);
		} else {
			this.opacity = Math.max(0, this.opacity - 64);
			if (this.opacity == 0) {
				this.closing.complete(null);
			}
		}
		super.tick();
	}

	@Override
	public CompletableFuture<Void> onClose() {
		return this.closing = new CompletableFuture<>();
	}

	@Override
	public void render(Screen screen, int mouseX, int mouseY) {
		// draw overlay
		screen.setColour(new Color(50, 50, 50, this.opacity));
		screen.drawRect(0, 0, screen.width(), screen.height());
		// draw children
		if (this.closing == null) super.render(screen, mouseX, mouseY);
	}
}
