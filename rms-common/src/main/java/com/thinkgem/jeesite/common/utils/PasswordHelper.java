package com.thinkgem.jeesite.common.utils;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.Assert;

/**
 * @author wangganggang
 * @date 2017年07月27日 下午9:31
 */
public class PasswordHelper {

    private final static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private final static String algorithmName  = "SHA-256";
    private final static int    hashIterations = 2;
    private final static String randomSalt = "2018080245";

    public static String  encryptPassword(String passWord) {
        Assert.notNull(passWord, "password must not be null");
        String newPassword = new SimpleHash(algorithmName, passWord, ByteSource.Util.bytes(randomSalt), hashIterations).toHex();
        return newPassword;
    }

    public static boolean checkPassword(String password,String loginPassword) {
        Assert.notNull(loginPassword, "password must not be null");
        String newPassword = new SimpleHash(algorithmName, loginPassword, ByteSource.Util.bytes(randomSalt), hashIterations).toHex();
        return password.equals(newPassword);
    }
}
