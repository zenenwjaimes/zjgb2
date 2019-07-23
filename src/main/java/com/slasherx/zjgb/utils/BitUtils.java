package com.slasherx.zjgb.utils;

import java.util.Arrays;

public class BitUtils {
	public static byte[] getByteRange(byte[] bytes, int from, int to) {
		return Arrays.copyOfRange(bytes, from, to);
	}
	
	public static byte getByte(byte[] bytes, int from) {
		return bytes[from];
	}
}
