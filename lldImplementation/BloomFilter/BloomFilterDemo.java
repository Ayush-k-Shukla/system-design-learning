package BloomFilter;

public class BloomFilterDemo {

    public static void run() {
        BloomFilter<String> bloom = new BloomFilter<String>(1000, 3);

        bloom.add("Ayush");
        bloom.add("Flipkart");
        bloom.add("BloomFilter");

        System.out.println(bloom.mightPresent("Ayush"));
        System.out.println(bloom.mightPresent("RandomUser"));
    }
}
