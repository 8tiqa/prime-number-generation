package primenumbergeneration.strategies;
import java.util.ArrayList;
import java.util.List;

// BruteForce: Simple Brute Force: check each number in the range for primality.
public class BruteForce extends Strategy {

    public List<Integer> generate(int startingValue, int endingValue){
        primesList = new ArrayList<Integer>();
        int start = Math.min(startingValue, endingValue);
        int end = Math.max(startingValue, endingValue);

        for(int i = start; i <= end; i++){
            if(isPrime(i)){
                primesList.add(i);
            }
        }
        return primesList;
    }


}