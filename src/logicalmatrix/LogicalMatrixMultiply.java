package src.logicalmatrix;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class LogicalMatrixMultiply extends Thread {
  private static boolean[][] A;
  private static boolean[][] B;
  private static boolean[][] C;
  private static int n;
  private static int threads;
  private int threadNumber;

  private LogicalMatrixMultiply(int threadNumber)
  {
     this.threadNumber=threadNumber;
  }

  public void run()
  {
      for (int i = threadNumber; i < n; i+= threads) { // aRow

           for (int j = 0; j < n; j++) { // bColumn
               for (int k = 0; k < n; k++) { // aColumn
                  if(!C[i][j])
                      C[i][j] = (C[i][j] || (A[i][k] && B[k][j]));
                  else
                      break;
               }
           }
       }

       print();
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

  public static void runFromDriver(int testNumber)
  {
    threads = 1; //Number of threads
    n = testNumber; //Size of matrixes
    Random rand = new Random();
    A=new boolean[n][n];
    B=new boolean[n][n];
    C=new boolean[n][n];
    LogicalMatrixMultiply[] thrd = new LogicalMatrixMultiply[threads];

    //Create A
    for(int i=0;i<n;i++)
    {
      for(int j=0;j<n;j++)
      {
          A[i][j]=rand.nextBoolean();
      }
    }
    System.out.println("Matrix A generated.");

    //Create B
    for(int i=0;i<n;i++)
    {
      for(int j=0;j<n;j++)
      {
         B[i][j]=rand.nextBoolean();
      }
    }
    System.out.println("Matrix B generated.");

    //Start clock
    long startTime = System.currentTimeMillis();

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
    System.out.println("The calculation of the matrix of dimension " + n + " took " + timeElapsedSeconds + " seconds.");
  }
}
