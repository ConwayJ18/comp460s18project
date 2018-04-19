package src.millerrabin;

import java.math.BigInteger;
import java.util.Random;

public class MillerRabin extends Thread {

	private static final BigInteger ZERO = BigInteger.ZERO; //BigInteger copy of ZERO
	private static final BigInteger ONE = BigInteger.ONE; //Same for ONE
	private static final BigInteger TWO = new BigInteger("2"); //TWO
	private static final BigInteger THREE = new BigInteger("3"); //And THREE
	private static final int degreeOfCertainty = 40; //Higher numbers reduce chance of false-positive
	private static BigInteger testNumber;
	private static boolean result = true;
	private static int threads;
	private static int s = 0;
	private static BigInteger d;
	public int threadNumber;

	private MillerRabin(int threadNumber)
	{
		this.threadNumber = threadNumber;
	}

	public void run()
	{
			for (int i = threadNumber; i < degreeOfCertainty; i+=threads) //For each of those numbers
			{
					BigInteger a = uniformRandom(TWO, testNumber.subtract(ONE)); //Assign it a value
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

	private static boolean isProbablePrime() //The actual calculator
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
				 thrd[i] = new MillerRabin(i);
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

  		return result; //If we make it all the way here, n is probably prime
	}

	public static boolean isProbablePrime(BigInteger n) //To be used outside the Driver
	{
			testNumber = n;
			return isProbablePrime();
	}

	private static BigInteger uniformRandom(BigInteger bottom, BigInteger top) //Used to generate random numbers
  {
  		Random rnd = new Random();
  		BigInteger res;
  		do {
  			   res = new BigInteger(top.bitLength(), rnd);
  		} while (res.compareTo(bottom) < 0 || res.compareTo(top) > 0);
  		return res;
	}

	public static boolean runFromDriver(BigInteger n, int t)
	{
			testNumber = n;
			threads = t;
			return isProbablePrime();
	}
}
