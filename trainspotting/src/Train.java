import TSim.*;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Train implements Runnable {

	private int id, speed;
	private int dir = 1; // 1 for down -1 for up
	private TSimInterface tsi;
	private SensorEvent event;
	private volatile Semaphore[] sem;
	public int lastSem;

	public Train(int id, int speed, TSimInterface tsi, Semaphore[] sem) {

		this.id = id;
		this.speed = speed;
		this.tsi = tsi;
		this.sem = sem;

		if (id == 2) {
			dir = -1;
		}
		try {

			tsi.setSpeed(id, speed);

		} catch (CommandException e) {
			e.printStackTrace(); // or only e.getMessage() for the error
			System.exit(1);
		}
	}

	public void run() {

		while (true) {

			try {

				event = tsi.getSensor(id);

//				firstStopUp 
//				sensor aboveFirstStop
				if (trainAt(14, 3)) {
					
					tsi.setSpeed(id, 0);
					
					if (dir == 1) {
//						semaphore for firstStopUp Lane
						sem[0].acquire();
						tsi.setSpeed(id, speed);

					}
					if (dir == -1) {
						StopAndChangeDirection();
					}
				}
//				sensor leftOfJunction
				if (trainAt(6, 5)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
//						acquire the Junction semaphore
						sem[2].acquire();
						tsi.setSpeed(id, speed);

					}
					if (dir == -1) {
//						relese junction semaphore
						sem[2].release();
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(11, 7)) {

					tsi.setSpeed(id, 0);
					if (dir == 1) {
//						relese junction smaphore
						sem[2].release();
						tsi.setSpeed(id, speed);

					}
					if (dir == -1) {
//						acquire junction semaphore
						sem[2].acquire();
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(15, 7)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
//						acquire 1stSingleLane semaphore and release firstStopLane
						sem[3].acquire();
						sem[0].release();
						if (sem[4].tryAcquire()) {
							lastSem = 4;
							tsi.setSwitch(17, 7, 2);
							tsi.setSwitch(15, 9, 2);
							tsi.setSpeed(id, speed);
						} else {
							sem[5].acquire();
							lastSem = 5;
							tsi.setSwitch(17, 7, 2);
							tsi.setSwitch(15, 9, 1);
							tsi.setSpeed(id, speed);
						}
					}
					if (dir == -1) {

						sem[3].release();
//						have to release sem4 or sem5
						if (lastSem == 4) {
							sem[4].release();
						} else
							sem[5].release();
						tsi.setSpeed(id, speed);

					}
				}

				if (trainAt(14, 5)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						sem[1].acquire();
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						StopAndChangeDirection();
					}
				}
				if (trainAt(8, 5)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						sem[2].acquire();
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						sem[2].release();
						tsi.setSpeed(id, speed);Exception in thread "Thread-1" java.lang.NullPointerException
					}
				}
				if (trainAt(11, 8)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						sem[2].release();
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						sem[2].acquire();
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(15, 8)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
//							acquire 1stSingleLane semaphore and release firstStopLane
						sem[3].acquire();
						sem[1].release();
						if (sem[4].tryAcquire()) {
							lastSem = 4;
							System.out.println("yo");
							tsi.setSwitch(17, 7, 1);
							tsi.setSwitch(15, 9, 2);
							tsi.setSpeed(id, speed);
						} else {
							sem[5].acquire();
							lastSem = 5;
							tsi.setSwitch(17, 7, 1);
							tsi.setSwitch(15, 9, 1);
							tsi.setSpeed(id, speed);
						}

					}
					if (dir == -1) {
						sem[3].release();

						if (lastSem == 4) {
							sem[4].release();
						} else
							sem[5].release();
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(13, 9)) {
					tsi.setSpeed(id, 0);

					if (dir == -1) {
						sem[3].acquire();
						if (sem[0].tryAcquire()) {
							lastSem = 0;
							tsi.setSwitch(15, 9, 2);
							tsi.setSwitch(17, 7, 2);
							tsi.setSpeed(id, speed);
						} else {
							sem[1].acquire();
							lastSem = 1;
							tsi.setSwitch(15, 9, 2);
							tsi.setSwitch(17, 7, 1);
							tsi.setSpeed(id, speed);
						}
					}
					if (dir == 1) {
						sem[3].release();
						if (lastSem == 0) {
							sem[0].release();
						} else
							sem[1].release();
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(6, 9)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						sem[6].acquire();
						if (sem[8].tryAcquire()) {
							lastSem = 8;
							tsi.setSwitch(4, 9, 1); // check if its correct
							tsi.setSwitch(3, 11, 1); // check if its correct
							tsi.setSpeed(id, speed);
						} else {
							sem[7].acquire();
							lastSem = 9;
							tsi.setSwitch(4, 9, 1);
							tsi.setSwitch(3, 11, 1);
							tsi.setSpeed(id, speed);
						}
					}
					if (dir == -1) {
						sem[6].release();
//							sem[4].acquire();
						if (lastSem == 8) {

							sem[8].release();
							System.out.println("sem8 is released");
						} else
							sem[7].release();
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(13, 10)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						sem[3].release();
						if (lastSem == 0) {
							sem[0].release();
						} else
							sem[1].release();
						tsi.setSpeed(id, speed);

					}
				}
				if (trainAt(6, 10)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						sem[6].acquire();
						if (sem[8].tryAcquire()) {
							lastSem = 8;
							tsi.setSwitch(4, 9, 1);
							tsi.setSwitch(3, 11, 2);
							tsi.setSpeed(id, speed);
						} else {
							sem[7].acquire();
							lastSem = 7;
							tsi.setSwitch(4, 9, 1);
							tsi.setSwitch(3, 11, 1);
							tsi.setSpeed(id, speed);

						}
					}
					if (dir == -1) {
						sem[6].release();
						if (lastSem == 7) {
							sem[7].release();
						} else
							sem[8].release();
						tsi.setSpeed(id, speed);
					}
				}

			} catch (CommandException | InterruptedException e) {
				e.printStackTrace(); // or only e.getMessage() for the error
				System.exit(1);

			}
		}

	}

	public void StopAndChangeDirection() throws CommandException, InterruptedException {

		tsi.setSpeed(id, 0);
		dir = dir * -1;
		speed = speed * -1;
		Thread.sleep(1000 + (20 * Math.abs(speed)));
		tsi.setSpeed(id, speed);

	}

	public boolean trainAt(int xPos, int yPos) throws CommandException, InterruptedException {

		int s = event.getStatus();
		int x = event.getXpos();
		int y = event.getYpos();
//		System.out.printf("trainAt(%d,%d,%d) s = %d, x=%d, y=%d dir =%d \n", xPos,yPos,direction,s,x,y,dir);

		if (s == 1 & x == xPos & y == yPos) {

			return true;

		} else
			return false;
	}
}
