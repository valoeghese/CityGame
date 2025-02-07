package nz.valoeghese.render.gui;

import nz.valoeghese.render.gui.widget.CascadeButton;
import nz.valoeghese.render.gui.widget.DropdownMenu;
import nz.valoeghese.world.Position;
import nz.valoeghese.world.Terrain;
import nz.valoeghese.world.object.FarmHouse;

/**
 * Menu that allows you to develop a land.
 */
public class DevelopLandMenu extends TopLevelMenu {
	public DevelopLandMenu(int x, int y, Terrain.Tile foundation) {
		super(x, y);
		this.foundation = foundation;
	}

	private final Terrain.Tile foundation;

	@Override
	protected void addChildren() {
		// Hardcoded for now. Once the building system is developed this will use that.
		if (this.foundation == Terrain.Tile.WATER) {
			CascadeButton cancel = new CascadeButton(null, "Cancel", this.measurements.fontWidth("Cancel") + 2, 16, bx -> this.close());
			this.children.add(new DropdownMenu(this.x, this.y, cancel));
		} else {
			CascadeButton housing = new CascadeButton(null, "Housing",
					this.measurements.fontWidth("Housing") + 8 + 2, 16,
					bx -> {
						// TODO housing options
						Position selected = this.cityGame.getSelectedTile();
						this.cityGame.getWorld().addObject(new FarmHouse(selected.x(), selected.y()));
						this.close();
					})
					.setIcon(this.resourceLoader.getTexture("build/farmhouse.png").getSubimage(0, 8, 8, 8));

			CascadeButton cancel = new CascadeButton(housing, "Cancel", this.measurements.fontWidth("Cancel") + 2, 16, bx -> this.close());
			this.children.add(new DropdownMenu(this.x, this.y, housing, cancel));
		}
	}
}
