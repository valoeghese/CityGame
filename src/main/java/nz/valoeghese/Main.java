package nz.valoeghese;

import nz.valoeghese.render.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("City Game");
		final int scale = 2;

		MouseTracker tracker = new MouseTracker(scale);
		Screen screen = Screen.getScreen(frame, scale);

		CityGame game = new CityGame(screen, tracker);

		// Override default close operation
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				game.shutdown();
				System.exit(0);
			}
		});
		frame.addMouseListener(tracker);
		frame.addMouseMotionListener(tracker);

		frame.setSize(new Dimension(720 * scale, 480 * scale));
		frame.setResizable(false);
		frame.setVisible(true);
		game.run();
	}
}
