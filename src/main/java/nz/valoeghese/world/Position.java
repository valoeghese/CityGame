package nz.valoeghese.world;

/**
 * A position on the tile grid.
 */
public record Position(int x, int y) {
	public long composite() {
		return ((long)x << 32) | y;
	}
}
