package nz.valoeghese;

import nz.valoeghese.render.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame("City Game");
		Screen screen = Screen.getScreen(frame);

		CityGame game = new CityGame(screen);

		// Override default close operation
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				game.shutdown();
				System.exit(0); // Exit the application gracefully
			}
		});

		frame.setSize(new Dimension(720, 480));
		frame.setVisible(true);
		game.run();
	}
}
