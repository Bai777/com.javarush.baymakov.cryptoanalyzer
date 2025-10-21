package cipher;

public class CaesarCipher {
    private final Alphabet alphabet;

    public CaesarCipher(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    public String encrypt(String text, int key) {
        return processText(text, key);
    }

    public String decrypt(String text, int key) {
        return processText(text, -key);
    }

    private String processText(String text, int shift) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < text.length()){
            char originalChar = text.charAt(i);
            char shiftedChar = shiftCharacter(originalChar, shift);
            result.append(shiftedChar);
            i++;
        }
        return result.toString();
    }

    private char shiftCharacter(char character, int shift){
        int index = alphabet.indexOf(Character.toLowerCase(character));
        if(index == -1){
            return character;
        }

        char shiftedChar = alphabet.charAt(index + shift);
        if (Character.isUpperCase(character)){
            return Character.toUpperCase(shiftedChar);
        }
        return shiftedChar;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }
}