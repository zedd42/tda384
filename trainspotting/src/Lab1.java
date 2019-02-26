import TSim.*;

import java.util.Stack;
import java.util.concurrent.*; 


public class Lab1 {
	public TSimInterface tsi = TSimInterface.getInstance();
	public int trainId1 = 1;
	public int trainId2 = 2;
	public int speed1;
	public int speed2;
	public Stack<Semaphore> stack = new Stack<>();
	
	
  public Lab1(int speed1, int speed2) throws InterruptedException{
	  
	  this.speed1= speed1;
	  this.speed2 = speed2;
	  
	 Semaphore[] sem = new Semaphore[9];
	  
	  for(int i =0; i<=8; i++) {
		  
		  sem[i] = new Semaphore(1,true);
		  
	  }
	  
	  
      Thread trainT1	= new Thread(new Train(trainId1,speed1, tsi, sem ));
      Thread trainT2	= new Thread(new Train(trainId2,speed2,tsi, sem));
      
      trainT1.start();
      trainT2.start();
      trainT1.join();
      trainT2.join();
     

  }
  

}

