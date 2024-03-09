package com.github.achaaab.retrocar;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroCarGame extends RetroGame {

	// minimum time between 2 rows of cars in the traffic
	private static final double MINIMUM_TRAFFIC_GAP = 15;

	// maximum time between 2 rows of cars in the traffic
	private static final double MAXIMUM_TRAFFIC_GAP = 22;

	// traffic speed
	private static final double TRAFFIC_SPPED = 14;

	private final Random random;
	private final int laneCount;
	private final RetroCar playerCar;
	private final RetroBorder border;
	private final Queue<RetroCar> traffic;
	private final WaveGenerator waveGenerator;

	private double playerCarSpeed;
	private double nextCarsDistance;

	private boolean accelerate;
	private boolean brake;

	/**
	 * @throws LineUnavailableException
	 * @since 0.0.0
	 */
	public RetroCarGame() throws LineUnavailableException {

		random = new Random();

		laneCount = 3;

		playerCarSpeed = 2;
		accelerate = false;
		brake = false;

		int screenHeight = screen.getHeight();

		playerCar = new RetroCar(laneCount == 2 ? 1 : 4, screenHeight - 4, screen);
		border = new RetroBorder(screen, 2, 6);
		traffic = new LinkedList<>();

		nextCarsDistance = 20;

		waveGenerator = new WaveGenerator();
		waveGenerator.setFrequency(getEngineSpeed(playerCarSpeed) / 10);
		new Thread(waveGenerator).start();
	}

	/**
	 * We roughly simulate an 8-gear, 600 to 9'000 RPM engine.
	 *
	 * @param carSpeed
	 * @return
	 */
	private double getEngineSpeed(double carSpeed) {

		double engineSpeed;

		if (carSpeed < 30) {

			// 1st gear
			engineSpeed = 9_000 * carSpeed / 30;

		} else if (carSpeed < 37) {

			// 2nd gear
			engineSpeed = 9_000 * carSpeed / 37;

		} else if (carSpeed < 47) {

			// 3rd gear
			engineSpeed = 9_000 * carSpeed / 47;

		} else if (carSpeed < 58) {

			// 4th gear
			engineSpeed = 9_000 * carSpeed / 58;

		} else if (carSpeed < 72) {

			// 5th gear
			engineSpeed = 9_000 * carSpeed / 72;

		} else if (carSpeed < 90) {

			// 6th gear
			engineSpeed = 9_000 * carSpeed / 90;

		} else if (carSpeed < 112) {

			// 7th gear
			engineSpeed = 9_000 * carSpeed / 112;

		} else {

			// 8th gear
			engineSpeed = 9_000 * carSpeed / 139;
		}

		return engineSpeed;
	}

	@Override
	public void update(Duration duration) {

		var engineSpeed = getEngineSpeed(playerCarSpeed);
		var engineForce = 20.0;
		var brakeForce = -30.0;
		var engineBrakeForce = -0.0001 * engineSpeed;
		var dragForce = -0.001 * playerCarSpeed * playerCarSpeed;

		var force = dragForce + engineBrakeForce;

		if (accelerate) {
			force += engineForce;
		} else if (brake) {
			force += brakeForce;
		}

		playerCarSpeed += force * duration.toSeconds();

		if (playerCarSpeed < 2) {
			playerCarSpeed = 2;
		}

		waveGenerator.setFrequency(getEngineSpeed(playerCarSpeed) / 8);

		var playerCarDistance = playerCarSpeed * duration.toSeconds();
		var trafficDistance = TRAFFIC_SPPED * duration.toSeconds();
		var relativeTrafficDistance = playerCarDistance - trafficDistance;

		border.move(playerCarDistance);
		moveCars(relativeTrafficDistance);

		nextCarsDistance -= relativeTrafficDistance;

		if (nextCarsDistance <= 0) {
			spawnCars();
		}

		removeCars();
	}

	/**
	 * @since 0.0.0
	 */
	private void moveCars(double distance) {

		for (RetroCar car : traffic) {
			car.move(0, distance);
		}
	}

	/**
	 * @since 0.0.0
	 */
	private void spawnCars() {

		int randomNumber;

		if (laneCount == 2) {

			randomNumber = 1 + random.nextInt(2);

			if ((randomNumber & 0b10) != 0) {
				traffic.offer(new RetroCar(1, -4, screen));
			}

			if ((randomNumber & 0b01) != 0) {
				traffic.offer(new RetroCar(4, -4, screen));
			}

		} else {

			randomNumber = 1 + random.nextInt(6);

			if ((randomNumber & 0b100) != 0) {
				traffic.offer(new RetroCar(1, -4, screen));
			}

			if ((randomNumber & 0b010) != 0) {
				traffic.offer(new RetroCar(4, -4, screen));
			}

			if ((randomNumber & 0b001) != 0) {
				traffic.offer(new RetroCar(7, -4, screen));
			}
		}

		nextCarsDistance = MINIMUM_TRAFFIC_GAP + Math.random() * (MAXIMUM_TRAFFIC_GAP - MINIMUM_TRAFFIC_GAP);
	}

	/**
	 * @since 0.0.0
	 */
	private void removeCars() {

		while (traffic.peek() != null && traffic.peek().getY() >= screen.getHeight()) {
			traffic.poll();
		}
	}

	@Override
	public void handle(KeyEvent keyEvent) {

		double playerCarX = playerCar.getX();

		KeyCode keyCode = keyEvent.getCode();
		boolean keyPressed = keyEvent.getEventType() == KeyEvent.KEY_PRESSED;

		switch (keyCode) {

		case LEFT:

			if (keyPressed) {

				if (laneCount == 2 && playerCarX != 2) {
					playerCar.move(-4, 0);
				} else if (laneCount == 3 && playerCarX != 1) {
					playerCar.move(-3, 0);
				}
			}

			break;

		case RIGHT:

			if (keyPressed) {

				if (laneCount == 2 && playerCarX != 6) {
					playerCar.move(+4, 0);
				} else if (laneCount == 3 && playerCarX != 7) {
					playerCar.move(+3, 0);
				}
			}

			break;

		case UP:

			accelerate = keyPressed;
			break;

		case DOWN:

			brake = keyPressed;
			break;

		default:
			break;
		}
	}
}
