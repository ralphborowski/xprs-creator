package rocks.xprs.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Creates hashes (e.g. for storing passwords in a database)
 *
 * @see https://crackstation.net/hashing-security.htm
 * @see https://github.com/defuse/password-hashing
 * @author Taylor Hornby (defuse)
 */
public class Pbkdf2Hash {

  public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

  // These constants may be changed without breaking existing hashes.
  private static final int SALT_BYTE_SIZE = 24;
  private static final int HASH_BYTE_SIZE = 18;
  private static final int PBKDF2_ITERATIONS = 64000;

  // These constants define the encoding and may not be changed.
  private static final int HASH_SECTIONS = 5;
  private static final int HASH_ALGORITHM_INDEX = 0;
  private static final int ITERATION_INDEX = 1;
  private static final int HASH_SIZE_INDEX = 2;
  private static final int SALT_INDEX = 3;
  private static final int PBKDF2_INDEX = 4;

  public static String createHash(String password) {
    return createHash(password.toCharArray());
  }

  public static String createHash(char[] password) {
    // Generate a random salt
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[SALT_BYTE_SIZE];
    random.nextBytes(salt);

    // Hash the password
    byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
    int hashSize = hash.length;

    // format: algorithm:iterations:hashSize:salt:hash
    String parts = "sha1:"
            + PBKDF2_ITERATIONS
            + ":" + hashSize
            + ":"
            + toBase64(salt)
            + ":"
            + toBase64(hash);
    return parts;
  }

  public static boolean verifyPassword(String password, String correctHash) {
    return verifyPassword(password.toCharArray(), correctHash);
  }

  public static boolean verifyPassword(char[] password, String correctHash) {
    // Decode the hash into its parameters
    String[] params = correctHash.split(":");
    if (params.length != HASH_SECTIONS) {
      Logger.getLogger(Pbkdf2Hash.class.getName()).log(Level.WARNING,
              "Fields are missing from the password hash.");
      return false;
    }

    // Currently, Java only supports SHA1.
    if (!params[HASH_ALGORITHM_INDEX].equals("sha1")) {
      Logger.getLogger(Pbkdf2Hash.class.getName()).log(Level.WARNING, "Unsupported hash type.");
      return false;
    }

    int iterations = 0;
    try {
      iterations = Integer.parseInt(params[ITERATION_INDEX]);
    } catch (NumberFormatException ex) {
      Logger.getLogger(Pbkdf2Hash.class.getName()).log(Level.WARNING,
              "Could not parse the iteration count as an integer.", ex);
      return false;
    }

    if (iterations < 1) {
      Logger.getLogger(Pbkdf2Hash.class.getName()).log(Level.WARNING,
              "Invalid number of iterations. Must be >= 1.");
      return false;
    }

    byte[] salt = null;
    try {
      salt = fromBase64(params[SALT_INDEX]);
    } catch (IllegalArgumentException ex) {
      Logger.getLogger(Pbkdf2Hash.class.getName()).log(Level.WARNING,
              "Base64 decoding of salt failed.",
              ex);
      return false;
    }

    byte[] hash = null;
    try {
      hash = fromBase64(params[PBKDF2_INDEX]);
    } catch (IllegalArgumentException ex) {
      Logger.getLogger(Pbkdf2Hash.class.getName()).log(Level.WARNING,
              "Base64 decoding of pbkdf2 output failed.",
              ex);
      return false;
    }

    int storedHashSize = 0;
    try {
      storedHashSize = Integer.parseInt(params[HASH_SIZE_INDEX]);
    } catch (NumberFormatException ex) {
      Logger.getLogger(Pbkdf2Hash.class.getName()).log(Level.WARNING,
              "Could not parse the hash size as an integer.",
              ex);
      return false;
    }

    if (storedHashSize != hash.length) {
      Logger.getLogger(Pbkdf2Hash.class.getName()).log(Level.WARNING,
              "Hash length doesn't match stored hash length.");
      return false;
    }

    // Compute the hash of the provided password, using the same salt,
    // iteration count, and hash length
    byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
    // Compare the hashes in constant time. The password is correct if
    // both hashes match.
    return slowEquals(hash, testHash);
  }

  private static boolean slowEquals(byte[] a, byte[] b) {
    int diff = a.length ^ b.length;
    for (int i = 0; i < a.length && i < b.length; i++) {
      diff |= a[i] ^ b[i];
    }
    return diff == 0;
  }

  private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
          throws RuntimeException {

    try {
      PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
      SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
      return skf.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException("Hash algorithm not supported.", ex);
    } catch (InvalidKeySpecException ex) {
      throw new RuntimeException("Invalid key spec.", ex);
    }
  }

  private static byte[] fromBase64(String hex)
          throws IllegalArgumentException {
    return Base64.getDecoder().decode(hex);
  }

  private static String toBase64(byte[] array) {
    return Base64.getEncoder().encodeToString(array);
  }
}
