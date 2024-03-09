package com.github.achaaab.retrocar;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroDigitGroup extends RetroComponent {

	private final int size;
	private final RetroDigit[] digits;

	/**
	 * @param screen
	 * @param x
	 * @param y
	 * @param size
	 * @since 0.0.0
	 */
	public RetroDigitGroup(RetroScreen screen, double x, double y, int size) {

		super(screen, x, y);

		this.size = size;

		digits = new RetroDigit[size];

		for (var digitIndex = 0; digitIndex < size; digitIndex++) {
			digits[digitIndex] = new RetroDigit(screen, x + 4 * digitIndex, y);
		}
	}

	/**
	 * @param value
	 * @since 0.0.0
	 */
	public void setValue(int value) {

		for (var digitIndex = size - 1; digitIndex >= 0; digitIndex--) {

			digits[digitIndex].setValue(value % 10);
			value /= 10;
		}
	}

	@Override
	public void draw(boolean pixel) {

		for (var digit : digits) {
			digit.draw(pixel);
		}
	}
}
