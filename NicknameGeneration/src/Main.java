import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger threeLetterCounter = new AtomicInteger(0);
    public static AtomicInteger fourLetterCounter = new AtomicInteger(0);
    public static AtomicInteger fiveLetterCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindromeThread = new Thread(() -> {
            for (String text : texts) {

                String reverseText = "";
                for (int i = text.length() - 1; i >= 0; i--) {
                    reverseText = reverseText + text.charAt(i);
                }

                if (text.equals(reverseText)) {
                    switch (text.length()) {
                        case 3 -> threeLetterCounter.getAndIncrement();
                        case 4 -> fourLetterCounter.getAndIncrement();
                        case 5 -> fiveLetterCounter.getAndIncrement();
                    }
                }
            }
        });

        Thread sameLetterThread = new Thread(() -> {
            for (String text : texts) {

                boolean flag = true;
                char firstLetter = text.charAt(0);

                for (int i = 1; i < text.length(); i++) {
                    if (text.charAt(i) != firstLetter) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    switch (text.length()) {
                        case 3 -> threeLetterCounter.getAndAdd(-1);
                        case 4 -> fourLetterCounter.getAndAdd(-1);
                        case 5 -> fiveLetterCounter.getAndAdd(-1);
                    }
                }
            }
        });

        Thread incrementalOrderThread = new Thread(() -> {
            for (String text : texts) {

                boolean flag = true;

                for (int i = 0; i < text.length() - 1; i++) {
                    if (text.charAt(i) > text.charAt(i + 1)) {
                        flag = false;
                        break;
                    }
                }

                if (flag) {
                    switch (text.length()) {
                        case 3 -> threeLetterCounter.getAndIncrement();
                        case 4 -> fourLetterCounter.getAndIncrement();
                        case 5 -> fiveLetterCounter.getAndIncrement();
                    }
                }
            }
        });

        palindromeThread.start();
        sameLetterThread.start();
        incrementalOrderThread.start();

        palindromeThread.join();
        sameLetterThread.join();
        incrementalOrderThread.join();

        System.out.printf("""
                        Красивых слов с длинной 3: %d\s
                        Красивых слов с длинной 4: %d\s
                        Красивых слов с длинной 5: %d\s
                        """,
                threeLetterCounter.get(), fourLetterCounter.get(), fiveLetterCounter.get());
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}