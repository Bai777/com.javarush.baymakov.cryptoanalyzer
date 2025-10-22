package analyzer;

import cipher.CaesarCipher;

import java.util.HashMap;
import java.util.Map;

public class StatisticalAnalyzer {
    private final CaesarCipher cipher;
    private final Map<Character, Double> languageFrequency;

    public StatisticalAnalyzer(CaesarCipher cipher, Map<Character, Double> languageFrequency) {
        this.cipher = cipher;
        this.languageFrequency = new HashMap<>(languageFrequency);
    }

    public StatisticalResult decrypt(String encryptedText) {
        Map<Character, Double> encryptedFrequency = calculateFrequency(encryptedText);

        int bestKey = -1;
        double bestMatch = Double.MAX_VALUE;

        for (int key = 0; key < cipher.getAlphabet().size(); key++) {
            double distance = calculateFrequencyDistance(encryptedFrequency, key);
            if (distance < bestMatch) {
                bestMatch = distance;
                bestKey = key;
            }
        }

        String decrypted = cipher.decrypt(encryptedText, bestKey);
        return new StatisticalResult(bestKey, decrypted, bestMatch);
    }

    private Map<Character, Double> calculateFrequency(String text) {
        Map<Character, Integer> counts = new HashMap<>();
        int total = 0;

        for (char character : text.toCharArray()) {
            if (cipher.getAlphabet().contains(Character.toLowerCase(character))) {
                char lowerChar = Character.toLowerCase(character);
                counts.put(lowerChar, counts.getOrDefault(lowerChar, 0) + 1);
                total++;
            }
        }

        Map<Character, Double> frequency = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : counts.entrySet()) {
            frequency.put(entry.getKey(), (double) entry.getValue() / total);
        }
        return frequency;
    }

    private double calculateFrequencyDistance(Map<Character, Double> encryptedFreq, int key) {
        double distance = 0.0;

        for (Map.Entry<Character, Double> entry : languageFrequency.entrySet()) {
            char originalChar = entry.getKey();
            double expectedFreq = entry.getValue();

            char encryptedChar = cipher.encrypt(String.valueOf(originalChar), key).charAt(0);
            double actualFreq = encryptedFreq.getOrDefault(encryptedChar, 0.0);

            distance += Math.pow(expectedFreq - actualFreq, 2);
        }
        return Math.sqrt(distance);
    }


    public static Map<Character, Double> russianFrequency() {
        Map<Character, Double> freq = new HashMap<>();

        freq.put(' ', 0.175);
        freq.put('о', 0.090);
        freq.put('е', 0.072);
        freq.put('а', 0.062);
        freq.put('и', 0.062);
        freq.put('н', 0.053);
        freq.put('т', 0.053);
        freq.put('с', 0.045);
        freq.put('р', 0.040);
        freq.put('в', 0.038);

        return freq;
    }

    public static class StatisticalResult {
        private final int key;
        private final String decryptedText;
        private final double confidence;

        public StatisticalResult(int key, String decryptedText, double confidence) {
            this.key = key;
            this.decryptedText = decryptedText;
            this.confidence = confidence;
        }


        public int getKey() { return key; }
        public String getDecryptedText() { return decryptedText; }
        public double getConfidence() { return confidence; }
    }

}
