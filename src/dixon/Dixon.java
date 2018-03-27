package src.dixon;

import java.io.File;
import java.io.IOException;
import java.math.*;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Dixon {

    public static int level = 0;
    public static final int RANDOM_POWER = 15;
    public static ArrayList<BigInteger> primes = new ArrayList<BigInteger>();

    public static int primeCounter(BigInteger n) {
        int i=0;
        BigInteger prime = BigInteger.ONE.nextProbablePrime();
        while(prime.compareTo(n) <= 0)
        {
          primes.add(prime);
          prime = prime.nextProbablePrime();
          i++;
        }

        return i;
    }

    public static String dixon(BigInteger n) {
        level++;
        int factorBase = primeCounter(n);
        Random generator = new Random();
        int arraySize = factorBase;
        BigInteger two = new BigInteger("2");
        int foundEqs = 0;

        int[][] equations = new int[arraySize][arraySize];
        BigInteger[] xVals = new BigInteger[arraySize];
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                equations[i][j] = 0;
            }
        }

        int[] tempEq = new int[arraySize];
        while (foundEqs < factorBase) {
            BigInteger x = new BigInteger(RANDOM_POWER, generator);
            BigInteger x2modn = x.modPow(two, n);
            //reject since random x was less than sqrt(n) so the x^2 mod n = 0
            if (x2modn.compareTo(BigInteger.ZERO) == 0) {
                continue;
            }

            //Get rid of any traces of last equation
            for (int i = 0; i < tempEq.length; i++) {
                tempEq[i] = 0;
            }

            //Tries to divide the working value by all the primes in the factor base
            for (int i = 0; i < factorBase; i++) {
                //If a prime in the factorbase can divide the working value, keep trying to divide it in
                while (x2modn.divideAndRemainder(primes.get(arraySize - 1 - i))[1] == BigInteger.ZERO && x2modn.intValue() != 1) {
                    //Increment the power value and update the working value
                    tempEq[arraySize - 1 - i]++;
                    x2modn = x2modn.divideAndRemainder(primes.get(arraySize - 1 - i))[0];
                }
            }
            //If the working value = 1 after all the factor base has been passed over the equation is good
            if (x2modn.intValue() == 1) {
                xVals[foundEqs] = x;
                BigInteger x2 = x.modPow(two, n);
                System.arraycopy(tempEq, 0, equations[foundEqs], 0, factorBase);
                foundEqs++;
            }
        }

        //Output data
        System.out.println("Done generating factor base, the factor base has " + arraySize + " primes.");
        //System.out.print("                         ");
        for (int i = 0; i < arraySize; i++) {
            //System.out.printf("%2d ", primes[i]);
        }
        //System.out.println("");
        for (int i = 0; i < xVals.length; i++) {
            //System.out.printf("%7d === %10d   ", xVals[i], xVals[i].modPow(two, n));
            for (int j = 0; j < arraySize; j++) {
                //System.out.printf("%2d ", equations[i][j]);
            }
            //System.out.println();
        }

        int[][] mod2Equations = mod2Eqs(equations, arraySize);
        int[][] identity = identityMatrix(arraySize);
        int[][] iAdjusted = gaussianEliminationMod2(mod2Equations, identity, arraySize, arraySize, true);
        int[][] cAdjusted = gaussianEliminationMod2(mod2Equations, identity, arraySize, arraySize, false);

        return combineEqs(xVals, equations, cAdjusted, iAdjusted, arraySize, n);
    }

    public static int[][] mod2Eqs(int[][] equations, int arraySize) {
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                if (equations[i][j] % 2 == 0) {
                    equations[i][j] = 0;
                } else {
                    equations[i][j] = 1;
                }
            }
        }
        return equations;
    }

    public static String combineEqs(BigInteger[] xVals, int[][] equations, int[][] cAdjusted, int[][] iAdjusted, int arraySize, BigInteger n) {
        for (int i = 0; i < arraySize; i++) { //Goes down
            BigInteger x = BigInteger.ONE;
            BigInteger y = BigInteger.ONE;
            if (isRowEmpty(cAdjusted[i])) {
                //System.out.println("EQ: " + i);
                for (int j = 0; j < arraySize; j++) { //Goes across
                    if (iAdjusted[i][j] == 1) {
                        x = x.multiply(xVals[j]);
                    }
                    if (iAdjusted[i][j] == 1) {
                        for (int k = 0; k < arraySize; k++) { //Goes across equation
                            //System.out.println(primes[k] + "^" + equations[j][k]);
                            y = y.multiply(primes.get(k).pow(equations[j][k]));
                        }
                    }
                }

                x = x.mod(n);
                y = sqrt(y).mod(n);
                //System.out.println("final x: " + x);
                //System.out.println("final y: " + y);

                if (x.compareTo(y) != 0) {
                    BigInteger gcd = x.subtract(y).abs().gcd(n);
                    if (gcd.compareTo(BigInteger.ONE) != 0) {
                        //System.out.println("GCD: " + gcd);
                        BigInteger factor = n.divide(gcd);
                        String returnString = gcd + " x " + factor;
                        return returnString;
                    }
                }
            }
        }
        System.out.println("***Factor base did not work. Attempt " + level + "\n");
        if (level >= 3) {
            System.out.println("Unable to find factors");
        } else {
            dixon(n);
        }
        return "";
    }

    public static boolean isRowEmpty(int[] row) {
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public static int[][] identityMatrix(int size) {
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (i == j) ? 1 : 0;
            }
        }
        return matrix;
    }

    public static BigInteger sqrt(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
        while (b.compareTo(a) >= 0) {
            BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
            if (mid.multiply(mid).compareTo(n) > 0) {
                b = mid.subtract(BigInteger.ONE);
            } else {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a.subtract(BigInteger.ONE);
    }

    // performs Gaussian elimination mod 2 on ROWSxCOLS matrix and ROWSxROWS identity matrix
    public static int[][] gaussianEliminationMod2(int[][] matrix, int[][] iMatrix, int ROWS, int COLS, boolean i) {
        for (int col = 0; col < COLS; col++) {
            // first get a 1 in the current diagonal entry
            if (matrix[col][col] == 0) {
                for (int row = col + 1; row < ROWS; row++) {
                    if (matrix[row][col] == 1) {
                        // swap c[col][*] with c[row][*]
                        for (int k = 0; k < COLS; k++) {
                            int tmp = matrix[row][k];
                            matrix[row][k] = matrix[col][k];
                            matrix[col][k] = tmp;
                        }
                        // do the same for the identity matrix
                        for (int k = 0; k < ROWS; k++) {
                            int tmp = iMatrix[row][k];
                            iMatrix[row][k] = iMatrix[col][k];
                            iMatrix[col][k] = tmp;
                        }
                        // found a pivot and swapped, no need to check more rows
                        break;
                    }
                }
            }
            // if we found a 1 and did a swap, try to zero out everything below
            if (matrix[col][col] == 1) {
                for (int row = col + 1; row < ROWS; row++) {
                    // if we've got a 1 below the pivot, add rows mod 2
                    // in both matrices
                    if (matrix[row][col] == 1) {
                        for (int k = 0; k < COLS; k++) {
                            matrix[row][k] = (byte) ((matrix[row][k]
                                    + matrix[col][k]) % 2);
                        }
                        for (int k = 0; k < COLS; k++) {
                            iMatrix[row][k] = (byte) ((iMatrix[row][k]
                                    + iMatrix[col][k]) % 2);
                        }
                    }
                }
            }
        }
        if (i) {
            return iMatrix;
        } else {
            return matrix;
        }
    }

    public static void main(String args[])
    {
  		String[] nonPrimes = { "22", "333", "4444", "55555", "666666", "7777777" }; //Known composites
  		for (String n : nonPrimes) //For each composite
      {
          BigInteger testComposite = new BigInteger(n);
          System.out.println("The factors of " + testComposite + " are " + dixon(testComposite));
      }
    }
}
