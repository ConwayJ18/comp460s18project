import java.io.*;
import java.util.*;
import java.math.*;
import src.millerrabin.*;
import src.semiprime.*;
import src.dixon.*;
import src.pollardrho.*;
import src.logicalmatrix.*;
import java.util.concurrent.TimeUnit;

class Driver {

  //Start clock
  public static long startTime;
  public static long endTime;
  public static long timeElapsedMillis;
  public static long timeElapsedSeconds;

    public static void main(String[] args)
    {
        boolean running = true;
        Scanner kbReader = new Scanner(System.in);
        int threads = 0;
        while(threads < 1 || threads > 8)
        {
            System.out.println("Please enter the number of cores (1-8) available. To quit, type 0: "); //Ask for number of threads
            threads = kbReader.nextInt(); //Call that number "threads"
        }

        while(running)
        {
            System.out.println("Please enter a number for factor testing. To quit, type 0: "); //Ask for test number
            BigInteger testNumber = kbReader.nextBigInteger(); //Call that number "testNumber"
            if(testNumber.equals(BigInteger.ZERO))
            {
                System.out.println("Program terminated.");
                running=false;
                break;
            } else
            {
                System.out.println("-----MILLER-RABIN PRIMALITY TEST-----");
                startTime = System.currentTimeMillis();
                boolean isComposite = !MillerRabin.runFromDriver(testNumber, threads);
                System.out.println(testNumber + " is composite? " + isComposite); //Test for primality
                endTime = System.currentTimeMillis();
                timeElapsedMillis = TimeUnit.MILLISECONDS.toMillis(endTime - startTime);
                System.out.println("This calculation took " + timeElapsedMillis + " milliseconds.");
                if(isComposite)
                {
                  System.out.println("-----SEMI-PRIME TEST-----");
                  startTime = System.currentTimeMillis();
                  System.out.println(testNumber + " is semi-prime? " + SemiPrime.runFromDriver(testNumber, threads)); //Run SemiPrime
                  endTime = System.currentTimeMillis();
                  timeElapsedMillis = TimeUnit.MILLISECONDS.toMillis(endTime - startTime);
                  System.out.println("This calculation took " + timeElapsedMillis + " milliseconds.");
                  System.out.println("-----POLLARD-RHO FACTORIZATION ALGORITHM-----");
                  startTime = System.currentTimeMillis();
                  System.out.println("The factors of " + testNumber + " via Pollard-Rho are " + PollardRho.runFromDriver(testNumber)); //Run PollardRho
                  endTime = System.currentTimeMillis();
                  timeElapsedMillis = TimeUnit.MILLISECONDS.toMillis(endTime - startTime);
                  System.out.println("This calculation took " + timeElapsedMillis + " milliseconds.");
                  System.out.println("-----DIXON FACTORIZATION ALGORITHM-----");
                  startTime = System.currentTimeMillis();
                  System.out.println("The factors of " + testNumber + " via Dixon are " + Dixon.runFromDriver(testNumber, threads)); //Run DixonSingleThread
                  endTime = System.currentTimeMillis();
                  timeElapsedMillis = TimeUnit.MILLISECONDS.toMillis(endTime - startTime);
                  System.out.println("This calculation took " + timeElapsedMillis + " milliseconds.");
                }
                else
                {
                  System.out.println("The number is prime and thus cannot be semi-prime. It's factors are 1 & itself.");
                }
            }
            System.out.println("Please enter a number for matrix testing. To quit, type 0: "); //Ask for test number
            int matrixSize = kbReader.nextInt();
            if(matrixSize == 0)
            {
                System.out.println("Program terminated.");
                running=false;
            } else
            {
              System.out.println("-----LOGICAL MATRIX MULTIPLICATION-----");
              startTime = System.currentTimeMillis();
              LogicalMatrixMultiply.runFromDriver(matrixSize, threads);
              endTime = System.currentTimeMillis();
              timeElapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
              System.out.println("This calculation took " + timeElapsedSeconds + " seconds.");
            }

        }
    }
}
