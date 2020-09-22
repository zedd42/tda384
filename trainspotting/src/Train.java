import TSim.*;
import java.util.concurrent.*;
import java.util.*;
public class Train implements Runnable {

	private int id, speed;
	private int dir = 1; // 1 for down -1 for up
	private TSimInterface tsi;
	private SensorEvent event;
	private volatile Semaphore[] sem;
	public int lastSem;
	public Queue <Integer> queue = new LinkedList<Integer>();
	public int counter = 0;
	

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
			tsi.setDebug(false);

		} catch (CommandException e) {
			e.printStackTrace(); // or only e.getMessage() for the error
			//			System.exit(1);
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
						sem[2].acquire();;
					
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
						sem[2].acquire();;
				
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(14, 7)) {
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
						sem[2].acquire();;
				
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						sem[2].release();
					
						tsi.setSpeed(id, speed);
					}
					
				}
				if (trainAt(11, 8)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						sem[2].release();;
						
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						sem[2].acquire();;
						
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(14, 8)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
//							acquire 1stSingleLane semaphore and release firstStopLane
						acquireSemaphore(3);
						
						
						
						if (trayingToAcquire(4)) {
							
							
							tsi.setSwitch(17, 7, 1);
							tsi.setSwitch(15, 9, 2);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(5);
							
						
							tsi.setSwitch(17, 7, 1);
							tsi.setSwitch(15, 9, 1);
							tsi.setSpeed(id, speed);
						}

					}
					
					if (dir == -1) {
						relaseSemaphore(3);
						relaseSemaphore(45);
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(12, 9)) {
					tsi.setSpeed(id, 0);
					
					if (dir == -1) {
						acquireSemaphore(3);
						
						if (trayingToAcquire(1)) {
						
							lastSem = 0;
							tsi.setSwitch(15, 9, 2);
							tsi.setSwitch(17, 7, 1);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(0);
							
							lastSem = 1;
							tsi.setSwitch(15, 9, 2);
							tsi.setSwitch(17, 7, 2);
							tsi.setSpeed(id, speed);
						}
						
					}
					if (dir == 1) {
						relaseSemaphore(3);
						relaseSemaphore(01);
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(6, 9)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						acquireSemaphore(6);
						if (trayingToAcquire(8)) {
							
							lastSem = 8;
							tsi.setSwitch(4, 9, 1); // check if its correct
							tsi.setSwitch(3, 11, 2); // check if its correct
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(7);
							
							lastSem = 7;
							tsi.setSwitch(4, 9, 1);
							tsi.setSwitch(3, 11, 1);
							tsi.setSpeed(id, speed);
						}
					}
					if (dir == -1) {
						relaseSemaphore(6);

						relaseSemaphore(78);
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(12, 10)) {
					
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
						if (trayingToAcquire(1)) {
							lastSem = 0;
							tsi.setSwitch(15, 9, 1);
							tsi.setSwitch(17, 7, 1);
							tsi.setSpeed(id, speed);
						} else {
							acquireSemaphore(0);
							lastSem = 1;
							tsi.setSwitch(15, 9, 1);
							tsi.setSwitch(17, 7, 2);
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
						relaseSemaphore(78);
						tsi.setSpeed(id, speed);
					}
				}
				if (trainAt(5, 11)) {
					tsi.setSpeed(id, 0);
					if (dir == 1) {
						relaseSemaphore(6);
						relaseSemaphore(45);
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
					if (dir == 1) {
						relaseSemaphore(6);
						relaseSemaphore(45);
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {
						
						acquireSemaphore(6);
						if (trayingToAcquire(4)) {
							
							tsi.setSwitch(3, 11, 2);
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
						relaseSemaphore(7);
						StopAndChangeDirection();
						
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

						
						tsi.setSpeed(id, speed);
					}
					if (dir == -1) {

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
//		tsi.setSpeed(id, speed);

	}

	public boolean trainAt(int xPos, int yPos) throws CommandException, InterruptedException {

		int s = event.getStatus();
		int x = event.getXpos();
		int y = event.getYpos();


		if (s < 2 & x == xPos & y == yPos) {
			System.out.format("Train %d going %d at Sensor (%d,%d)%n", id,dir,x,y);


			return true;

		} else
			return false;
	}
	
	
	private void acquireSemaphore(int id_sem) throws CommandException, InterruptedException{
		
		tsi.setSpeed(id, 0);

		sem[id_sem].acquire();
		queue.add(id_sem);
		counter++;
	}
	
	private void relaseSemaphore(int id_sem) throws CommandException, InterruptedException{
		
//		sem[id_sem].release();
		int temp = queue.poll();
		sem[temp].release();
		counter--;
	}
	
	private boolean trayingToAcquire(int id_sem) throws CommandException, InterruptedException{
		
		if(sem[id_sem].tryAcquire()) {
			queue.add(id_sem);
			counter++;
			return true;
		}else {
			return false;
		}
		
	}
			
			
}
