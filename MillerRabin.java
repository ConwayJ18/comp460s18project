import java.math.BigInteger;
import java.util.Random;

public class MillerRabin {

	private static final BigInteger ZERO = BigInteger.ZERO; //BigInteger copy of ZERO
	private static final BigInteger ONE = BigInteger.ONE; //Same for ONE
	private static final BigInteger TWO = new BigInteger("2"); //TWO
	private static final BigInteger THREE = new BigInteger("3"); //And THREE
	private static final int degreeOfCertainty = 40; //Higher numbers reduce chance of false-positive

	public static boolean isProbablePrime(BigInteger n, int degreeOfCertainty) //The actual calculator
	{
			//The first two are special cases
  		if (n.compareTo(ONE) == 0) //Is the number 1?
  			 return false; //Then it's not prime

      if (n.compareTo(THREE) < 0) //Is it less than 3?
  			 return true; //Then it is prime

			//Start the tough stuff, write n-1 = 2^s + d
      int s = 0;
  		BigInteger d = n.subtract(ONE); //Start with d = n-1
  		while (d.mod(TWO).equals(ZERO)) //While still even
			{
    			s++;	//Increment exponent
    			d = d.divide(TWO); //Divide by 2
  		}

			//Now choose k=degreeOfCertainty random numbers between 2 and n-2
      for (int i = 0; i < degreeOfCertainty; i++) //For each of those numbers
			{
    			BigInteger a = uniformRandom(TWO, n.subtract(ONE)); //Assign it a value
    			BigInteger x = a.modPow(d, n); //Calculate x = a^d mod n

          if (x.equals(ONE) || x.equals(n.subtract(ONE))) //If x=1 or x=n-1
    				  continue; //Stop here, move on to the next random number

          int r;
    			for (r=0; r < s; r++) //This part runs s-1 times
					{
      				x = x.modPow(TWO, n); //Calculate (new) x = (old) x^2 mod n

      				if (x.equals(ONE)) //If this new x=1
      					   return false;	//The number is composite

      				if (x.equals(n.subtract(ONE))) //If this new x=n-1
      					   break; //Stop here, move on to next random number
    			}
    			if (r == s) //If for some k, no r made x=n-1, n is composite
    				  return false;
  		}
  		return true; //If we make it all the way here, n is probably prime
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

	public static void main(String[] args) //To test this class. Actual main() is in Driver.java
  {
  		String[] primes = { "2", "3", "3613", "7297", "226673591177742970257407", "2932031007403" }; //Known primes
  		String[] nonPrimes = { "3341", "2932021007403", "226673591177742970257405" }; //Known composites
  		for (String p : primes) //For each prime
      {
          BigInteger testPrime = new BigInteger(p);
          if(isProbablePrime(testPrime, degreeOfCertainty))
          {
              System.out.println(testPrime + " is probably prime"); //Should get this
          }
          else
          {
              System.out.println(testPrime + " is composite");
          }
      }

  		for (String n : nonPrimes) //For each composite
      {
          BigInteger testComposite = new BigInteger(n);
          if(isProbablePrime(testComposite, degreeOfCertainty))
          {
              System.out.println(testComposite + " is probably prime");
          }
          else
          {
              System.out.println(testComposite + " is composite"); //Should get this
          }
      }
  }
}
