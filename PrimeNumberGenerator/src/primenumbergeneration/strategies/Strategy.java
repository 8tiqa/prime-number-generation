package primenumbergeneration.strategies;

import java.util.List;

public abstract class Strategy {
    List<Integer> primesList;

    abstract List<Integer> generate(int startingValue, int endingValue);

    // classic O(sqrt(n)) algorithm
    boolean isPrimeClassic(int value){
        if(value <= 2)
            return false;
        int i = 2;
        while(i*i<=value)
        {
            if (value%i == 0)
                return false;
            i+=1;
        }
        return true;

    }
    // 6k ± 1 optimization of the O(sqrt(n)) algorithm
    // It's 3 times as fast as testing all m.
    // all primes are of the form 6k ± 1, with the exception of 2 and 3
    boolean isPrime(int value){
        if(value <= 1)
            return false;
        else if(value <= 3)
            return true;
        // check if n is divisible by 2 or 3
        else if ((value%2 == 0)||(value%3 == 0))
            return false;
        // check through all the numbers of form 6k ± 1
        int i = 5;
        while(i*i<=value)
        {
            if ((value%i == 0)||(value%(i+2) == 0))
                return false;
            i+=6;
        }
        return true;
    }
}
