package com.github.achaaab.retrocar;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public abstract class RetroComponent {

	protected final RetroScreen screen;
	protected double x;
	protected double y;

	/**
	 * @param screen
	 * @param x
	 * @param y
	 * @since 0.0.0
	 */
	public RetroComponent(RetroScreen screen, double x, double y) {

		this.screen = screen;

		this.x = x;
		this.y = y;
	}

	/**
	 * Draws this component.
	 *
	 * @param pixel
	 * @since 0.0.0
	 */
	public abstract void draw(boolean pixel);

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
