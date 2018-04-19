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
        Scanner kbReader = new Scanner(System.in);
        System.out.println("Please enter the number of cores available on your computer. To quit, type 0: "); //Ask for number of threads
        int threads = kbReader.nextInt(); //Call that number "threads"

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
                boolean isComposite = !MillerRabin.runFromDriver(testNumber, threads);
                System.out.println(testNumber + " is composite? " + isComposite); //Test for primality
                if(isComposite)
                {
                  System.out.println(testNumber + " is semi-prime? " + SemiPrime.runFromDriver(testNumber, threads)); //Run SemiPrime
                  System.out.println("The factors of " + testNumber + " via Pollard-Rho are " + PollardRho.runFromDriver(testNumber)); //Run PollardRho
                  System.out.println("The factors of " + testNumber + " via Dixon are " + Dixon.runFromDriver(testNumber, threads)); //Run DixonSingleThread
                  Dixon.runFromDriver(testNumber, threads);
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
                LogicalMatrixMultiply.runFromDriver(matrixSize, threads);
            }

        }
    }
}
