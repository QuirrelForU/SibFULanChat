/**
 * Класс CaesarCipherEncryptor реализует шифр Цезаря для шифрования и дешифрования текстовых сообщений.
 */
public class CaesarCipherEncryptor implements IMessageEncryptor {
    private int shift;

    /**
     * Конструктор по умолчанию.
     * Инициализирует CaesarCipherEncryptor с нулевым сдвигом.
     */
    public CaesarCipherEncryptor() {
        this.shift = 0; // значение по умолчанию
    }

    /**
     * Шифрует переданное сообщение с использованием шифра Цезаря.
     *
     * @param message Строка, которую нужно зашифровать.
     * @return Зашифрованная строка.
     */
    @Override
    public String encrypt(String message) {
        return shiftString(message, shift);
    }

    /**
     * Дешифрует переданное сообщение, зашифрованное с использованием шифра Цезаря.
     *
     * @param message Зашифрованная строка для дешифрования.
     * @return Расшифрованная строка.
     */
    @Override
    public String decrypt(String message) {
        return shiftString(message, -shift);
    }

    /**
     * Устанавливает новое значение сдвига для шифра Цезаря.
     *
     * @param newShift Новое значение сдвига.
     */
    public void setShift(int newShift) {
        this.shift = newShift;
    }

    /**
     * Вспомогательный метод для сдвига символов в строке.
     * Производит сдвиг каждого буквенного символа в строке на заданное количество позиций.
     *
     * @param message Сообщение для сдвига.
     * @param shift Количество позиций для сдвига.
     * @return Преобразованная строка с примененным сдвигом.
     */
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
