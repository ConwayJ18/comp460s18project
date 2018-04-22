package src.logicalmatrix;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class for Logical Matrix Multiplication
 */
public class LogicalMatrixMultiply extends Thread {
  private static boolean[][] A; //Matrix A
  private static boolean[][] B; //Matrix B
  private static boolean[][] C; //Result matrix
  private static int matrixSize; //Matrix dimension
  private static int threads; //Number of threads
  private int threadNumber;

  /**
 * @param threadNumber
 */
private LogicalMatrixMultiply(int threadNumber)
  {
     this.threadNumber=threadNumber; //Thread constructor
  }

  
/* (non-Javadoc)
 * @see java.lang.Thread#run()
 */
public void run()
  {
      for (int i = threadNumber; i < matrixSize; i+= threads) { //Row of A
           for (int j = 0; j < matrixSize; j++) { //Column of B
               for (int k = 0; k < matrixSize; k++) { //Column of A
                  if(!C[i][j]) //While C is false
                      C[i][j] = (C[i][j] || (A[i][k] && B[k][j])); //C = C or (A and B)
                  else
                      break; //Once C is true it will stay true no matter what
               }
           }
       }
  }
	
  private static void print(){ //Used to print for testing. Not normally necessary.
    for (int i = 0; i < C.length; i++) {
           for (int j = 0; j < A[0].length; j++) {
                System.out.print(((A[i][j]) ? 1 : 0) + " " ); //Print A
           }
           System.out.print(" * ");
           for (int j = 0; j < B[0].length; j++) {
                System.out.print(((B[i][j]) ? 1 : 0) + " " ); //Print B
           }
           System.out.print(" = ");
           for (int j = 0; j < C[0].length; j++) {
               System.out.print(((C[i][j]) ? 1 : 0) + " " ); //Print C
           }
           System.out.println();
    }
  }
  
  
  public static void runFromDriver(int n, int t)
  {
    threads = t; //Number of threads
    matrixSize = n; //Size of matrixes
    Random rand = new Random();
    A=new boolean[matrixSize][matrixSize]; //Initialize A, B, C
    B=new boolean[matrixSize][matrixSize];
    C=new boolean[matrixSize][matrixSize];
    LogicalMatrixMultiply[] thrd = new LogicalMatrixMultiply[threads];

    //Create A
    for(int i=0;i<matrixSize;i++)
    {
      for(int j=0;j<matrixSize;j++)
      {
          A[i][j]=rand.nextBoolean(); //Randomly fill A
      }
    }
    //System.out.println("Matrix A generated.");

    //Create B
    for(int i=0;i<matrixSize;i++)
    {
      for(int j=0;j<matrixSize;j++)
      {
         B[i][j]=rand.nextBoolean(); //Randomly fill B
      }
    }
    //System.out.println("Matrix B generated.");

    //Start multithreading
    for(int i=0;i<threads;i++)
    {
       thrd[i] = new LogicalMatrixMultiply(i); //Fill thread array
       thrd[i].start(); //Start each thread
    }

    for(int i=0;i<threads;i++)
    {
          try
          {
              thrd[i].join(); //Wait for each thread to finish
          }
          catch(InterruptedException e){}
    }
    //End multithreading

    //Print results
    System.out.println("Successfully generated 2 matrixes of dimension " + matrixSize + " and calulcated their product.");
  }
}
