package src.pollardrho;

import java.math.BigInteger;
import java.util.Random;

public class PollardRho {

  private static String pollardRho(BigInteger n) {
      BigInteger a = new BigInteger("2"); //Set a = 2
      BigInteger b = new BigInteger("2"); //Set b = 2
      BigInteger d = BigInteger.ONE; //Set d = 1
      while (d.compareTo(BigInteger.ONE) == 0) { //While d is equal to 1
          //f(x) = x^2+1 mod n
          //a = f(a)
          a = a.pow(2); //Square a
          a = a.add(BigInteger.ONE); //Add 1
          a = a.mod(n); //Mod n

          //b = f(f(b))
          for(int i=0; i<2; i++){
              b = b.pow(2); //Square b
              b = b.add(BigInteger.ONE); //Add 1
              b = b.mod(n); //Mod n
          }

          //d = gcd(a-b, n)
          d = a;
          d = d.subtract(b);
          d = d.gcd(n);

          if (d.compareTo(n) == 0) { //If d is n, number is prime (See Fermat's Little Theorem)
              return "unable to be found."; //If prime factors "unable to be found"
          }
      }
      BigInteger factor = n.divide(d); //Divide out d from the original number
      return "" + d + " x " + factor; //Print d and the other factor
  }

	public static String runFromDriver(BigInteger n) //Called from Driver
  {
    BigInteger testNumber = n; //Assign input
    return pollardRho(testNumber); //Run test & return results
  }
}
