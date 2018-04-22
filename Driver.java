import java.io.*;
import java.util.*;
import java.math.*;
import src.millerrabin.*;
import src.semiprime.*;
import src.dixon.*;
import src.pollardrho.*;
import src.logicalmatrix.*;
import java.util.concurrent.TimeUnit;

/**
 * Class Used to run all aspects of the program at once
 */
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
        while(threads < 1 || threads > 8) //Make sure it's a reasonable number of cores
        {
            System.out.println("Please enter the number of cores (1-8) available. To quit, type 0: "); //Ask for number of threads
            threads = kbReader.nextInt(); //Call that number "threads"
        }

        while(running)
        {
            System.out.println("Please enter a number for factor testing. To quit, type 0: "); //Ask for test number
            BigInteger testNumber = kbReader.nextBigInteger(); //Call that number "testNumber"
            if(testNumber.equals(BigInteger.ZERO)) //If 0 then quit
            {
                System.out.println("Program terminated.");
                running=false;
                break;
            } else
            {
                System.out.println("-----MILLER-RABIN PRIMALITY TEST-----"); //Run Miller-Rabin
                startTime = System.currentTimeMillis();
                boolean isComposite = !MillerRabin.runFromDriver(testNumber, threads);
                System.out.println(testNumber + " is composite? " + isComposite);
                endTime = System.currentTimeMillis();
                timeElapsedMillis = TimeUnit.MILLISECONDS.toMillis(endTime - startTime);
                System.out.println("This calculation took " + timeElapsedMillis + " milliseconds."); //Print time
                if(isComposite) //If composite
                {
                  System.out.println("-----SEMI-PRIME TEST-----"); //Run Semi-Prime
                  startTime = System.currentTimeMillis();
                  System.out.println(testNumber + " is semi-prime? " + SemiPrime.runFromDriver(testNumber, threads));
                  endTime = System.currentTimeMillis();
                  timeElapsedMillis = TimeUnit.MILLISECONDS.toMillis(endTime - startTime);
                  System.out.println("This calculation took " + timeElapsedMillis + " milliseconds."); //Print time
                  System.out.println("-----POLLARD-RHO FACTORIZATION ALGORITHM-----"); //Run Pollard's Rho
                  startTime = System.currentTimeMillis();
                  System.out.println("The factors of " + testNumber + " via Pollard-Rho are " + PollardRho.runFromDriver(testNumber));
                  endTime = System.currentTimeMillis();
                  timeElapsedMillis = TimeUnit.MILLISECONDS.toMillis(endTime - startTime);
                  System.out.println("This calculation took " + timeElapsedMillis + " milliseconds."); //Print time
                  System.out.println("-----DIXON FACTORIZATION ALGORITHM-----"); //Run Dixon
                  startTime = System.currentTimeMillis();
                  System.out.println("The factors of " + testNumber + " via Dixon are " + Dixon.runFromDriver(testNumber, threads));
                  endTime = System.currentTimeMillis();
                  timeElapsedMillis = TimeUnit.MILLISECONDS.toMillis(endTime - startTime);
                  System.out.println("This calculation took " + timeElapsedMillis + " milliseconds."); //Print time
                }
                else
                {
                  System.out.println("The number is prime and thus cannot be semi-prime. It's factors are 1 & itself."); //If prime, cannot factor
                }
            }
            System.out.println("Please enter a number for matrix testing. To quit, type 0: "); //Ask for matrix dimension
            int matrixSize = kbReader.nextInt();
            if(matrixSize == 0) //If 0 then quit
            {
                System.out.println("Program terminated.");
                running=false;
            } else
            {
              System.out.println("-----LOGICAL MATRIX MULTIPLICATION-----"); //Run matrix multiplication
              startTime = System.currentTimeMillis();
              LogicalMatrixMultiply.runFromDriver(matrixSize, threads);
              endTime = System.currentTimeMillis();
              timeElapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(endTime - startTime);
              System.out.println("This calculation took " + timeElapsedSeconds + " seconds."); //Print time
            }

        }
    }
}
