package nz.valoeghese.render.gui;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Button in a chain that cascade-opens.
 */
public class CascadeButton extends Button {
	public CascadeButton(@Nullable CascadeButton previous, int width, int height, Consumer<Button> onPress) {
		super(width, height, onPress);

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
			int advance = Math.max((getSpecifiedWidth() - actualWidth) / 5, 2);
			actualWidth = Math.min(getSpecifiedWidth(), actualWidth + advance);

			// at halfway start next button cascading
			if (next != null && actualWidth >= getSpecifiedWidth()/2) {
				next.cascading = true;
			}
		}
	}

	@Override
	public int getWidth() {
		return actualWidth;
	}
}
