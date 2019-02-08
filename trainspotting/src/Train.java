import TSim.*;
import java.util.concurrent.*; 





public class Train implements Runnable {
	
	 int id;
	 int speed;
	 TSimInterface tsi = TSimInterface.getInstance();
	 Semaphore sem;

	
	public Train(int id, int speed, Semaphore sem) {
		
		this.id		= id;
		this.speed	= speed;
		this.sem 	= sem;
	}
	
	public void run() {
		System.out.printf("Train %d is created\n", this.id);
		
		try {
		//	tsi.setSwitch(17,7,tsi.SWITCH_RIGHT);
			tsi.setSpeed(id,speed);
			//tsi.setDebug(false);
			while (true) {
				testCritical ();
			}
		}
		catch(CommandException e) {
			e.printStackTrace();
		}
	}

	public void testCritical () {
		
		try {

			SensorEvent t1Events		= (SensorEvent) tsi.getSensor(id);
			System.out.println("t1Events.getStatus = " + t1Events.getStatus());
			
			int x = t1Events.getXpos();
			int y = t1Events.getYpos();
			int s = t1Events.getStatus();
			
			if ((s == 1 & x == 6 & y== 7) | (s == 1 & x == 5 & y== 11)) {
				
				tsi.setSwitch(15, 9, 0);
				if(sem.tryAcquire()) {
					tsi.setSpeed(id,speed);
				}
				else {
					tsi.setSpeed(id,0);
				}
			}
			if ((s == 1 & x == 6 & y== 9) | (s == 1 & x == 15 & y== 7)) {
				tsi.setSpeed(id,0);
			
				
				//sem.notifyAll();
			}
			else
			{
				sem.acquire();
				tsi.setSpeed(id,speed);
			}
			if ((s == 1 & x == 8 & y== 5) & sem.tryAcquire()){
				
				tsi.setSpeed(id,0);
				sem.release();
			}
			
			
		}
		catch (CommandException |InterruptedException e ) {
			e.printStackTrace();
			System.exit(1);
		}	
	}
	
}