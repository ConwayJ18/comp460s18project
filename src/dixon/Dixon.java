package src.dixon;

import java.io.File;
import java.io.IOException;
import java.math.*;
import java.util.Random;
import java.util.Scanner;

public class Dixon extends Thread {

    public static int level = 0; //Tracks number of attempts generating factor base
    public static final int RANDOM_POWER = 15; //As described
    private static final double LOG2 = Math.log(2.0); //Log 2
    public static BigInteger[] primes = new BigInteger[100000]; //Will hold primes
    private static BigInteger testNumber; //Number being factored
    private static double logN; //Log of number being factored
    private static BigInteger smoothnessBound; //Needed for smoothness calculation
    private static BigInteger factorBase; //Size of factor base
    private static int threads; //Number of threads
    private int threadNumber;

    private Dixon(int threadNumber)
    {
        this.threadNumber = threadNumber; //Thread constructor
    }

    public void run() //Multithreaded code
    {
        try {
            Scanner input = new Scanner(new File("src/dixon/primes.txt")); //Open prime file
            int i = threadNumber;
            for(int j = 0; j < threadNumber; j++)
            {
                input.nextLine(); //Start at the appropriate line
            }
            while (input.hasNextLine()) {
                String line = input.nextLine(); //Grab the next line
                if (!line.isEmpty()) {
                    primes[i] = new BigInteger(line); //Put the number in the array
                    i++;
                }
                for(int j = 0; i<threads; j++)
                {
                  if(input.hasNextLine()) //Careful not to throw an error
                    input.nextLine(); //Skip the lines grabbed by other threads
                  else
                    break;
                }
            }
        } catch (IOException error) {
            System.out.println("Error in processing the file primes.txt" + error); //Hopefully this doesn't happen
        }
    }

    private static void loadPrimes() {
        //Start multithreading
        Dixon[] thrd = new Dixon[threads];
        for(int i=0;i<threads;i++)
        {
           thrd[i] = new Dixon(i); //Fill thread array
           thrd[i].start(); //Start all of them
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
    }

    private static String dixonsAlgorithm(BigInteger n, BigInteger factorBase) {
        level++; //Increment attempts
        Random generator = new Random();
        int arraySize = factorBase.intValue();
        BigInteger two = new BigInteger("2");
        int foundEqs = 0;

        int[][] equations = new int[arraySize][arraySize];
        BigInteger[] xVals = new BigInteger[arraySize];
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                equations[i][j] = 0; //Initializes every equation entry as 0
            }
        }

        int[] tempEq = new int[arraySize];
        while (foundEqs < factorBase.intValue()) {
            BigInteger x = new BigInteger(RANDOM_POWER, generator);
            BigInteger x2modn = x.modPow(two, n);
            //Reject since random x was less than sqrt(n) so the x^2 mod n = 0
            if (x2modn.compareTo(BigInteger.ZERO) == 0) {
                continue;
            }

            //Get rid of the previous equation
            for (int i = 0; i < tempEq.length; i++) {
                tempEq[i] = 0;
            }

            //Tries to divide the working value by all the primes in the factor base
            for (int i = 0; i < factorBase.intValue(); i++) {
                //If a prime in the factorbase can divide the working value, keep trying to divide it in
                while (x2modn.divideAndRemainder(primes[arraySize - 1 - i])[1] == BigInteger.ZERO && x2modn.intValue() != 1) {
                    //Increment the power value and update the working value
                    tempEq[arraySize - 1 - i]++;
                    x2modn = x2modn.divideAndRemainder(primes[arraySize - 1 - i])[0];
                }
            }
            //If the working value = 1 after all the factor base has been passed over the equation is good
            if (x2modn.intValue() == 1) {
                xVals[foundEqs] = x;
                BigInteger x2 = x.modPow(two, n);
                System.arraycopy(tempEq, 0, equations[foundEqs], 0, factorBase.intValue());
                foundEqs++;
            }
        }

