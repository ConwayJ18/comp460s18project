package src.millerrabin;

import java.math.BigInteger;
import java.util.Random;

/**
 * 
 * Class implementing Miller Rabin Algorithm
 * 
 */
public class MillerRabin extends Thread {

	private static final BigInteger ZERO = BigInteger.ZERO; //BigInteger copy of ZERO
	private static final BigInteger ONE = BigInteger.ONE; //Same for ONE
	private static final BigInteger TWO = new BigInteger("2"); //TWO
	private static final BigInteger THREE = new BigInteger("3"); //And THREE
	private static final int degreeOfCertainty = 40; //Higher numbers reduce chance of false-positive
	private static BigInteger testNumber; //Number being tested
	private static boolean result = true; //Shared among threads
	private static int threads; //Number of threads
	private static int s = 0; //Needed by threads
	private static BigInteger d; //Also needed by threads
	public int threadNumber;

	/**
	 * @param threadNumber
	 */
	private MillerRabin(int threadNumber) //Tread constructor
	{
		this.threadNumber = threadNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() //Multithreaded part of code
	{
			for (int i = threadNumber; i < degreeOfCertainty; i+=threads) //Run degreeOfCertainty times
			{
					BigInteger a = uniformRandom(TWO, testNumber.subtract(ONE)); //Grab a random number a
					BigInteger x = a.modPow(d, testNumber); //Calculate x = a^d mod n

					if (x.equals(ONE) || x.equals(testNumber.subtract(ONE))) //If x=1 or x=n-1
							continue; //Stop here, move on to the next random number

					int r;
					for (r=0; r < s; r++) //This part runs s-1 times
					{
							x = x.modPow(TWO, testNumber); //Calculate (new) x = (old) x^2 mod n

							if (x.equals(ONE)) //If this new x=1
							{
									 result = false;	//The number is composite
									 break;
							}

							if (x.equals(testNumber.subtract(ONE))) //If this new x=n-1
									 break; //Stop here, move on to next random number
					}
					if (r == s) //If for some k, no r made x=n-1, n is composite
					{
							result = false;
							break;
					}
			}
	}

	/**
	 * @return boolean
	 * Method -The actual calculator
	 */
	private static boolean isProbablePrime()
	{
			//The first two are special cases
  		if (testNumber.compareTo(ONE) == 0) //Is the number 1?
  			 return false; //Then it's not prime

      if (testNumber.compareTo(THREE) < 0) //Is it less than 3?
  			 return true; //Then it is prime

			//Start the tough stuff, write n-1 = 2^s + d
  		d = testNumber.subtract(ONE); //Start with d = n-1
  		while (d.mod(TWO).equals(ZERO)) //While still even
			{
    			s++;	//Increment exponent
    			d = d.divide(TWO); //Divide by 2
  		}

			//Start multithreading
			MillerRabin[] thrd = new MillerRabin[threads];
			for(int i=0;i<threads;i++)
			{
				 thrd[i] = new MillerRabin(i); //Fill threads array
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

  		return result; //Result with be true if probably prime
	}

	/**
	 * @param bottom
	 * @param top
	 * @return BigInteger
	 * Method Used to generate random numbers
	 */
	private static BigInteger uniformRandom(BigInteger bottom, BigInteger top) 
  {
  		Random rnd = new Random();
  		BigInteger res;
  		do {
  			   res = new BigInteger(top.bitLength(), rnd);
  		} while (res.compareTo(bottom) < 0 || res.compareTo(top) > 0);
  		return res;
	}

	/**
	 * @param n
	 * @param t
	 * @return 
	 * Method Used by SemiPrime.java
	 */
	public static boolean isProbablePrime(BigInteger n, int t) 
	{
			testNumber = n; //Assign input
			threads = t; //Assign input
			return isProbablePrime(); //Run test & return results
	}

	/**
	 * @param n
	 * @param t
	 * @return
	 * Method Used by Driver
	 */
	public static boolean runFromDriver(BigInteger n, int t) 
	{
			testNumber = n; //Assign input
			threads = t; //Assign input
			return isProbablePrime(); //Run test & return results
	}
}
