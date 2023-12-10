public class CaesarCipherEncryptor implements IMessageEncryptor {
    private int shift;
    public CaesarCipherEncryptor() {
        this.shift = 0; // значение по умолчанию
    }


    @Override
    public String encrypt(String message) {
        return shiftString(message, shift);
    }

    @Override
    public String decrypt(String message) {
        return shiftString(message, -shift);
    }
    public void setShift(int newShift) {
        this.shift = newShift;
    }

    private String shiftString(String message, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + shift + 26) % 26 + base);
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
