package lab11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PromisesPrimeNumberFinder {

	private static final int K_SLICES = 1000;
	private static final int K = 1000;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CompletableFuture<Integer> promise = CompletableFuture.completedFuture(0);

		for (int index = 0; index < K_SLICES; ++index) {
			int range = index;
			CompletableFuture<Integer> nextPromise = CompletableFuture.supplyAsync(() -> countPrimes(range * K, range * K + K - 1));
			promise = promise.thenCombine(nextPromise, (first, second) -> first + second);
		}

		System.out.println("Total primes found: " + promise.get());
	}

	private static Integer countPrimes(int startRange, int endRange) {
		int primesFound = 0;

		for (int primeCandidate = startRange; primeCandidate <= endRange; ++primeCandidate) {
			primesFound += isPrime(primeCandidate) ? 1 : 0;
		}

		return primesFound;
	}

	private static boolean isPrime(int primeCandidate) throws IllegalArgumentException {
		
		if (primeCandidate < 0) {
			throw new IllegalArgumentException("PrimeCandidate must be a positive number - received: " + primeCandidate);
		}
		
		boolean isPrime = primeCandidate == 2;

		if (primeCandidate > 2) {
			isPrime = true;
			for (int testValue = 2; testValue <= Math.sqrt(primeCandidate); ++testValue) {
				if (primeCandidate % testValue == 0) {
					isPrime = false;
					break;
				}
			}
		} 

		return isPrime;
	}
}
