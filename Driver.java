import java.io.*;
import java.util.*;
import java.math.*;

class Driver {

    public static void main(String[] args)
    {
        boolean running = true;

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
                System.out.println(testNumber + " is composite? " + !MillerRabin.isProbablePrime(testNumber, 40)); //Test for primality
                System.out.println(testNumber + " is semi-prime? " + SemiPrime.isSemiPrime(testNumber)); //Test if semiPrime
            }
        }
    }
}
