package com.github.achaaab.retrocar;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroBorder extends RetroComponent {

	private final int x0;
	private final int x1;

	private final int on;
	private final int off;

	/**
	 * @param screen
	 * @param x0
	 * @param x1
	 * @param on
	 * @param off
	 * @since 0.0.0
	 */
	public RetroBorder(RetroScreen screen, int x0, int x1, int on, int off) {

		super(screen, x0, 0);

		this.x0 = x0;
		this.x1 = x1;

		this.on = on;
		this.off = off;

		draw(true);
	}

	/**
	 * @param dY
	 * @since 0.0.0
	 */
	public void move(double dY) {

		y += dY;
		draw(true);
	}

	@Override
	public void draw(boolean pixel) {

		int screenHeight = screen.getHeight();

		for (var y = 0; y < screenHeight; y++) {

			pixel = (this.y - y) % (on + off) < on;

			screen.setPixel(x0, y, pixel);
			screen.setPixel(x1, y, pixel);
		}
	}
}
