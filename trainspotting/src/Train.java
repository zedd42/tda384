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
	 private Semaphore[] sem;

	
	public Train(int id, int speed, TSimInterface tsi, Semaphore[] sem) {
	
		this.id = id;
		this.speed = speed;
		this.tsi = tsi;
		this.sem = sem;
		
		if (id == 2) {
			dir = -1;
		}
		try {
			
			tsi.setSpeed(id,speed);
		
		}catch (CommandException e) {
			 e.printStackTrace();    // or only e.getMessage() for the error
		     System.exit(1); 
		}
	}

	
	public void run() {
		
		while(true) {
			
			try {
				
				event = tsi.getSensor(id);
				
				if (trainAt(6,5,1)) 
				
				{
					tsi.setSpeed(id,0);
					sem[0].acquire();
//					tsi.setSwitch(17,7,2);
//					tsi.setSwitch(15,9,2);
//					tsi.setSwitch(3,11,2);
					tsi.setSpeed(id,speed);	
				}
				
				if (trainAt(11,17,1)) 
				{
					sem[0].release();
					
				}
				if (trainAt(15,7,1)) 
				{
					tsi.setSpeed(id,0);
					sem[1].acquire();
					tsi.setSwitch(17,7,2);
					tsi.setSwitch(15,9,2);
					tsi.setSpeed(id,speed);
					
				}
				if (trainAt(13,9,1))
				{
					
					sem[1].release();
					
				}
				if (trainAt(6,9,1))
				{
					tsi.setSpeed(id, 0);
					sem[2].acquire();
					tsi.setSwitch(3,11,2);
				}
				if (trainAt(5,13,1))
				{
					sem[2].release();
					StopAndChangeDirection ();
				}
				
				

				if (trainAt(5,11,-1)) 
				
				{
					tsi.setSpeed(id,0);
					sem[0].acquire();
					tsi.setSwitch(3,11,2);
					tsi.setSpeed(id,speed);
				}
				if (trainAt(6,5,-1))
				{
					sem[0].release();
					StopAndChangeDirection ();
					
				}

				
				
				
				
			}
			catch (CommandException | InterruptedException e) {
				  e.printStackTrace();    // or only e.getMessage() for the error
			      System.exit(1);
				
			}
		}

	}
	
	
	public void StopAndChangeDirection () throws CommandException, InterruptedException{
		
		tsi.setSpeed(id,0);
//		dir = dir * -1;
//		Thread.sleep(1000 + (20 * Math.abs(speed)));
//		tsi.setSpeed(id,dir * speed);
		
	}
	
	
	public boolean trainAt(int xPos, int yPos, int direction) throws CommandException, InterruptedException{
		
		int s = event.getStatus();
		int x = event.getXpos();
		int y = event.getYpos();
//		System.out.printf("trainAt(%d,%d,%d) s = %d, x=%d, y=%d dir =%d \n", xPos,yPos,direction,s,x,y,dir);
		
		if (s == 1 & x == xPos & y == yPos & direction == dir ) {
			
			return true;
			
		}else 
			return false;
	}
}
	
