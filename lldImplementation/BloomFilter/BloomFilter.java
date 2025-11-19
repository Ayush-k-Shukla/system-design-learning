package BloomFilter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter<T> {
    private final int bitSize;
    private final int hashCount;
    private final BitSet bitSet;

    private final MessageDigest messageDigest;

    public BloomFilter(int bitSize, int hashCount){
        this.bitSize = bitSize;
        this.hashCount = hashCount;
        this.bitSet = new BitSet(bitSize);

        try {
            this.messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    /*
    * instead of writing k hash functions
    * we will use same sha algo with k different seeds
    * to generate k hashes
    * */
    private int hash(T item, int seed){
        byte[] hashBytes = messageDigest.digest((item.toString() + seed).getBytes());

        int hash = 0;
        for (int i = 0; i < 4; i++) {
            int unsignedByte = hashBytes[i] & 0xFF;

            // Shift by 8 bit as to make place for 4 , 8 bits no
            hash = (hash << 8) | unsignedByte;
        }

        // Shrink index within Bloom Filter size
        return Math.abs(hash % bitSize);
    }

    public void add(T item) {
        for (int i = 0; i < hashCount; i++) {
            int index = hash(item, i);
            bitSet.set(index);
        }
    }

    public boolean mightPresent(T item) {
        for (int i = 0; i < hashCount; i++) {
            int index = hash(item, i);
            if (!bitSet.get(index)) {
                return false;  // definitely not present
            }
        }
        return true; // maybe present
    }
}