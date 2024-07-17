package nz.valoeghese.render.gui;

import nz.valoeghese.render.gui.widget.CascadeButton;
import nz.valoeghese.render.gui.widget.DropdownMenu;

/**
 * Menu that allows you to develop a land.
 */
public class DevelopLandMenu extends TopLevelMenu {
	public DevelopLandMenu(int x, int y) {
		super(x, y);
	}

	@Override
	protected void addChildren() {
		CascadeButton housing = new CascadeButton(null, "Housing",
				this.measurements.fontWidth("Housing") + 8 + 2, 16,
				bx -> {})
				.setIcon(this.resourceLoader.getTexture("build/farmhouse.png").getSubimage(0, 8, 8, 8));

		CascadeButton cancel = new CascadeButton(housing, "Cancel", this.measurements.fontWidth("Cancel") + 2, 16, bx -> this.close());

		this.children.add(new DropdownMenu(this.x, this.y, housing, cancel));
	}
}
