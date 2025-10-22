package analyzer;

import cipher.CaesarCipher;

import java.util.Set;

public class BruteForceAnalyzer {
    private final CaesarCipher cipher;
    private final Set<String> commonWords;

    public BruteForceAnalyzer(CaesarCipher cipher, Set<String> commonWords) {
        this.cipher = cipher;
        this.commonWords = commonWords;
    }

    public BruteForceAnalyzer(CaesarCipher cipher) {
        this(cipher, initializeCommonWords());
    }

    public BruteForceResult decrypt(String encryptedText) {
        int bestKey = -1;
        String bestDecryption = "";
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int key = 0; key < cipher.getAlphabet().size(); key++) {
            String decrypted = cipher.decrypt(encryptedText, key);
            double score = calculateReadabilityScore(decrypted);
            if (score > bestScore){
                bestScore = score;
                bestKey = key;
                bestDecryption = decrypted;
            }
        }
        return new BruteForceResult(bestKey, bestDecryption, bestScore);
    }

    private double calculateReadabilityScore(String text) {
        double score = 0.0;
        String lowerText = text.toLowerCase();

        for (String word : commonWords) {
            if (lowerText.contains(" " + word + " ") ||
                    lowerText.startsWith(word + " ") ||
                    lowerText.endsWith(" " + word)) {
                score += 2.0;
            }
        }

        score += countOccurrences(lowerText, " и ") * 1.5;
        score += countOccurrences(lowerText, " в ") * 1.5;
        score += countOccurrences(lowerText, " не ") * 1.2;

        // Штраф за непонятные комбинации символов
        score -= countOccurrences(lowerText, "  ") * 0.5; // двойные пробелы
        score -= countOccurrences(lowerText, "..") * 1.0; // многоточия без смысла

        return score;
    }

    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    public static Set<String> initializeCommonWords() {
        return Set.of(
                "и", "в", "не", "на", "я", "быть", "он", "с", "что", "а",
                "по", "это", "она", "этот", "но", "они", "мы", "то", "из",
                "вы", "как", "все", "так", "его", "за", "от", "к", "же", "ты",
                "у", "для", "о", "да", "бы", "вот", "сказать"
        );
    }

    public static class BruteForceResult {
        private final int key;
        private final String decryptedText;
        private final double confidence;

        public BruteForceResult(int key, String decryptedText, double confidence) {
            this.key = key;
            this.decryptedText = decryptedText;
            this.confidence = confidence;
        }

        public int getKey() { return key; }
        public String getDecryptedText() { return decryptedText; }
        public double getConfidence() { return confidence; }
    }

}
