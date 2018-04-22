package src.semiprime;

import java.math.*;
import java.util.Random;
import src.millerrabin.*;

/**
 * The class to run semiprme testing
 */

public class SemiPrime extends Thread {
    private static BigDecimal THREE_D=BigDecimal.valueOf(3); //Needed in cubeRoot calculation
    private static int UP=BigDecimal.ROUND_HALF_UP; //Rounds things up, also needed in cubeRoot
    private static BigInteger testNumber; //The number being factored
    private static BigInteger cbrt; //Stores the cube root of the number being factored
    private static int degreeOfCertainty = 100; //Higher numbers means more certainty
    private static int threads; //The number of threads we will run
    private static boolean result = false; //Shared among threads
    private int threadNumber;

    /**
     * @param threadNumber
     */
    private SemiPrime(int threadNumber) //Thread constructor
    {
       this.threadNumber=threadNumber;
    }

    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     */
    public void run() //The part of the code that's multithreaded
    {
        BigInteger i = BigInteger.valueOf(threadNumber+2); //Initialize the incrementer
        BigInteger m = null; //We'll need this later
        BigInteger bigThreads = BigInteger.valueOf(threads);

        while(i.compareTo(cbrt)<=0) //For 2 <= i <= cbrt(n)
        {
            if(testNumber.mod(i).equals(BigInteger.ZERO)) //Is i a factor of n?
            {
                if(!MillerRabin.isProbablePrime(i, threads)) //If so, is i composite?
                {
                    result = true; //Then n has more than 2 factors
                }
                else
                {
                    m = testNumber.divide(i);
                    if(!MillerRabin.isProbablePrime(m, threads)) //What about n/i?
                    {
                        result = true; //Then n still has more than 2 factors
                    }
                }
            }
            i = i.add(bigThreads); //Increment i
        }
    }

    /**
     * @return
     */
    private static boolean isSemiPrime() //The actual test
    {
        if(!MillerRabin.isProbablePrime(testNumber, threads)) //Is it composite?
        {
                cbrt = cubeRoot(testNumber); //Calculate the cube root
                if(!manyFactors(testNumber)) //How many factors does it have?
                        return true; //If composite & only two factors, exist, return true
        }
        return false; //Else return false
    }

    /**
     * @param n
     * @return
     */
    private static BigInteger cubeRoot(BigInteger n) //Calculates cube roots
    {
        BigDecimal nCopy = new BigDecimal(n); //Create copy of n
        BigInteger tempCbrt = BigInteger.ZERO.setBit(n.bitLength()/3); //Guess bitLength of result
        BigDecimal s = BigDecimal.ZERO; //Set s equal to 0
        while (!tempCbrt.equals(s.toBigInteger())) //Is result equal to s?
        {
            s = new BigDecimal(tempCbrt); //Set s equal to BigDecimal version of result

            BigDecimal temp0 = new BigDecimal(tempCbrt.shiftLeft(1)); //Do some math
            BigDecimal temp1 = temp0.add(nCopy.divide(s.multiply(s), UP));
            BigDecimal temp2 = temp1.divide(THREE_D, UP);

            tempCbrt = temp2.toBigInteger(); //Convert things back to BigInteger
        }
        return tempCbrt;  //Finally return r
    }

    /**
     * @param n
     * @return
     */
    private static boolean manyFactors(BigInteger n) //How many factors does n have?
    {
            SemiPrime[] thrd = new SemiPrime[threads];

            //Start multithreading
            for(int i=0;i<threads;i++)
            {
               thrd[i] = new SemiPrime(i); //Fill threads array
               thrd[i].start(); //Start each thread
            }

            for(int i=0;i<threads;i++)
            {
                  try
                  {
                      thrd[i].join(); //Wait for each thread to finish
                  }
                  catch(InterruptedException e){}
            }
            //End multithreading

            return result;
    }

    /**
     * @param n
     * @param t
     * @return
     */
    public static boolean runFromDriver(BigInteger n, int t) //Called from Driver
    {
        testNumber = n; //Assign input
        threads = t; //Assign input
        return isSemiPrime(); //Run test & return results
    }
}
