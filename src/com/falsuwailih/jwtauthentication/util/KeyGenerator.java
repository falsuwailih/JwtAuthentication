package com.falsuwailih.jwtauthentication.util;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */
public class KeyGenerator {
	public static Key generateKey() {
		String keyString = "SimpleKey";
		Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
		return key;
	}
}
