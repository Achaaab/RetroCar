package com.github.achaaab.retrocar;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroScreen {

	private final int width;
	private final int height;

	private final boolean[][] pixels;

	/**
	 * @param width
	 * @param height
	 * @since 0.0.0
	 */
	public RetroScreen(int width, int height) {

		this.width = width;
		this.height = height;

		pixels = new boolean[width][height];
	}

	/**
	 * @return width
	 * @since 0.0.0
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return height
	 * @since 0.0.0
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 * @since 0.0.0
	 */
	public boolean getPixel(int x, int y) {
		return pixels[x][y];
	}

	/**
	 * @param x
	 * @param y
	 * @param pixel
	 * @since 0.0.0
	 */
	public void setPixel(int x, int y, boolean pixel) {

		if (x >= 0 && x < width && y >= 0 && y < height) {
			pixels[x][y] = pixel;
		}
	}
}
