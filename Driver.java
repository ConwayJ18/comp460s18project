import java.io.*;
import java.util.*;
import java.math.*;

class Driver {

    public static void main(String[] args)
    {
        Scanner kbReader = new Scanner(System.in);
        System.out.println("Please enter a number for testing: "); //Ask for test number
        BigInteger testNumber = kbReader.nextBigInteger(); //Call that number "testNumber"

        System.out.println(testNumber + " is probably prime? " + MillerRabin.isProbablePrime(testNumber, 40)); //Test for primality
        System.out.println(testNumber + " is semiPrime? " + SemiPrime.isSemiPrime(testNumber)); //Test if semiPrime
    }
}
