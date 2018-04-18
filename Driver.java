import java.io.*;
import java.util.*;
import java.math.*;
import src.millerrabin.*;
import src.semiprime.*;
import src.dixon.*;
import src.pollardrho.*;
import src.logicalmatrix.*;

class Driver {

    public static void main(String[] args)
    {
        boolean running = true;
        BigInteger MAX_Dixon = new BigInteger("180250");
        //BigInteger MAX_PollardRho = new BigInteger("0");

        while(running)
        {
            Scanner kbReader = new Scanner(System.in);
            System.out.println("Please enter a number for factor testing. To quit, type 0: "); //Ask for test number
            BigInteger testFactor = kbReader.nextBigInteger(); //Call that number "testFactor"
            if(testFactor.equals(BigInteger.ZERO))
            {
                System.out.println("Program terminated.");
                running=false;
                break;
            } else
            {
                boolean isComposite = !MillerRabin.isProbablePrime(testFactor, 40);
                System.out.println(testFactor + " is composite? " + isComposite); //Test for primality
                if(isComposite)
                {
                  System.out.println(testFactor + " is semi-prime? " + SemiPrime.runFromDriver(testFactor)); //Run SemiPrime
                  System.out.println("The factors of " + testFactor + " via Pollard-Rho are " + PollardRho.runFromDriver(testFactor)); //Run PollardRho
                  if(testFactor.compareTo(MAX_Dixon) <= 0)
                  {
                    System.out.println("The factors of " + testFactor + " via Dixon on a single thread are " + DixonSingleThread.runFromDriver(testFactor)); //Run DixonSingleThread
                    //System.out.println("The factors of " + testFactor + " via Dixon on multiple threads are " + Dixon.runFromDriver(testFactor)); //Run DixonSingleThread
                  }
                  else
                  {
                    System.out.println("The test number is too large for our Dixon algorithm.");
                  }
                }
                else
                {
                  System.out.println("The number is prime and thus cannot be semi-prime. It's factors are 1 & itself.");
                }
            }
            System.out.println("Please enter a number for matrix testing. To quit, type 0: "); //Ask for test number
            int testMatrix = kbReader.nextInt();
            if(testMatrix == 0)
            {
                System.out.println("Program terminated.");
                running=false;
            } else
            {
                LogicalMatrixMultiply.runFromDriver(testMatrix);
            }

        }
    }
}
