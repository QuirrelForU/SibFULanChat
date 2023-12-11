/**
 * Интерфейс `IMessageEncryptor` определяет методы для шифрования и дешифрования текстовых сообщений.
 */
public interface IMessageEncryptor {
    /**
     * Шифрует переданное сообщение.
     *
     * @param message Исходное сообщение для шифрования.
     * @return Зашифрованное сообщение.
     */
    String encrypt(String message);

    /**
     * Дешифрует переданное зашифрованное сообщение.
     *
     * @param message Зашифрованное сообщение для дешифрования.
     * @return Расшифрованное сообщение.
     */
    String decrypt(String message);
}
