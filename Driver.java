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
                boolean isComposite = !MillerRabin.runFromDriver(testFactor);
                System.out.println(testFactor + " is composite? " + isComposite); //Test for primality
                if(isComposite)
                {
                  System.out.println(testFactor + " is semi-prime? " + SemiPrime.runFromDriver(testFactor)); //Run SemiPrime
                  System.out.println("The factors of " + testFactor + " via Pollard-Rho are " + PollardRho.runFromDriver(testFactor)); //Run PollardRho
                  System.out.println("The factors of " + testFactor + " via Dixon are " + Dixon.runFromDriver(testFactor)); //Run DixonSingleThread
                  Dixon.runFromDriver(testFactor);
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
