package src.pollardrho;

import java.math.BigInteger;
import java.util.Random;

public class PollardRho {

  public static String pollardRho(BigInteger n) {
      BigInteger a = new BigInteger("2");
      BigInteger b = new BigInteger("2");
      BigInteger d = BigInteger.ONE;
      while (d.compareTo(BigInteger.ONE) == 0) {
          //a <- a^2+1 mod n
          a = a.pow(2);
          a = a.add(BigInteger.ONE);
          a = a.mod(n);

          //b <- b^2+1 mod n
          b = b.pow(2);
          b = b.add(BigInteger.ONE);
          b = b.mod(n);

          //d = gcd(a-b, n)
          d = a;
          d = d.subtract(b);
          d = d.gcd(n);

          if (d.compareTo(n) == 0) {
              return "Number is prime.";
          }
      }
      BigInteger factor = n.divide(d);
      return "" + d + " x " + factor;
  }

	public static void main(String[] args) //To test this class. Actual main() is in Driver.java
  {
    String[] nonPrimes = { "22", "333", "4444", "55555", "666666", "7777777", "88888888", "999999999", "1010101010" };

    for (String n : nonPrimes) //For each composite
    {
        BigInteger testComposite = new BigInteger(n);
        System.out.println("The factors of " + testComposite + " are " + pollardRho(testComposite));
    }
  }
}
