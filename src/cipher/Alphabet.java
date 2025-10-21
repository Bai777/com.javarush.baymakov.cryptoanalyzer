package cipher;

public class Alphabet {
    private final char[] characters;
    private final int size;

    public Alphabet(char[] characters) {
        this.characters = characters.clone();
        this.size = characters.length;
    }

    public int indexOf(char c) {
        for (int i = 0; i < size; i++) {
            if (characters[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public char charAt(int index) {
        return characters[Math.floorMod(index, size)];
    }

    public int size() {
        return size;
    }

    public boolean contains(char character) {
        return indexOf(character) != -1;
    }

    public static Alphabet russianAlphabet() {
        return new Alphabet(new char[]{
                'а', 'б', 'в', 'г', 'д', 'е', 'ж', 'з', 'и', 'й', 'к', 'л', 'м',
                'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
                'ъ', 'ы', 'ь', 'э', 'ю', 'я',
                '.', ',', '«', '»', '"', '\'', ':', '-', '!', '?', ' '
        });
    }
}
