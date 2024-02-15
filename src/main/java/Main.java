import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            String route = generateRoute("RLRFR", 100);

            Runnable algo = () -> {
                int count = 0;
                for (int j = 0; j < route.length(); j++) {
                    count = route.charAt(j) == 'R' ? count + 1 : count;
                }
                synchronized (sizeToFreq) {
                    sizeToFreq.put(count, sizeToFreq.getOrDefault(count, 0) + 1);
                }
            };
            Thread thread = new Thread(algo);
            thread.start();
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        int maxCount = Integer.MIN_VALUE;
        int maxKey = 0;
        for (int key : sizeToFreq.keySet())  {
            if (sizeToFreq.get(key) > maxCount) {
                maxCount = sizeToFreq.get(key);
                maxKey = key;
            }
        }
        System.out.println("Самое частое количество повторений " + maxKey
                + " (встретилось " + sizeToFreq.get(maxKey) + " раз)");
        sizeToFreq.remove(maxKey);
        System.out.println("Другие размеры:");
        sizeToFreq.forEach((key, value) -> System.out.println("- " + key + " (" + value + " раз)"));
    }
}
