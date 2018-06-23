package primenumbergeneration.strategies;

import static java.lang.Math.sqrt;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.List;


// SieveEratosthenesMem:  Segmented Sieve_of_Eratosthenes
// The time complexity of this algorithm is O(n log log n), provided the array update is an O(1) operation, as is usually the case.
// Space Complexity: O(sqrt(n))

public class SieveEratosthenesMem extends Strategy {

    private void simpleSieve(int start, int end , List<Integer> prime)
    {
        boolean[] isPrime = new boolean[end + 1];
        for (int i = 2; i <= end; i++) {
            isPrime[i] = true;
        }

        for (int i = 2; i*i <= end; i++) { // i = 2, 3, 4, ..., not exceeding √n
            if (isPrime[i]) {
                for (int j = i*i; j <= end; j+=i) { // j = i2, i2+i, i2+2i, i2+3i, ..., not exceeding n
                    isPrime[j] = false;
                }
            }
        }
        for (int i = start; i <= end; i++) {
            if (isPrime[i] == true){
                prime.add(i);
            }
        }
    }

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

        // Compute all primes smaller than or equal to square root of n using simple sieve
        int limit = (int) (floor(sqrt(end))+1);
        simpleSieve(start, limit, primesList);

        // Divide the range [0..n-1] in different segments of size sqrt(n) each.
        int low  = limit;
        int high = 2*limit;

        // While all segments of range [0..n-1] are not processed, process one segment at a time
        while (low < end)
        {
            if (high >= end)
                high = end;

            // To mark primes in current range. A value in mark[i]
            // will finally be false if 'i-low' is Not a prime,
            // else true.
            boolean isPrime[] = new boolean[limit+1];

            for (int i = 0; i < isPrime.length; i++)
                isPrime[i] = true;

            // Use the found primes by simpleSieve() to find
            // primes in current range
            for (int i = 0; i < primesList.size(); i++)
            {
                // Find the minimum number in [low..high] that is a multiple of prime.get(i)
                int loLim = (int) (floor(low/primesList.get(i)) * primesList.get(i));
                if (loLim < low)
                    loLim += primesList.get(i);

                /*  Mark multiples of prime.get(i) in [low..high]:
                    We are marking j - low for j, i.e. each number
                    in range [low, high] is mapped to [0, high-low]
                    so if range is [50, 100]  marking 50 corresponds
                    to marking 0, marking 51 corresponds to 1 and
                    so on. In this way we need to allocate space only
                    for range  */
                for (int j=loLim; j<high; j+=primesList.get(i))
                    isPrime[j-low] = false;
            }

            // Numbers which are not marked as false are prime
            for (int i = low; i<high; i++)
                if (isPrime[i - low] == true)
                    primesList.add(i);

            // Update low and high for next segment
            low  = low + limit;
            high = high + limit;
        }

        return primesList;
    }
}