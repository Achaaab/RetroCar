package com.github.achaaab.retrocar;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroBorder {

	private final RetroScreen screen;

	private final int on;
	private final int off;

	private double borderY;

	/**
	 * @param screen
	 * @param on
	 * @param off
	 * @since 0.0.0
	 */
	public RetroBorder(RetroScreen screen, int on, int off) {

		this.screen = screen;

		this.on = on;
		this.off = off;

		borderY = 0;

		draw();
	}

	/**
	 * @param dY
	 * @since 0.0.0
	 */
	public void move(double dY) {

		borderY += dY;

		draw();
	}

	/**
	 * @since 0.0.0
	 */
	private void draw() {

		int screenWidth = screen.getWidth();
		int screenHeight = screen.getHeight();

		boolean pixel;

		for (int y = 0; y < screenHeight; y++) {

			pixel = (borderY - y) % (on + off) < on;

			screen.setPixel(0, y, pixel);
			screen.setPixel(screenWidth - 1, y, pixel);
		}
	}
}
