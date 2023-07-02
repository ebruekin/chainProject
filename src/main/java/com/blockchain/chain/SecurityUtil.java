package com.blockchain.chain;

import java.nio.charset.StandardCharsets;
import java.security.*;
//import java.util.Base64;

public class SecurityUtil {


    public static byte[] applyRSASig(PrivateKey privateKey, String data) {
        try {
            Signature rsaSign = Signature.getInstance("SHA256withRSA");
            rsaSign.initSign(privateKey);
            rsaSign.update(data.getBytes(StandardCharsets.UTF_8));
            return rsaSign.sign();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verifyRSASig(PublicKey publicKey, String data,  byte[] signature) {
        try {
            Signature rsaVerify = Signature.getInstance("SHA256withRSA");
            rsaVerify.initVerify(publicKey);
            rsaVerify.update(data.getBytes(StandardCharsets.UTF_8));
            return rsaVerify.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String applySha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /*public static byte[] applyECDSASig(PrivateKey privateKey, String data) {
        try {
            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
            ecdsaSign.initSign(privateKey);
            ecdsaSign.update(data.getBytes(StandardCharsets.UTF_8));
            return ecdsaSign.sign();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

       /*public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes(StandardCharsets.UTF_8));
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/


}
