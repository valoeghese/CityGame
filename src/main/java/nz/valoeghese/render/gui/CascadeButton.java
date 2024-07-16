package nz.valoeghese.render.gui;

import nz.valoeghese.render.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Button in a chain that cascade-opens.
 */
public class CascadeButton extends Button {
	public CascadeButton(@Nullable CascadeButton previous, String message, int width, int height, Consumer<Button> onPress) {
		super(message, width, height, onPress);

		// assign cascade
		if (previous == null) {
			cascading = true;
		} else {
			previous.next = this;
		}
	}

	private @Nullable CascadeButton next;
	private boolean cascading;
	private int actualWidth = 0;

	@Override
	public void tick() {
		if (cascading && actualWidth < getSpecifiedWidth()) {
			int advance = Math.max((getSpecifiedWidth() - actualWidth) / 3, 2);
			actualWidth = Math.min(getSpecifiedWidth(), actualWidth + advance);

			// at 3/5 start next button cascading
			if (next != null && actualWidth >= getSpecifiedWidth()*3/5) {
				next.cascading = true;
			}
		}
	}

	@Override
	public void render(Screen screen, int mouseX, int mouseY) {
		screen.stencil(this.x, this.y, Math.max(1, this.getWidth()), this.getHeight());
		super.render(screen, mouseX, mouseY);
		screen.endStencil();
	}

	@Override
	public int getWidth() {
		return actualWidth;
	}
}
