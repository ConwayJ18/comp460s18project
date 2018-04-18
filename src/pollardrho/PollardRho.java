package src.pollardrho;

import java.math.BigInteger;
import java.util.Random;

public class PollardRho {

  public static String pollardRho(BigInteger n) {
      BigInteger a = new BigInteger("2");
      BigInteger b = new BigInteger("2");
      BigInteger d = BigInteger.ONE;
      while (d.compareTo(BigInteger.ONE) == 0) { //While d is equal to 1
          //a <- a^2+1 mod n
          a = a.pow(2); //Square a
          a = a.add(BigInteger.ONE); //Add 1
          a = a.mod(n); //Mod n

          //b <- b^2+1 mod n
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
              return "Number is prime.";
          }
      }
      BigInteger factor = n.divide(d); //Divide out d from the original number
      return "" + d + " x " + factor; //Print d and the other factor
  }

	public static String runFromDriver(BigInteger n) //To test this class. Actual main() is in Driver.java
  {
    BigInteger testNumber = n;
    return pollardRho(testNumber);
  }
}
