package src.logicalmatrix;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class LogicalMatrixMultiply extends Thread {
  private static boolean[][] A;
  private static boolean[][] B;
  private static boolean[][] C;
  private static int matrixSize;
  private static int threads;
  private int threadNumber;

  private LogicalMatrixMultiply(int threadNumber)
  {
     this.threadNumber=threadNumber;
  }

  public void run()
  {
      for (int i = threadNumber; i < matrixSize; i+= threads) { // aRow

           for (int j = 0; j < matrixSize; j++) { // bColumn
               for (int k = 0; k < matrixSize; k++) { // aColumn
                  if(!C[i][j])
                      C[i][j] = (C[i][j] || (A[i][k] && B[k][j]));
                  else
                      break;
               }
           }
       }

       //print();
  }

  private static void print(){
    for (int i = 0; i < C.length; i++) {
           for (int j = 0; j < A[0].length; j++) {
                System.out.print(((A[i][j]) ? 1 : 0) + " " );
           }
           System.out.print(" * ");
           for (int j = 0; j < B[0].length; j++) {
                System.out.print(((B[i][j]) ? 1 : 0) + " " );
           }
           System.out.print(" = ");
           for (int j = 0; j < C[0].length; j++) {
               System.out.print(((C[i][j]) ? 1 : 0) + " " );
           }
           System.out.println();
    }
  }

  public static void runFromDriver(int n, int t)
  {
    threads = t; //Number of threads
    matrixSize = n; //Size of matrixes
    Random rand = new Random();
    A=new boolean[matrixSize][matrixSize];
    B=new boolean[matrixSize][matrixSize];
    C=new boolean[matrixSize][matrixSize];
    LogicalMatrixMultiply[] thrd = new LogicalMatrixMultiply[threads];

    //Start clock
    long startTime = System.currentTimeMillis();

    //Create A
    for(int i=0;i<matrixSize;i++)
    {
      for(int j=0;j<matrixSize;j++)
      {
          A[i][j]=rand.nextBoolean();
      }
    }
    //System.out.println("Matrix A generated.");

    //Create B
    for(int i=0;i<matrixSize;i++)
    {
      for(int j=0;j<matrixSize;j++)
      {
         B[i][j]=rand.nextBoolean();
      }
    }
    //System.out.println("Matrix B generated.");

    //Run calculation
    for(int i=0;i<threads;i++)
    {
       thrd[i] = new LogicalMatrixMultiply(i);
       thrd[i].start();
    }

    for(int i=0;i<threads;i++)
    {
          try
          {
              thrd[i].join();
          }
          catch(InterruptedException e){}
    }

    //Stop clock
    long endTime = System.currentTimeMillis();

    //Calculate time elapsed
    long timeElapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);

    //Print results
    System.out.println("The generation of 2 matrices of dimension " + matrixSize + " and the calculation of their product took " + timeElapsedSeconds + " seconds.");
  }
}
