package com.github.achaaab.retrocar;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroCar {

	private final RetroScreen screen;

	private double x;
	private double y;

	/**
	 * @param x
	 * @param y
	 * @param screen
	 * @since 0.0.0
	 */
	public RetroCar(double x, double y, RetroScreen screen) {

		this.x = x;
		this.y = y;

		this.screen = screen;

		draw(true);
	}

	/**
	 * @param dX
	 * @param dY
	 * @since 0.0.0
	 */
	public void move(double dX, double dY) {

		draw(false);

		x += dX;
		y += dY;

		draw(true);
	}

	/**
	 * @param pixel
	 * @since 0.0.0
	 */
	private void draw(boolean pixel) {

		int roundedX = (int) Math.round(x);
		int roundedY = (int) Math.round(y);
		
		screen.setPixel(roundedX, roundedY + 3, pixel);
		screen.setPixel(roundedX, roundedY + 1, pixel);
		screen.setPixel(roundedX + 1, roundedY + 2, pixel);
		screen.setPixel(roundedX + 1, roundedY + 1, pixel);
		screen.setPixel(roundedX + 1, roundedY + 0, pixel);
		screen.setPixel(roundedX + 2, roundedY + 3, pixel);
		screen.setPixel(roundedX + 2, roundedY + 1, pixel);
	}

	/**
	 * @return x
	 * @since 0.0.0
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return y
	 * @since 0.0.0
	 */
	public double getY() {
		return y;
	}
}
