package primenumbergeneration.strategies;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.LinkedList;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

// Split the range into segments
// master marks primes up to square root of endingValue
// and propagate to other sieve threads
// other sieves mark multiples in a specific segment of the range
// gather results from all sieves.

public class SieveEratosthenesParallel extends Strategy {

    private void simpleSieve(int start, int end , List<Integer> primes, List<Integer> firstprimes)
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

        for (int i = 2; i <= end; i++) {
            if (isPrime[i] == true){
                firstprimes.add(i);
                if(i>=start){
                    primes.add(i);
                }
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

        int limit = (int) (floor(sqrt(end))+1);

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available Processors : " + availableProcessors );


        // Compute all primes smaller than or equal to square root of n using simple sieve
        List<Integer> firstPrimes = new ArrayList<Integer>();
        simpleSieve(start, limit, primesList, firstPrimes);

        List<Future<Void>> futures = new LinkedList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors);

        int remainingRange = (end - limit);
        int segmentSize = remainingRange / availableProcessors;
        int segmentCount = remainingRange / segmentSize;

        System.out.println("Segment size : " + segmentSize + "   no of segments : " + segmentCount);

        // creating segments
        int lower = limit + 1;
        int upper = lower + segmentSize;
        List<Sieve> sievesList = new ArrayList<>(segmentCount);
        for (int i = 1; i <= segmentCount; i++) {
            Sieve sieve = new Sieve(lower, upper > end ? end : upper, firstPrimes);
            System.out.println("Segment " + i + " lower bound " + sieve.getLower() + "  upper bound " + sieve.getUpper());
            sievesList.add(sieve);
            lower = upper + 1;
            upper = lower + segmentSize;
        }


        sievesList.stream().forEach(segmentedSieve -> { futures.add(executorService.submit(segmentedSieve));});

        // wait for completion by getting futures
        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception ex) {
                System.out.println("Exception in getting future " + ex);
            }
        });

        executorService.shutdownNow();

        // gather results into primesList
        IntStream.range(0, sievesList.size()).forEach(n -> {
            Sieve segmentedSieve = sievesList.get(n);
            for (int i = segmentedSieve.getLower(); i < segmentedSieve.getUpper(); i++) {
                if (!segmentedSieve.getBitSet().get(i - segmentedSieve.getLower())) {
                    primesList.add(i);
                }
            }
        });

        return primesList;

    }

    class Sieve implements Callable<Void> {

        private int lower;
        private int upper;
        private List<Integer> smallPrimes;
        private BitSet bitSet;

        Sieve(int lower, int upper, List<Integer> firstPrimes) {
            this.lower = lower;
            this.upper = upper;
            this.smallPrimes = firstPrimes;
        }

        @Override
        public Void call() throws Exception {
            int size = (upper - lower) + 1;
            this.bitSet = new BitSet(size);
            smallPrimes.forEach(prime -> {
                int remainder = lower % prime;
                int startIndex = remainder == 0 ? 0 : prime - remainder;
                for (int i = startIndex; i <= size; i += prime) {
                    if (!bitSet.get(i)) {
                        bitSet.set(i);
                    }
                }
            });
            return null;
        }

        public BitSet getBitSet() {
            return this.bitSet;
        }

        public int getUpper() {
            return upper;
        }

        public int getLower() {
            return lower;
        }
    }
}
