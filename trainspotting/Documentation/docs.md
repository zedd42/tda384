# Assaignment Nr.1 Trainspotting Course TDA384

## An explanation of how the program works
The map is divided in 9 different critical sections where the trains could possibly collide with each other. There is sensors at the beginning and end of these section to ensure the safety. 

For a train to enter one of the critical section it requires the semaphore of the critical section. If denied it will stop at the sensor otherwise the train can continue. 

The placement of the sensor was critical for two reasons. The first is to ensure that the train will not derail in high speed, the highest speed tested is 16. The second reason is to give a distance for the train to brake to full stop if the entrance of the critical section is denied. After trial and error i have come to the conclusion that the best way is the one showed in the attached file.
   
## The critical sections
1. 14,3 <--> 14,7
2. 6,5 <--> 11,7 or 8,5 <--> 11,8
3. 14,5 <--> 14,8
4. 14,7 or 14,8 <--> 12,9 or 12,10
5. 12,9 <--> 6,9
6. 12,10 <--> 6,10
7. 6,9 or 6,10 <--> 5,11 or 5,13
8. 5,11 <--> 14,11 
9. 5,13 <--> 14,13 