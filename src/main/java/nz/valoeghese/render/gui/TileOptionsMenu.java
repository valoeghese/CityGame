package nz.valoeghese.render.gui;

import nz.valoeghese.render.gui.widget.CascadeButton;
import nz.valoeghese.render.gui.widget.DropdownMenu;

/**
 * Menu that pops up when you click on a tile.
 */
public class TileOptionsMenu extends TopLevelMenu {
	public TileOptionsMenu(int x, int y) {
		super(x, y);
	}

	@Override
	protected void addChildren() {
		CascadeButton develop = new CascadeButton(null, "Develop",
				this.measurements.fontWidth("Develop") + 2, 16,
				bx -> this.cityGame.setTopLevelMenu(new DevelopLandMenu(this.x, this.y)));

		CascadeButton cancel = new CascadeButton(develop, "Cancel",
				this.measurements.fontWidth("Cancel") + 2, 16,
				bx -> this.close());

		this.children.add(new DropdownMenu(this.x, this.y, develop, cancel));
	}
}
