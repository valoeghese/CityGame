package nz.valoeghese.render.gui;

import nz.valoeghese.CityGame;
import nz.valoeghese.render.ResourceLoader;
import nz.valoeghese.render.Screen;
import nz.valoeghese.render.ScreenMeasurements;
import nz.valoeghese.render.gui.widget.AbstractWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A top-level menu. Only one can be open at a time.
 */
public abstract class TopLevelMenu extends AbstractWidget {
	public TopLevelMenu(int x, int y) {
		// width and height are ignored by for contains calls
		super(x, y, 0, 0);
	}

	protected CityGame cityGame;
	protected ScreenMeasurements measurements;
	protected ResourceLoader resourceLoader;
	protected final List<AbstractWidget> children = new ArrayList<>();

	/**
	 * Called when this menu becomes the top level menu.
	 */
	public void init(CityGame game, ScreenMeasurements measurements, ResourceLoader resourceLoader) {
		this.cityGame = game;
		this.measurements = measurements;
		this.resourceLoader = resourceLoader;
		this.addChildren();
	}

	protected abstract void addChildren();

	/**
	 * Called when this top level item is closed.
	 * @return a completable future that completes when this menu can stop being rendered.
	 */
	public CompletableFuture<Void> onClose() {
		return CompletableFuture.completedFuture(null);
	}

	protected final void close() {
		cityGame.setTopLevelMenu(null);
	}

	@Override
	public void render(Screen screen, int mouseX, int mouseY) {
		for (AbstractWidget widget : this.children) {
			widget.render(screen, mouseX, mouseY);
		}
	}

	@Override
	public final List<AbstractWidget> getChildren() {
		return this.children;
	}

	@Override
	public final boolean contains(int x, int y) {
		for (AbstractWidget widget : this.children) {
			if (widget.contains(x, y)) {
				return true;
			}
		}

		return false;
	}
}
