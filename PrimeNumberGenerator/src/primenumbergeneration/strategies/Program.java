package primenumbergeneration.strategies;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        /*concise, efficient and robust prime number generator written in Java that gives all prime numbers
        in the range between two numbers provided by the user (e.g. user gives 1 and 10 and you return "2, 3, 5, 7").

        You should offer the user at least 3 different prime number generation strategies with different performance optimizations.
        Please include some simple but slow strategy and some others that increase performance at the expense of increased complexity.
        Consider also the use of multiple cores: if there is a gain, you could offer also a parallel implementation.
        Think also about memory usage (besides of execution time).

        Include some unit tests to validate the correctness of the program.

        Make your program usable via the command-line, allowing the user to select the generation strategy.
        */

        if (!(args.length>0))
        {
            System.out.println("Error: Invalid Arguments, ExpectingStrategy# ");
            return;
        }


        Strategy ruteForce = new BruteForce();

        // Primality test Approach 1: Execution time
        long startTime = System.nanoTime();
        primeNumberGenerator.isPrimeClassic(500);
        long endTime = System.nanoTime();
        System.out.println("Classic Primality Test took " + (endTime - startTime) + " nanosecs.");

        // Primality test Approach 2: Execution time
        startTime = System.nanoTime();
        primeNumberGenerator.isPrime(500);
        endTime = System.nanoTime();
        System.out.println("Optimized Primality Test took " + (endTime - startTime) + " nanosecs.");

        //input start and end values
        int startingValue = 5;
        int endingValue = 500;

        // Brute Force: Execution time
        startTime = System.nanoTime();
        List<Integer> generatedPrimesList1 = primeNumberGenerator.generate(startingValue, endingValue);
        endTime = System.nanoTime();
        System.out.println("Brute Force Generated primes between " + startingValue + " and " + endingValue + " inclusive: total:" + generatedPrimesList1.size() +" list:"+ generatedPrimesList1.toString());
        System.out.println("Time Taken " + (endTime - startTime) + " nanosecs.");

        // Sieve Eratosthenes: Execution time
        SieveEratosthenes Eratosthenes = new SieveEratosthenes();
        startTime = System.nanoTime();
        List<Integer> generatedPrimesList = Eratosthenes.generate(startingValue, endingValue);
        endTime = System.nanoTime();
        System.out.println("Eratosthenes Generated primes between " + startingValue + " and " + endingValue + " inclusive: total:" + generatedPrimesList.size() +" list:"+ generatedPrimesList.toString());
        System.out.println("Time Taken " + (endTime - startTime) + " nanosecs.");


        // Sieve Eratosthenes using BitSet: Execution time
        startTime = System.nanoTime();
        generatedPrimesList = Eratosthenes.generateUsingBitSet(startingValue, endingValue);
        endTime = System.nanoTime();
        System.out.println("Eratosthenes BitSet Generated primes between " + startingValue + " and " + endingValue + " inclusive: total:" + generatedPrimesList.size() +" list:"+ generatedPrimesList.toString());
        System.out.println("Time Taken " + (endTime - startTime) + " nanosecs.");

        // Sieve Eratosthenes Segmented: Execution time
        primeNumberGenerator = new SieveEratosthenesMem();
        startTime = System.nanoTime();
        generatedPrimesList = primeNumberGenerator.generate(startingValue, endingValue);
        endTime = System.nanoTime();
        System.out.println("Eratosthenes Seg Generated primes between " + startingValue + " and " + endingValue + " inclusive: total:" + generatedPrimesList.size() +" list:"+ generatedPrimesList.toString());
        System.out.println("Time Taken " + (endTime - startTime) + " nanosecs.");

        // Sieve Eratosthenes Parallel: Execution time
        primeNumberGenerator = new SieveEratosthenesParallel();
        startTime = System.nanoTime();
        List<Integer> generatedPrimesList2 = primeNumberGenerator.generate(startingValue, endingValue);
        endTime = System.nanoTime();
        System.out.println("Eratosthenes Parallel Generated primes between " + startingValue + " and " + endingValue + " inclusive: total:" + generatedPrimesList2.size() +" list:"+ generatedPrimesList2.toString());
        System.out.println("Time Taken " + (endTime - startTime) + " nanosecs.");

        Collection<Integer> similar = new HashSet<Integer>( generatedPrimesList1 );
        Collection<Integer> different = new HashSet<Integer>();
        different.addAll( generatedPrimesList1 );
        different.addAll( generatedPrimesList2 );

        similar.retainAll( generatedPrimesList2 );
        different.removeAll( similar );

        System.out.printf("Different:"+ different);


    }
}
