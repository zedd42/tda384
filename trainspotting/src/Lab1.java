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
	  
	  Semaphore sem = new Semaphore(1,true);
    

      Thread trainT1	= new Thread(new Train(trainId1,speed1,sem));
      Thread trainT2	= new Thread(new Train(trainId2,speed2,sem));
      
      trainT1.start();
      trainT2.start();
      trainT1.join();
      trainT2.join();
     

  }
  

}

