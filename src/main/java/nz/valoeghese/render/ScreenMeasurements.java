package nz.valoeghese.render;

public interface ScreenMeasurements {
	// width and height for drawing
	int width();
	int height();
	int fontWidth(String text);
	int fontHeight();
}
