COMP 460, Spring 2018 — Final Project

Repo: https://github.com/ConwayJ18/comp460s18final/

Contents:

Driver.java — Used to run all aspects of the program at once.

src/
../dixon/Dixon.java - Contains all necessary elements for the Dixon algorithm
../logicalmatrix/LogicalMatrixMultiply.java - Contains all necessary elements for Logical Matrix Multiplication
../millerrabin/MillerRabin.java - Contains all necessary elements for Primality Testing
../pollardrho/PollardRho.java - Contains all necessary elements for the Pollard's Rho algorithm
../semiprime/SemiPrime.java - Contains all necessary elements for Semi-Prime Testing

src/singlethread/
../../DixonSingleThread.java - An archival file to run Dixon on a single thread
../../MillerRabinSingleThread.java - An archival file to run Primality Testing on a single thread
../../SemiPrimeSingleThread.java - An archival file to run Semi-Prime Testing on a single thread

Project Description:

This is a project designed to implement a set of Number Theoretic Algorithms on one or more threads.

The selected algorithms include the following:
1. The Miller-Rabin Primality Testing Algorithm
2, A standard Semi-Prime Testing Algorithm
3. The Pollard's Rho Factoring Algorithm
4. The Dixon Factoring Algorithm
5. A Logical (0,1) Matrix Multiplication Algorithm

Each of these programs is run by the Driver.java file and can be run using multiple threads if the user so desires.

Project Roles:
Jess Conway — Project lead. Wrote the base code for each algorithm.
Pranjali Mishra — Multithreaded each algorithm to run on multiple cores.
