package com.slasherx.zjgb.utils;

import java.math.BigInteger;
import java.util.Arrays;

public class BitUtils {
	public static byte[] getByteRange(byte[] bytes, int from, int to) {
		return Arrays.copyOfRange(bytes, from, to);
	}
	
	public static byte getByte(byte[] bytes, int from) {
		return bytes[from];
	}
	
	public static String shortToString(short val) {
		return Integer.toHexString((int)(val & 0xFFFF));
	}
	
	public static String byteToString(byte val) {
		return Integer.toHexString((int)(val & 0xFF));
	}
	
	public static String bytesToString(byte[] val) {
		return new BigInteger(1, val).toString(16);
	}
	
	public static short bytesToShort(byte high, byte low) {
		return (short)(((high & 0xFF)<< 8) + (low & 0xFF));
	}
	
	public static int bytesToInt(byte high, byte low) {
		return (int)(((high & 0xFF)<< 8) + (low & 0xFF));
	}
	
	public static int unsetBit(short num, int bit) {
		num &= ~(1 << bit);
		return num;
	}
	
	public static int toggleBit(short num, int bit) {
		num ^= (1 << bit);
		return num;
	}
	
	public static int setBit(short num, int bit) {
		num |= (1 << bit);
		return num;
	}
}
