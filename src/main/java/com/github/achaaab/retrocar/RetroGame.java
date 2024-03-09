package com.github.achaaab.retrocar;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public abstract class RetroGame implements EventHandler<KeyEvent> {

	protected RetroScreen screen;

	/**
	 * @since 0.0.0
	 */
	public RetroGame() {
		screen = new RetroScreen(24, 40);
	}

	/**
	 * @param duration
	 * @since 0.0.0
	 */
	public abstract void update(Duration duration);

	/**
	 * @return screen
	 * @since 0.0.0
	 */
	public RetroScreen getScreen() {
		return screen;
	}
}
