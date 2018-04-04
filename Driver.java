import java.io.*;
import java.util.*;
import java.math.*;
import src.millerrabin.*;
import src.semiprime.*;
import src.dixon.*;
import src.pollardrho.*;

class Driver {

    public static void main(String[] args)
    {
        boolean running = true;
        BigInteger MAX_Dixon = new BigInteger("180250");
        //BigInteger MAX_PollardRho = new BigInteger("0");

        while(running)
        {
            Scanner kbReader = new Scanner(System.in);
            System.out.println("Please enter a number for testing. To quit, type 0: "); //Ask for test number
            BigInteger testNumber = kbReader.nextBigInteger(); //Call that number "testNumber"
            if(testNumber.equals(BigInteger.ZERO))
            {
                System.out.println("Program terminated.");
                running=false;
            } else
            {
                boolean isComposite = !MillerRabin.isProbablePrime(testNumber, 40);
                System.out.println(testNumber + " is composite? " + isComposite); //Test for primality
                if(isComposite)
                {
                  System.out.println(testNumber + " is semi-prime? " + SemiPrime.isSemiPrime(testNumber)); //Run SemiPrime
                  System.out.println("The factors of " + testNumber + " via Pollard-Rho are " + PollardRho.pollardRho(testNumber)); //Run PollardRho
                  if(testNumber.compareTo(MAX_Dixon) <= 0)
                  {
                    System.out.println("The factors of " + testNumber + " via Dixon are " + Dixon.dixon(testNumber)); //Run Dixon
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
        }
    }
}
