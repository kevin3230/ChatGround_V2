package com.chatground.utility;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptEncryptorUtil {

	public static void main(String[] args) {
		
		
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        
        //設定金鑰，與application.properties 相同
        config.setPassword("!!yourEncryptKey!!");
        
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
		
		//要加密的資訊
		String username = encryptor.encrypt("!!yourUsername!!");
		String password = encryptor.encrypt("!!yourPassword!!");
		String redis_password = encryptor.encrypt("!!yourRedisPassword!!");
		
		System.out.println("username:" + username);
		System.out.println("password:" + password);
		System.out.println("redis_password:" + redis_password);
		
		System.out.println("decode username:" + encryptor.decrypt(username));
		System.out.println("decode password:" + encryptor.decrypt(password));
		System.out.println("decode redis_password:" + encryptor.decrypt(redis_password));
	}

}
