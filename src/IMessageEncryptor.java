public interface IMessageEncryptor {
    String encrypt(String message);
    String decrypt(String message);
}
