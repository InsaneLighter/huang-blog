package com.huang.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Time 2022-03-15 19:47
 * Created by Huang
 * className: RSAUtils
 * Description:
 */
public class RSAUtils {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成密钥对(公钥和私钥)
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 拿到生成的密钥对里的私钥
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 拿到生成的密钥对里的公钥
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 公钥加密
     */
    public static String encryptByPublicKey(String content, String publicKey, String charset) throws Exception {
        byte[] data = content.getBytes(charset);
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * 私钥解密
     */
    public static String decryptByPrivateKey(String content, String privateKey, String charset) throws Exception {
        byte[] encryptedData = Base64.decodeBase64(content);
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        out.close();
        return out.toString(charset);
    }

    public static String decryptByPrivateKey(String content, String privateKey) {
        byte[] decryptedData = new byte[0];
        try {
            byte[] encryptedData = Base64.decodeBase64(content);
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();
            out.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    /**
     * 签名
     */
    public static String sign(String content, String privateKey, String charset) throws Exception {
        byte[] data = content.getBytes(charset);
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }
    /**
     * 验签
     */
    public static boolean verify(String content, String publicKey, String sign, String charset) throws Exception {

        byte[] data = content.getBytes(charset);
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 加解密 加签验签
     * @param args
     */
    public static void main(String[] args) {
        try {
            /**
             * 生成 RSA publicKey privateKey
             */
            //Map<String, Object> keyPair = genKeyPair();
            //String publicKey = getPublicKey(keyPair);
            //String privateKey = getPrivateKey(keyPair);
            //System.out.println(String.format("rsa privateKey:%s\nrsa publicKey:%s", privateKey, publicKey));
            /**
             * rsa加解密:公钥加密，私钥解密
             */
            // 明文
            String data = "huanghong1314...";
            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTG1CCIKxRNX4LJXWgLaDbPvGpFzfwtnv/lKI6LLqbhySzZ/31NuIL5bwUgc9pfcKFHYrABN3MndHBDkNp/g3bcocxyipWh+OEtWu9XUCGdaFUPZftZnVig/68/lhTIdWXEG0C7YZcbjZo9d3HwPZ5HK7qhsi58Ii1mxq55iFNJwIDAQAB";
            String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANMbUIIgrFE1fgsldaAtoNs+8akXN/C2e/+UojosupuHJLNn/fU24gvlvBSBz2l9woUdisAE3cyd0cEOQ2n+DdtyhzHKKlaH44S1a71dQIZ1oVQ9l+1mdWKD/rz+WFMh1ZcQbQLthlxuNmj13cfA9nkcruqGyLnwiLWbGrnmIU0nAgMBAAECgYAPoRIQ0EJxsLgoMxnGRhqGoN5bWnC1zYzYmzR3giA+q364GZbftLR/lP6bthCZCsp/+HPxS2rwXy7MUDYFft2foxuv15SNuTXs47tqy5uS2qBC8PZunwG8kJz43m0pq1Hro7jxf6FoYTSc81WI1w2pkFthO+eAVZS9nFXCpVqfgQJBAPaHpgKB8tyCbKza9GIu17a0JhDX64qiE8ud84uAIpNwrURK7UAk7+8G3i7kpRf3Bfo+IRaZ21taeE82ULGKjxECQQDbN1Fbyrig2G+Hz4syBwAYCPS0l4IZbqkiqicvac6a3hvwnQ+G0REvx5fdbJGjzPoZGs6KhnD+A6aNWZiG1Ii3AkBcyiOuz/B0FNcInITdK5pflyC4uDxcerKOsg3H1NuuuPtOOBFiUkTIp8Iazk5hXcWvMZFMq25HzR55Wg03NdqxAkEAwj4vCi4EEQoXMXkWtvp6+9Ebu9WwQwbKOdwZoqqXlqSOj1MsSeKA3xx1lY85MbJUPeCB+T8NahckAoXMYFR82wJAXbwK8Oy9s+7rZArKWFHscbtqGk2KYDrE7FojgVUJUN211Tv8wtQyOJYAVt21bGFDrDdIKJYo0q8ywNFotQHXSw==";
            //publicKey加密
            String encryptData = encryptByPublicKey(data, publicKey, "UTF-8");
            //privateKey解密
            String decryptData = decryptByPrivateKey(encryptData, privateKey, "UTF-8");
            System.out.println(String.format("data:%s, encryptData:%s, decryptData:%s", data, encryptData, decryptData));
            /**
             * RSA签名与验签
             */
            //privateKey签名
            String sign = sign(data, privateKey, "UTF-8");
            //publicKey验签
            boolean verify = verify(data, publicKey, sign, "UTF-8");
            System.out.println(String.format("data:%s, sign:%s, verify:%s", data, sign, verify));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
