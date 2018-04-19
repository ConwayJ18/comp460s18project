package src.semiprime;

import java.math.*;
import java.util.Random;
import src.millerrabin.*;

public class SemiPrime extends Thread {
    private static BigDecimal THREE_D=BigDecimal.valueOf(3); //Needed in cubeRoot calculation
    private static int UP=BigDecimal.ROUND_HALF_UP; //Rounds things up, also needed in cubeRoot
    private static BigInteger testNumber;
    private static BigInteger cbrt;
    private static int degreeOfCertainty = 100; //Higher numbers means more certainty
    private static int threads;
    private static boolean result = false;
    private int threadNumber;

    private SemiPrime(int threadNumber)
    {
       this.threadNumber=threadNumber;
    }

    public void run()
    {
        BigInteger i = BigInteger.valueOf(threadNumber+2); //Initialize the incrementer
        BigInteger m = null; //We'll need this later
        BigInteger bigThreads = BigInteger.valueOf(threads);

        while(i.compareTo(cbrt)<=0) //For 2 <= i <= cbrt(n)
        {
            if(testNumber.mod(i).equals(BigInteger.ZERO)) //Is i a factor of n?
            {
                if(!MillerRabin.isProbablePrime(i)) //If so, is i composite?
                {
                    result = true; //Then n has more than 2 factors
                }
                else
                {
                    m = testNumber.divide(i);
                    if(!MillerRabin.isProbablePrime(m)) //What about n/i?
                    {
                        result = true; //Then n still has more than 2 factors
                    }
                }
            }
            i = i.add(bigThreads); //Increment i
        }
    }

    private static boolean isSemiPrime() //The actual test
    {
        if(!MillerRabin.isProbablePrime(testNumber)) //Is it composite?
        {
                cbrt = cubeRoot(testNumber); //Calculate the cube root
                if(!manyFactors(testNumber)) //How many factors does it have?
                        return true; //If composite & only two factors, exist, return true
        }
        return false; //Else return false
    }

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

    private static boolean manyFactors(BigInteger n) //How many factors does n have?
    {
            SemiPrime[] thrd = new SemiPrime[threads];

            //Start multithreading
            for(int i=0;i<threads;i++)
            {
               thrd[i] = new SemiPrime(i);
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
            //End multithreading

            return result;
    }

    public static boolean runFromDriver(BigInteger n, int t)
    {
        testNumber = n;
        threads = t;
        return isSemiPrime();
    }
}
