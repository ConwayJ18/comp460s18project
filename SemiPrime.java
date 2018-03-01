import java.math.*;
import java.util.Random;

public class SemiPrime {
    public static BigDecimal THREE_D=BigDecimal.valueOf(3); //Needed in cubeRoot calculation
    public static int UP=BigDecimal.ROUND_HALF_UP; //Rounds things up, also needed in cubeRoot
    public static int degreeOfCertainty = 40; //Higher numbers means more certainty

    public static boolean isSemiPrime(BigInteger n) //The actual test
    {
            if(!MillerRabin.isProbablePrime(n, degreeOfCertainty)) //Is it composite?
            {
                    if(!manyFactors(n)) //How many factors does it have?
                            return true; //If composite & only two factors, exist, return true
            }
            return false; //Else return false
    }

    public static BigInteger cubeRoot(BigInteger n) //Calculates cube roots
    {
        BigDecimal nCopy = new BigDecimal(n); //Create copy of n
        BigInteger result = BigInteger.ZERO.setBit(n.bitLength()/3); //Guess bitLength of result
        BigDecimal s = BigDecimal.ZERO; //Set s equal to 0
        while (!result.equals(s.toBigInteger())) //Is result equal to s?
        {
            s = new BigDecimal(result); //Set s equal to BigDecimal version of result
        
            BigDecimal temp0 = new BigDecimal(result.shiftLeft(1)); //Do some math
            BigDecimal temp1 = temp0.add(nCopy.divide(s.multiply(s), UP));
            BigDecimal temp2 = temp1.divide(THREE_D, UP);
            
            result = temp2.toBigInteger(); //Convert things back to BigInteger
        }
        return result;  //Finally return r
    }

    public static boolean manyFactors(BigInteger n) //How many factors does n have?
    {
            BigInteger cbrt = cubeRoot(n); //First calculate the cube root
            BigInteger i = new BigInteger("2"); //Initialize the incrementer
            BigInteger m = null; //We'll need this later
            while(i.compareTo(cbrt)<=0) //For 2 <= i <= cbrt(n)
            {
                if(n.mod(i).equals(BigInteger.ZERO)) //Is i a factor of n?
                {
                    if(!MillerRabin.isProbablePrime(i,degreeOfCertainty)) //If so, is i composite?
                    {
                        return true; //Then n has more than 2 factors
                    }
                    else
                    {
                        m = n.divide(i);
                        if(!MillerRabin.isProbablePrime(m, degreeOfCertainty)) //What about n/i?
                        {
                            return true; //Then n still has more than 2 factors
                        }
                    }
                }
                i = i.add(BigInteger.ONE); //Increment i
            }
            return false; //If we make it this far, there's only 2 factors, both of which are prime
    }

  public static void main(String[] args) //We'll use the main method for testing. Actual main() is in Driver.java
  {
        String[] semiPrimes = { "4", "6", "3901", "12193", "8602133", "48883987" }; //We know these are semi-prime
        String[] nonSemis = { "5", "7", "5581", "902937", "1295739", "10483595" }; //We know these are not
        for (String p : semiPrimes) //For each semiPrime, run the test
        {
            BigInteger testSemiPrime = new BigInteger(p);
            if(isSemiPrime(testSemiPrime))
            {
                System.out.println(testSemiPrime + " is semi-prime."); //We should always get this one
            }
            else
            {
                System.out.println(testSemiPrime + " is not semi-prime.");
            }
        }

        for (String n : nonSemis) //For each non-semiPrime, run the test
        {
            BigInteger testNonSemi = new BigInteger(n);
            if(isSemiPrime(testNonSemi))
            {
                System.out.println(testNonSemi + " is semi-prime.");
            }
            else
            {
                System.out.println(testNonSemi + " is not semi-prime."); //We should always get this one
            }
        }
  }
}
