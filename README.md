<html>
<h1>COMP 460, Spring 2018 &mdash; Final Project</h1>
<h2>Repo:</h2>
<p><a href="https://github.com/ConwayJ18/comp460s18final/">https://github.com/ConwayJ18/comp460s18final/</a></p>
<h2>Contents:</h2>
<table style="width: 594px;">
<tbody>
<tr>
<td style="width: 119px;"><strong>File Name</strong></td>
<td style="width: 465px;"><strong>File Description</strong></td>
</tr>
<tr>
<td style="width: 119px;">Driver.java</td>
<td style="width: 465px;">Used to run all aspects of the program at once.&nbsp;</td>
</tr>
<tr>
<td style="width: 119px;">src/dixon/Dixon.java</td>
<td style="width: 465px;">Contains all necessary elements for the Dixon algorithm</td>
</tr>
<tr>
<td style="width: 119px;">src/logicalmatrix/LogicalMatrixMultiply.java</td>
<td style="width: 465px;">Contains all necessary elements for Logical Matrix Multiplication</td>
</tr>
<tr>
<td style="width: 119px;">src/millerrabin/MillerRabin.java</td>
<td style="width: 465px;">Contains all necessary elements for Primality Testing</td>
</tr>
<tr>
<td style="width: 119px;">src/pollardrho/PollardRho.java</td>
<td style="width: 465px;">Contains all necessary elements for the Pollard's Rho algorithm</td>
</tr>
<tr>
<td style="width: 119px;">src/semiprime/SemiPrime.java</td>
<td style="width: 465px;">Contains all necessary elements for Semi-Prime Testing</td>
</tr>
<tr>
<td style="width: 119px;">src/singlethread/DixonSingleThread.java</td>
<td style="width: 465px;">An archival file to run Dixon on a single thread</td>
</tr>
<tr>
<td style="width: 119px;">src/singlethread/MillerRabinSingleThread.java</td>
<td style="width: 465px;">An archival file to run Primality Testing on a single thread</td>
</tr>
<tr>
<td style="width: 119px;">src/singlethread/SemiPrimeSingleThread.java</td>
<td style="width: 465px;">An archival file to run Semi-Prime Testing on a single thread</td>
</tr>
<tr>
<td style="width: 119px;">Documentation.pdf</td>
<td style="width: 465px;">The documentation associated with this project.</td>
</tr>
</tbody>
</table>
<h2>Project Description:</h2>
<p>This is a project designed to implement a set of Number Theoretic Algorithms on one or more threads.</p>
<p>The selected algorithms include the following:<br />
<ol>
<li>The Miller-Rabin Primality Testing Algorithm</li>
<li>A standard Semi-Prime Testing Algorithm</li>
<li>The Pollard's Rho Factoring Algorithm</li>
<li>The Dixon Factoring Algorithm</li>
<li>A Logical (0,1) Matrix Multiplication Algorithm</li></p>
</ol>
<p>Each of these programs is run by the Driver.java file and can be run using multiple threads if the user so desires.</p>
<h2>Project Roles:</h2>
<p>Jess Conway &mdash; Project lead. Wrote the base code for each algorithm.<br />Pranjali Mishra &mdash; Multithreaded each algorithm to run on multiple cores.</p>
<h2>How to run:</h2>
<ol>
<li>Compile and run Driver.java</li>
<li>Enter the number of cores (1-8) that you have on your computer
<ul><li>This will determine the number of threads used by the program</li></ul>
</li>
<li>Enter a number to run the non-matrix algorithms on</li>
<li>Enter a number to use as the logical matrix dimension</li>
<li>Repeat 3 & 4 as many times as desired, typing "0" when finished</li>
</ol>
</html>
