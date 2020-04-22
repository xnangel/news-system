package com.sunxn.news.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @description:
 * @data: 2020/4/21 22:22
 * @author: xiaoNan
 */
public class RsaUtils {

    /**
     * 根据密文，生成rsa公钥和私钥，并写入指定文件
     * @param publicKeyFilename     公钥文件路径
     * @param privateKeyFilename    私钥文件路径
     * @param secret                生成密钥的密文
     * @throws Exception
     */
    public static void generateKey(String publicKeyFilename, String privateKeyFilename, String secret) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 获取公钥并写入指定文件
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        writeFile(publicKeyFilename, publicKeyBytes);
        // 获取私钥并写入指定文件
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        writeFile(privateKeyFilename, privateKeyBytes);
    }

    /**
     * 把字节码文件写入指定路径名称文件中
     * @param destPath  指定路径文件名称
     * @param keyBytes  字节码
     * @throws IOException
     */
    private static void writeFile(String destPath, byte[] keyBytes) throws IOException {
        File file = new File(destPath);
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.write(file.toPath(), keyBytes);
    }

    /**
     * 从文件中读取公钥
     * @param filename  公钥保存路径，相对于classpath
     * @return  公钥对象
     * @throws Exception
     */
    public static PublicKey getPublicKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPublicKey(bytes);
    }

    /**
     * 获取公钥
     * @param bytes 公钥的字节形式
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    /**
     * 从文件中读取私钥
     * @param filename  私钥保存路径，相对于classpath
     * @return  私钥对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] bytes = readFile(filename);
        return getPrivateKey(bytes);
    }

    /**
     * 获取私钥 密钥
     * @param bytes 私钥的字节形式
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    /**
     * 从指定的路径文件中获取字节码数据
     * @param filename
     * @return
     * @throws IOException
     */
    private static byte[] readFile(String filename) throws IOException {
        return Files.readAllBytes(new File(filename).toPath());
    }
}