        int[][] mod2Equations = mod2Eqs(equations, arraySize); //Recalculates equations modulo 2
        int[][] identity = identityMatrix(arraySize); //Generates appropriately sized identity matrix
        int[][] iAdjusted = gaussianEliminationMod2(mod2Equations, identity, arraySize, arraySize, true);
        int[][] cAdjusted = gaussianEliminationMod2(mod2Equations, identity, arraySize, arraySize, false);

        return combineEqs(xVals, equations, cAdjusted, iAdjusted, arraySize, n); //Combines equations and returns results
    }

    private static int[][] mod2Eqs(int[][] equations, int arraySize) { //Calculates equations mod 2
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                if (equations[i][j] % 2 == 0) {
                    equations[i][j] = 0; //If even coefficient
                } else {
                    equations[i][j] = 1; //If odd coefficient
                }
            }
        }
        return equations;
    }

    private static String combineEqs(BigInteger[] xVals, int[][] equations, int[][] cAdjusted, int[][] iAdjusted, int arraySize, BigInteger n) {
        for (int i = 0; i < arraySize; i++) { //Goes down
            BigInteger x = BigInteger.ONE;
            BigInteger y = BigInteger.ONE;
            if (isRowEmpty(cAdjusted[i])) {
                for (int j = 0; j < arraySize; j++) { //Goes across
                    if (iAdjusted[i][j] == 1) {
                        x = x.multiply(xVals[j]);
                    }
                    if (iAdjusted[i][j] == 1) {
                        for (int k = 0; k < arraySize; k++) { //Goes across equation
                            y = y.multiply(primes[k].pow(equations[j][k]));
                        }
                    }
                }

                x = x.mod(n);
                y = sqrt(y).mod(n);

                if (x.compareTo(y) != 0) {
                    BigInteger gcd = x.subtract(y).abs().gcd(n);
                    if (gcd.compareTo(BigInteger.ONE) != 0) {
                        BigInteger factor = n.divide(gcd);
                        return "" + gcd + " x " + factor;
                    }
                }
            }
        }
        if (level >= 3) { //If we've tried more than twice
            return "unable to be found";
        } else {
            dixonsAlgorithm(n, new BigInteger(arraySize + ""));
        }
        return "unable to be found";
    }

    private static boolean isRowEmpty(int[] row) { //Checks if a row is empty
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {
                return false;
            }
        }
        return true;
    }

    private static int[][] identityMatrix(int size) { //Creates an appropriately sized identity
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = (i == j) ? 1 : 0; //Put 1's on the diagonal
            }
        }
        return matrix;
    }

    private static BigInteger sqrt(BigInteger n) { //Calculate sqrt, the fast way
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
    private static int[][] gaussianEliminationMod2(int[][] matrix, int[][] iMatrix, int ROWS, int COLS, boolean i) {
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

    private static double logBigInteger(BigInteger val) { //Calculates log of the BigInteger, the fast way
        int blex = val.bitLength() - 1022; //Any value from 60 - 1023 is cool
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * LOG2 : res;
    }

    public static String runFromDriver(BigInteger n, int t)
    {
            testNumber = n; //Assign input
            threads = t; //Assign input
            loadPrimes(); //Load primes
            logN = logBigInteger(testNumber); //Calculate logN
            smoothnessBound = BigInteger.valueOf(Math.round(logN * Math.log(logN))); //Ideal smoothnessBound = log(N) * log(log(N))
            int i = 0;
            while(primes[i].compareTo(smoothnessBound) <= 0) //While primes in array are smaller than smoothnessBound
            {
                i++; //Keep going
            }
            factorBase = BigInteger.valueOf(i).add(BigInteger.ONE); //Size of the factor base is the number of primes smaller than the smoothness bound

            return dixonsAlgorithm(testNumber, factorBase); //Run the code and return the results
    }
}
