package com.github.achaaab.retrocar;

import static java.lang.Math.round;
import static java.lang.Math.toIntExact;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroCar extends RetroComponent {

	/**
	 * @param screen
	 * @param x
	 * @param y
	 * @since 0.0.0
	 */
	public RetroCar(RetroScreen screen, double x, double y) {

		super(screen, x, y);

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

	@Override
	public void draw(boolean pixel) {

		var roundedX = toIntExact(round(x));
		var roundedY = toIntExact(round(y));

		screen.setPixel(roundedX, roundedY + 3, pixel);
		screen.setPixel(roundedX, roundedY + 1, pixel);
		screen.setPixel(roundedX + 1, roundedY + 2, pixel);
		screen.setPixel(roundedX + 1, roundedY + 1, pixel);
		screen.setPixel(roundedX + 1, roundedY, pixel);
		screen.setPixel(roundedX + 2, roundedY + 3, pixel);
		screen.setPixel(roundedX + 2, roundedY + 1, pixel);
	}
}
