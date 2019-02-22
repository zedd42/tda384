import TSim.*;
import java.util.concurrent.*;

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
//			tsi.setDebug(false);

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
						acquireSemaphore(0);
						
						tsi.setSpeed(id, speed);

					}
					if (dir == -1) {
						
						StopAndChangeDirection();
						tsi.setSpeed(id, speed);
					}
				}
//				sensor leftOfJunction
				if (trainAt(6, 5)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
//						acquire the Junction semaphore
						acquireSemaphore(2);
					
						tsi.setSpeed(id, speed);

					}
					if (dir == -1) {
//						relese junction semaphore
						relaseSemaphore(2);
				
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(11, 7)) {

					tsi.setSpeed(id, 0);
					if (dir == 1) {
//						relese junction smaphore
						relaseSemaphore(2);
					
						tsi.setSpeed(id, speed);

					}
					if (dir == -1) {
//						acquire junction semaphore
						acquireSemaphore(2);
				
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(15, 7)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
//						acquire 1stSingleLane semaphore and release firstStopLane
						acquireSemaphore(3);
				
						
			
						if (trayingToAcquire(4)) {
						
							lastSem = 4;
							tsi.setSwitch(17, 7, 2);
							tsi.setSwitch(15, 9, 2);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(5);
						
							lastSem = 5;
							tsi.setSwitch(17, 7, 2);
							tsi.setSwitch(15, 9, 1);
							tsi.setSpeed(id, speed);
						}
					}
					if (dir == -1) {

						relaseSemaphore(3);
			
//						have to release sem4 or sem5
						if (lastSem == 4) {
							relaseSemaphore(4);
					
						} else {
							relaseSemaphore(5);
							
						}
						tsi.setSpeed(id, speed);

					}
				}

				if (trainAt(14, 5)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						acquireSemaphore(1);
				
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						StopAndChangeDirection();
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(8, 5)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						acquireSemaphore(2);
				
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						relaseSemaphore(2);
					
						tsi.setSpeed(id, speed);
					}
					
				}
				if (trainAt(11, 8)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						relaseSemaphore(2);
						
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						acquireSemaphore(2);
						
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(15, 8)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
//							acquire 1stSingleLane semaphore and release firstStopLane
						acquireSemaphore(3);
						
						relaseSemaphore(1);
						
						if (trayingToAcquire(4)) {
							
							lastSem = 4;
							tsi.setSwitch(17, 7, 1);
							tsi.setSwitch(15, 9, 2);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(5);
							
							lastSem = 5;
							tsi.setSwitch(17, 7, 1);
							tsi.setSwitch(15, 9, 1);
							tsi.setSpeed(id, speed);
						}

					}
					
					if (dir == -1) {
						relaseSemaphore(3);
						

						if (lastSem == 4) {
							relaseSemaphore(4);
						} else
							relaseSemaphore(5);
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(13, 9)) {
					tsi.setSpeed(id, 0);
					
					if (dir == -1) {
						acquireSemaphore(3);
						
						if (trayingToAcquire(0)) {
						
							lastSem = 0;
							tsi.setSwitch(15, 9, 2);
							tsi.setSwitch(17, 7, 2);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(1);
							
							lastSem = 1;
							tsi.setSwitch(15, 9, 2);
							tsi.setSwitch(17, 7, 1);
							tsi.setSpeed(id, speed);
						}
						
					}
					if (dir == 1) {
						relaseSemaphore(3);
					
						if (lastSem == 0) {
							relaseSemaphore(0);
						
						} else {
							relaseSemaphore(1);
							
						}
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(6, 9)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						acquireSemaphore(6);
						if (trayingToAcquire(8)) {
							relaseSemaphore(4);
							lastSem = 8;
							tsi.setSwitch(4, 9, 1); // check if its correct
							tsi.setSwitch(3, 11, 2); // check if its correct
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(7);
							relaseSemaphore(4);
							lastSem = 7;
							tsi.setSwitch(4, 9, 1);
							tsi.setSwitch(3, 11, 1);
							tsi.setSpeed(id, speed);
						}
					}
					if (dir == -1) {
						relaseSemaphore(6);

						if (lastSem == 8) {

							relaseSemaphore(8);
							
						} else
							relaseSemaphore(7);
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(13, 10)) {
					
					tsi.setSpeed(id, 0);
					
					if (dir == 1) {
						relaseSemaphore(3);
						if (lastSem == 0) {
							relaseSemaphore(0);
						} else
							relaseSemaphore(1);
						tsi.setSpeed(id, speed);

					}
					if (dir == -1) {
						acquireSemaphore(3);
						if (trayingToAcquire(0)) {
							lastSem = 0;
							tsi.setSwitch(15, 9, 1);
							tsi.setSwitch(17, 7, 2);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(1);
							lastSem = 1;
							tsi.setSwitch(15, 9, 1);
							tsi.setSwitch(17, 7, 1);
							tsi.setSpeed(id, speed);
						}
					}
				}
				if (trainAt(6, 10)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						acquireSemaphore(6);
						if (trayingToAcquire(8)) {
							
							lastSem = 8;
							tsi.setSwitch(4, 9, 2);
							tsi.setSwitch(3, 11, 2);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(7);
							
							lastSem = 7;
							tsi.setSwitch(4, 9, 2);
							tsi.setSwitch(3, 11, 1);
							tsi.setSpeed(id, speed);

						}
					}
					if (dir == -1) {
						relaseSemaphore(6);
						if (lastSem == 8) {
							relaseSemaphore(8);
						} else
							relaseSemaphore(7);
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(5, 11)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						relaseSemaphore(6);
						if (lastSem == 4) {
							relaseSemaphore(4);
						} else
							relaseSemaphore(5);
						tsi.setSpeed(id, speed);

					}
					if (dir == -1) {
						acquireSemaphore(6);
						if (trayingToAcquire(4)) {
							lastSem = 4;
							tsi.setSwitch(3, 11, 1);
							tsi.setSwitch(4, 9, 1);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(5);
							tsi.setSwitch(3, 11, 1);
							tsi.setSwitch(4, 9, 2);
							tsi.setSpeed(id, speed);
						}
					}
				}
				if (trainAt(5, 13)) {
					tsi.setSpeed(id, 0);
					System.out.println("ssgj");
					if (dir == 1) {
						relaseSemaphore(6);
						if (lastSem == 4) {
							relaseSemaphore(4);
							
						} else {
							relaseSemaphore(5);
							
						}
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						
						acquireSemaphore(6);
						if (trayingToAcquire(4)) {
							
							tsi.setSwitch(3, 11, 1);
							tsi.setSwitch(4, 9, 1);
							tsi.setSpeed(id, speed);

						} else {
							
							acquireSemaphore(5);
							
							tsi.setSwitch(3, 11, 2);
							tsi.setSwitch(4, 9, 2);
							tsi.setSpeed(id, speed);
						}

					}
				}
				if (trainAt(14, 11)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						StopAndChangeDirection();
						relaseSemaphore(7);
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						acquireSemaphore(7);
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(14, 13)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						StopAndChangeDirection();
						relaseSemaphore(8);
						System.out.println("vänder");
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						acquireSemaphore(8);
						tsi.setSpeed(id, speed);
						System.out.println("vänder");
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
//		tsi.setSpeed(id, speed);

	}

	public boolean trainAt(int xPos, int yPos) throws CommandException, InterruptedException {

		int s = event.getStatus();
		int x = event.getXpos();
		int y = event.getYpos();
//		System.out.printf("trainAt(%d,%d,%d) s = %d, x=%d, y=%d dir =%d \n", xPos,yPos,direction,s,x,y,dir);

		if (s < 2 & x == xPos & y == yPos) {
			System.out.format("Train %d going %d at Sensor (%d,%d)%n", id,dir,x,y);
			System.out.format("The lastsemaphore is %d%n", lastSem);

			return true;

		} else
			return false;
	}
	
	
	private void acquireSemaphore(int id_sem) throws CommandException, InterruptedException{
		
		tsi.setSpeed(id, 0);
		System.out.format("Train %d: Waiting to get semaphore %d%n",id, id_sem);
		sem[id_sem].acquire();
		System.out.format("Train %d: Has acquired semaphore %d%n",id, id_sem);
//		tsi.setSpeed(id, speed);
	}
	
	private void relaseSemaphore(int id_sem) throws CommandException, InterruptedException{
		
		sem[id_sem].release();
		System.out.format("Train %d: Has released semaphore %d%n",id, id_sem);
		
	}
	
	private boolean trayingToAcquire(int id_sem) throws CommandException, InterruptedException{
		
		
		System.out.format("Train %d: Trying to get semaphore %d%n",id, id_sem);
		if(sem[id_sem].tryAcquire()) {
			System.out.format("Train %d: Has acquired semaphore %d%n",id, id_sem);
			return true;
		}else {
			return false;
		}
		
	}
			
			
}
