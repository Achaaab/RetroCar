package com.github.achaaab.retrocar;

import static java.lang.Math.round;
import static java.lang.Math.toIntExact;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroDigit extends RetroComponent {

	private static final String FONT = """
			  ⬛        ⬛      ⬛ ⬛ ⬛    ⬛ ⬛ ⬛    ⬛   ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛
			⬛   ⬛    ⬛ ⬛          ⬛        ⬛    ⬛   ⬛    ⬛        ⬛        ⬛   ⬛    ⬛   ⬛    ⬛   ⬛
			⬛   ⬛      ⬛      ⬛ ⬛ ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛        ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛
			⬛   ⬛      ⬛      ⬛            ⬛        ⬛        ⬛    ⬛   ⬛        ⬛    ⬛   ⬛        ⬛
			  ⬛      ⬛ ⬛ ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛        ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛        ⬛    ⬛ ⬛ ⬛    ⬛ ⬛ ⬛
						""";

	private int value;

	/**
	 * @param screen
	 * @param x
	 * @param y
	 * @since 0.0.0
	 */
	public RetroDigit(RetroScreen screen, double x, double y) {

		super(screen, x, y);

		setValue(0);
	}

	/**
	 * @param value
	 * @since 0.0.0
	 */
	public void setValue(int value) {

		this.value = value;

		draw(true);
	}

	@Override
	public void draw(boolean pixel) {

		var roundedX = toIntExact(round(x));
		var roundedY = toIntExact(round(y));

		for (var pixelX = 0; pixelX < 3; pixelX++) {
			for (var pixelY = 0; pixelY < 5; pixelY++) {

				var fontIndex = value * 9 + pixelX * 2 + pixelY * 87;
				screen.setPixel(roundedX + pixelX, roundedY + pixelY, pixel && FONT.charAt(fontIndex) == '⬛');
			}
		}
	}
}
