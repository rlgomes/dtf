/**
 * simple random number generator created for external tools that want to
 * generated data just like DTF does. This is useful if you have other tools
 * that generate data on a system and then want to easily do your validation
 * within DTF without having to worry about generating the same data within DTF.
 *
 * This implementation is of course not thread safe but if you want to make it
 * thread safe just place the seed into a structure that can be passed along
 * with each call to the nextBytes(), nextLong() functions and then you'll have
 * the multi-threaded safety without the issues of using any synchronization
 * primitives which can take up precious CPU time.
 *
 * To build this file just use gcc random.c and you'll have your a.out ready to
 * be executed. Currently this file contains a main() method because its used
 * to do some small validation that the output from the various methods has the
 * same output as the DTFRandom class.
 */
#include <stdio.h>
#include <time.h>

const int INTEGER_SIZE = 32;
const int BYTE_SIZE = 4;

#define LONG64 long long

static long rseed = 1234567890L;

long min(long a, long b) {
	return (a < b ? a : b);
}

int next(int nbits) {
	LONG64 x = rseed++;
	x ^= (x << 21);
	x ^= (x >> 35);
	x ^= (x << 4);
	return x;
}

int nextInt() {
	return next(INTEGER_SIZE);
}

unsigned int nextBoolean() {
	return next(1) != 0;
}

/*
 * The only special thing being done in this function is that we're skipping
 * to generate the sequence ${ because this could result in a property
 * being generated that DTF would later try to resolve.
 */
void nextBytes(char* bytes, int length) {
	int i,rnd,len,n,prev;

    for (i = 0, prev = 0, len = length; i < len; ) {
        for (rnd = nextInt(),
             n = min(len - i, INTEGER_SIZE/BYTE_SIZE);
             n-- > 0; rnd >>= BYTE_SIZE) {
            char b = (char)rnd;
            if ( b == '{' && prev == '$' ) continue;
            bytes[i++] = b;
        }
    }
}


void random(long seed) {
	rseed = seed;
}

int main(int argc, char **argv) {
	long seed = 1234567890L;
	random(seed);

	int expected[] = { 782593066,
			           746941499,
			           577072204,
			           541420637,
			           639986798,
			           604335231,
			           1004891264,
			           969239697,
			           1067805858,
			           1032154291 };

	int i;
	printf("Validating nextInt() against known results from DTFRandom.\n");
	for (i = 0; i < 10; i++) {
		int result = nextInt();
		if ( result != expected[i] ) {
			printf("nextInt() with seed %ld, did not return %d, got %d instead",
				   seed, expected[i], result);
			return -1;
		}
	}

	return 0;
}
