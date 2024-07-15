package nz.valoeghese.util;

/**
 * Out of bounds array accessor.
 */
public class OOBAccessor<T> {
	public OOBAccessor(T[][] array, T oobValue) {
		this.array = array;
		this.oob = oobValue;
		this.h = array.length;
		this.w = array[0].length;
	}

	private final T[][] array;
	private final int h, w;
	private final T oob;

	public T get(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h) {
			return oob;
		}

		return array[x][y];
	}
}
