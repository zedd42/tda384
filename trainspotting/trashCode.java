import TSim.*;
import java.util.concurrent.TimeUnit;
//import Train.*;

public class Lab1 {
	public TSimInterface tsi = TSimInterface.getInstance();
	public int trainId1 = 1;
	public int trainId2 = 2;//import Train.*;
	public int speed1;
	public int speed2;
	
	
	
  public Lab1(int speed1, int speed2) {

    
    try {
      tsi.setSpeed(trainId1,speed1);
 //     tsi.setSpeed(2,speed2);
      //tsi.setSwitch(17,7,0);
      //tsi.setSwitch(4,9,0);
     // SensorEvent sen = tsi.getSensor(1);
     // System.out.println(sen);
      test();
      TestThread();

      
    }
    catch (CommandException e) {
      e.printStackTrace();    // or only e.getMessage() for the error
      System.out.println("Hej");
      System.exit(1);
      
    }


  }
  
  public void test () {
	  
	  SensorEvent sen1 = new SensorEvent(1,6,6,0);
	  SensorEvent sen2 = new SensorEvent(1,15,7,0);
	  
  	
      try {
        	tsi.getSensor(trainId1);
        	System.out.println("hej");
        	//SensorEvent event		= (SensorEvent) tsi.getSensor(id); // what does this event
        	
        	//tsi.setSpeed(id,0);
        	//System.out.println(event.getStatus());
        	//System.out.println(sen1.getStatus());
        	if (sen2.getStatus() == 0) {
        		tsi.setSwitch(17,7,0);
        	}
        	
     //   	TimeUnit.SECONDS.sleep(1);    //we cant do this, find another way
     //  	tsi.setSpeed(id, -10);
        	
        	
        	
        }
        catch(InterruptedException | CommandException e ) {
        	System.out.println("fghjkl");
        	
        }
  }

  public void TestThread () {
	  
	  Thread train1 = new Thread(new Train(1,15));
	  
	  train1.start();
  }

}

import TSim.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*; 


public class Lab1 {
	public TSimInterface tsi = TSimInterface.getInstance();
	public int trainId1 = 1;
	public int trainId2 = 2;
	public int speed1;
	public int speed2;
	
	
	
  public Lab1(int speed1, int speed2) throws InterruptedException{
	  
	  this.speed1= speed1;
	  this.speed2 = speed2;
	  
	  Semaphore sem = new Semaphore(1);
    

      Thread trainT1	= new Thread(new Train(trainId1,speed1,sem));
      Thread trainT2	= new Thread(new Train(trainId2,speed2, sem));
      
      trainT1.start();
      trainT2.start();
      trainT1.join();
      trainT2.join();
     

  }
  

}




if (trainAt(6,5,1)) 
	
{
	tsi.setSpeed(id,0);
//	the junction semphore
	sem[0].acquire();
	tsi.setSpeed(id,speed);	
}
//sensor right of the junction
if (trainAt(11,17,1)) 
{
	sem[0].release();
	
}
//sensor left of 1st switch
if (trainAt(15,7,1)) 
{
	tsi.setSpeed(id,0);
//	entring 1st single lane
	sem[1].acquire();
	tsi.setSwitch(17,7,2);
	tsi.setSwitch(15,9,2);
	tsi.setSpeed(id,speed);
	
}
//sensor left of 2nd switch
if (trainAt(13,9,1))
{
//	exiting 1st single lane
	sem[1].release();
	
}
//sensor right of 3rd switch
if (trainAt(6,9,1))
{
	tsi.setSpeed(id, 0);
//	entring 2nd single lane
	sem[2].acquire();
	tsi.setSwitch(3,11,2);
	tsi.setSpeed(id,speed);
}
if (trainAt(5,13,1))
{
	sem[2].release();
	StopAndChangeDirection ();
}



if (trainAt(5,11,-1)) 

{
	tsi.setSpeed(id,0);
	sem[2].acquire();
	tsi.setSwitch(3,11,1);
	tsi.setSpeed(id,speed);
}
if (trainAt(6,9,-1))
{
	sem[2].release();
	StopAndChangeDirection ();
	
}















if(trainAt(15,7)) {
	tsi.setSpeed(id,0);
	if(dir == 1) {
//		acquire 1stSingleLane semaphore and release firstStopLane
		sem[3].acquire();
		sem[0].release();
		if(sem[4].tryAcquire()) {
			tsi.setSwitch(17,7,2);
			tsi.setSwitch(15,9,2);
			tsi.setSpeed(id,speed);
		}else {
			sem[5].acquire();
			tsi.setSwitch(17,7,2);
			tsi.setSwitch(15,9,1);
			tsi.setSpeed(id,speed);
		}	
	}
	if(dir == -1) {
		
		sem[3].release();
//		have to release sem4 or sem5
		tsi.setSpeed(id,speed);
		
	}
	
	if(trainAt(15,8)) {
		tsi.setSpeed(id,0);
		if(dir == 1) {
//			acquire 1stSingleLane semaphore and release firstStopLane
			sem[3].acquire();
			sem[1].release();
			if(sem[4].tryAcquire()) {
				tsi.setSwitch(17,7,2);
				tsi.setSwitch(15,9,2);
				tsi.setSpeed(id,speed);
			}else {
				sem[5].acquire();
				tsi.setSwitch(17,7,2);
				tsi.setSwitch(15,9,1);
				tsi.setSpeed(id,speed);
			}
			
		}
		if (dir == -1) {
			sem[3].release();
//			have to release sem4 or sem5
			tsi.setSpeed(id,speed);
		}
	}















