package org.noear.solonx.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Rsa 内部小工具
 *
 * @author noear
 * @since 2.7
 */
public class RsaUtil {
    private static final String ALGORITHM = "RSA";
    private static final String PUBLICK_KEY = "PUBLICK_KEY";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";

    /**
     * 密钥长度（常见为1024）
     */
    private final int keySize;
    public RsaUtil(int keySize) {
        this.keySize = keySize;
    }

    /**
     * 生成秘钥对，公钥和私钥
     */
    public Map<String, Object> genKeyPair() throws NoSuchAlgorithmException {
        Map<String, Object> keyMap = new HashMap<>();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(keySize);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        keyMap.put(PUBLICK_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 加密
     */
    public String encryptToBase64(String dataStr, String publicKeyStr) throws Exception {
        byte[] data = dataStr.getBytes(StandardCharsets.UTF_8);
        byte[] tmp = encrypt(data, publicKeyStr);
        return new String(Base64.getEncoder().encode(tmp));
    }

    /**
     * 加密
     */
    public byte[] encrypt(byte[] data, String publicKeyStr) throws Exception {
        // 得到公钥
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr.getBytes());
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // 分段加密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int maxEncryptSize = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;

        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i = 0, offset = 0;
        byte[] cache;
        while (inputLength - offset > 0) {
            if (inputLength - offset > maxEncryptSize) {
                cache = cipher.doFinal(data, offset, maxEncryptSize);
            } else {
                cache = cipher.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache);
            i++;
            offset = i * maxEncryptSize;
        }

        return out.toByteArray();
    }

    /**
     * 解密
     */
    public String decryptFromBase64(String base64Str, String privateKeyStr) throws Exception {
        byte[] data = Base64.getDecoder().decode(base64Str);
        return new String(decrypt(data, privateKeyStr), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     */
    public byte[] decrypt(byte[] data, String privateKeyStr) throws Exception {
        // 得到私钥
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr.getBytes());
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pKCS8EncodedKeySpec);


        // 分段解密
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int maxDecryptSize = (cipher.getOutputSize((int) (((RSAPrivateKey) privateKey).getModulus().bitLength() + 7) >> 3));

        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int i = 0, offset = 0;
        byte[] cache;
        while (inputLength - offset > 0) {
            if (inputLength - offset > maxDecryptSize) {
                cache = cipher.doFinal(data, offset, maxDecryptSize);
            } else {
                cache = cipher.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache);
            i++;
            offset = i * maxDecryptSize;
        }

        return out.toByteArray();
    }

    /**
     * 获取公钥
     */
    public String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLICK_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 获取私钥
     */
    public String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}