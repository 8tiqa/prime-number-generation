package primenumbergeneration.strategies;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

// SieveEratosthenes:  Prime Sieve
// there are many prime sieves. The simple sieve of Eratosthenes (250s BCE), the sieve of Sundaram (1934),
// the still faster but more complicated sieve of Atkin[1] (2004), and various wheel sieves[2] are most common.
// prime sieve works by creating a list of all integers up to a desired limit and progressively removing
// composite numbers (which it directly generates) until only primes are left
// source: https://en.wikipedia.org/wiki/Generating_primes


// Sieve_of_Eratosthenes
// includes a common optimization, which is to start enumerating the multiples of each prime i from i2.
// The time complexity of this algorithm is O(n log log n), provided the array update is an O(1) operation, as is usually the case.
// Space Complexity: O(n)

public class SieveEratosthenes extends Strategy {

    public List<Integer> generate(int startingValue, int endingValue) {
        primesList = new ArrayList<Integer>();

        int start = Math.min(startingValue, endingValue);
        int end = Math.max(startingValue, endingValue);

        if (end < 2) {
            return primesList;
        }

        if(start < 2) {
            start = 2;
        }

        // Let isPrime be an array of Boolean values, indexed by integers 2 to n,
        // initially all set to true.
        boolean[] isPrime = new boolean[end + 1];
        for (int i = 2; i <= end; i++) {
            isPrime[i] = true;
        }

        // Crossing out the multiples of each prime i
        for (int i = 2; i*i <= end; i++) { // i = 2, 3, 4, ..., not exceeding √n
            if (isPrime[i]) {
                for (int j = i*i; j <= end; j+=i) { // j = i2, i2+i, i2+2i, i2+3i, ..., not exceeding n
                    isPrime[j] = false;
                }
            }

            // if i is prime, then mark multiples of i as nonprime
            // suffices to consider multiples i, i+1, ..., N/i
//            if (isPrime[i]) {
//                for (int j = i; i*j <= end; j++) {
//                    isPrime[i*j] = false;
//                }
//            }
        }

        //  Output: all i such that isPrime[i] is true.
        for (int i = start; i <= end; i++) {
            if (isPrime[i] == true){
                primesList.add(i);
            }
        }

        return primesList;
    }

    // With BitSet
    public List<Integer> generateUsingBitSet(int startingValue, int endingValue) {
        primesList = new ArrayList<Integer>();

        int start = Math.min(startingValue, endingValue);
        int end = Math.max(startingValue, endingValue);

        if (end < 2) {
            return primesList;
        }

        if(start < 2) {
            start = 2;
        }
        final BitSet primes = new BitSet();
        primes.set( 0, false );
        primes.set( 1, false );
        primes.set( 2, end, true );


        for (int i = 2; i*i <= end; i++) { // i = 2, 3, 4, ..., not exceeding √n
            if (primes.get(i)) {
                for (int j = i * i; j <= end; j += i) { // j = i2, i2+i, i2+2i, i2+3i, ..., not exceeding n
                    primes.clear(j);
                }
            }
        }

        for (int i = primes.nextSetBit(start); i >= 0; i = primes.nextSetBit(i + 1)) {
            primesList.add(i);
            if (i == Integer.MAX_VALUE) {
                break;
            }
        }


        return primesList;
    }

    // Using Odd Numbers Only
    // The only even prime number is 2, all other primes are odd.
    // This observation saves 50% memory and is nearly twice as fast as the basic algorithm while requiring only minor code modifications.

}