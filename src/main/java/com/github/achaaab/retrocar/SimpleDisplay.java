package com.github.achaaab.retrocar;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.animation.Animation.INDEFINITE;
import static javafx.util.Duration.seconds;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class SimpleDisplay extends Application {

	/**
	 * @param arguments none
	 * @since 0.0.0
	 */
	public static void main(String... arguments) {
		launch(arguments);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		var game = new RetroCarGame();
		var screen = game.getScreen();
		var gui = new RetroScreenGui(screen);
		var root = new Group(gui);
		var scene = new Scene(root);

		scene.setOnKeyPressed(game);
		scene.setOnKeyReleased(game);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Simple retroscreen display");
		primaryStage.show();

		var frameDuration = seconds(1.0 / 60.0);

		var keyFrame = new KeyFrame(frameDuration, (onFinished) -> {
			game.update(frameDuration);
			gui.draw();
		});

		var animation = new Timeline(keyFrame);
		animation.setCycleCount(INDEFINITE);
		animation.play();
	}

	@Override
	public void stop() {

		Platform.exit();
		System.exit(0);
	}
}
