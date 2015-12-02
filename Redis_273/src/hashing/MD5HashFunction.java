import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5HashFunction implements HashFunction {
  static final Logger LOG = LoggerFactory.getLogger(MD5HashFunction.class);
  MessageDigest digest;

  public MD5HashFunction() {
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      LOG.error("MD5 algorithm doesn't exist?", e);
      throw new IllegalArgumentException("This should never happen");
    }
  }

  public int hash(Object s) {
    digest.reset();
    byte[] hash = digest.digest(s.toString().getBytes());
    int h0 = (hash[0] & 0xFF);
    int h1 = (hash[1] & 0xFF) << 8;
    int h2 = (hash[2] & 0xFF) << 16;
    int h3 = (hash[3] & 0xFF) << 24;
    int val = h0 + h1 + h2 + h3;
    return val;
  }
}