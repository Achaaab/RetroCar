package com.github.achaaab.retrocar;

import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import javax.sound.sampled.LineUnavailableException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.toIntExact;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

/**
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class RetroCarGame extends RetroGame {

	// minimum time between 2 rows of cars in the traffic
	private static final double MINIMUM_TRAFFIC_GAP = 30;

	// maximum time between 2 rows of cars in the traffic
	private static final double MAXIMUM_TRAFFIC_GAP = 100;

	// traffic speed
	private static final double TRAFFIC_SPEED = 35.0;

	private static final double MAX_ENGINE_SPEED = 9_000.0;
	private static final double[] MAX_SPEED_AT_GEAR = { 0.000, 30.00, 35.10, 41.07, 48.05, 56.22, 65.77, 76.95, 90.04 };
	private static final double[] MIN_SPEED_AT_GEAR = { 0.000, 0.000, 3.510, 4.107, 4.805, 5.622, 6.577, 7.695, 9.004 };

	private final Random random;
	private final int laneCount;
	private final RetroCar playerCar;
	private final RetroBorder border;
	private final Queue<RetroCar> traffic;
	private final WaveGenerator waveGenerator;
	private final RetroDigitGroup gearMeter;
	private final RetroDigitGroup speedMeter;
	private final RetroDigitGroup distanceMeter;
	private final RetroDigitGroup kiloMeter;

	private double playerCarSpeed;
	private int playerCarGear;
	private double nextCarsDistance;
	private double totalDistance;

	private boolean accelerate;
	private boolean brake;

	/**
	 * @throws LineUnavailableException
	 * @since 0.0.0
	 */
	public RetroCarGame() throws LineUnavailableException {

		random = new Random();

		laneCount = 3;

		playerCarSpeed = 0;
		playerCarGear = 1;

		accelerate = false;
		brake = false;

		int screenHeight = screen.getHeight();

		playerCar = new RetroCar(screen, laneCount == 2 ? 2 : 4, screenHeight - 4);
		gearMeter = new RetroDigitGroup(screen, 12, 1, 1);
		speedMeter = new RetroDigitGroup(screen, 12, 7, 3);
		kiloMeter = new RetroDigitGroup(screen, 12, 19, 3);
		distanceMeter = new RetroDigitGroup(screen, 12, 25, 3);
		border = new RetroBorder(screen, 0, 10, 2, 6);
		traffic = new LinkedList<>();

		totalDistance = 0;
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
	 * @since 0.0.0
	 */
	private double getEngineSpeed(double carSpeed) {

		while (carSpeed > MAX_SPEED_AT_GEAR[playerCarGear]) {
			playerCarGear++;
		}

		if (accelerate) {

			while (carSpeed < 0.9 * MAX_SPEED_AT_GEAR[playerCarGear - 1]) {
				playerCarGear--;
			}

		} else {

			while (carSpeed < MIN_SPEED_AT_GEAR[playerCarGear] && playerCarGear > 1) {
				playerCarGear--;
			}
		}

		return max(900.0, MAX_ENGINE_SPEED * carSpeed / MAX_SPEED_AT_GEAR[playerCarGear]);
	}

	@Override
	public void update(Duration duration) {

		var engineSpeed = getEngineSpeed(playerCarSpeed);
		var engineForce = 31.0 / pow(1.17, playerCarGear);
		var brakeForce = -40.0;
		var engineBrakeForce = -0.0001 * engineSpeed;
		var dragForce = -0.001 * playerCarSpeed * playerCarSpeed;

		var force = dragForce + engineBrakeForce;

		if (accelerate) {
			force += engineForce;
		} else if (brake) {
			force += brakeForce;
		}

		if (force > 12) {
			force = 12;
		} else if (force < -20) {
			force = -20;
		}

		playerCarSpeed += force * duration.toSeconds();

		if (playerCarSpeed < 0) {
			playerCarSpeed = 0;
		}

		waveGenerator.setFrequency(getEngineSpeed(playerCarSpeed) / 10);

		var playerCarDistance = playerCarSpeed * duration.toSeconds();
		var trafficDistance = TRAFFIC_SPEED * duration.toSeconds();
		var relativeTrafficDistance = playerCarDistance - trafficDistance;

		border.move(playerCarDistance);
		moveCars(relativeTrafficDistance);

		totalDistance += playerCarDistance;
		nextCarsDistance -= relativeTrafficDistance;

		if (nextCarsDistance <= 0) {
			spawnCars();
		}

		removeCars();

		gearMeter.setValue(playerCarGear);
		speedMeter.setValue(toIntExact(round(playerCarSpeed * 3.6)));
		kiloMeter.setValue(toIntExact(round(totalDistance / 1000)));
		distanceMeter.setValue(toIntExact(round(totalDistance)));
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
				traffic.offer(new RetroCar(screen, 2, -4));
			}

			if ((randomNumber & 0b01) != 0) {
				traffic.offer(new RetroCar(screen, 6, -4));
			}

		} else {

			randomNumber = 1 + random.nextInt(6);

			if ((randomNumber & 0b100) != 0) {
				traffic.offer(new RetroCar(screen, 1, -4));
			}

			if ((randomNumber & 0b010) != 0) {
				traffic.offer(new RetroCar(screen, 4, -4));
			}

			if ((randomNumber & 0b001) != 0) {
				traffic.offer(new RetroCar(screen, 7, -4));
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

		var playerCarX = playerCar.getX();

		var keyCode = keyEvent.getCode();
		var keyPressed = keyEvent.getEventType() == KEY_PRESSED;

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
