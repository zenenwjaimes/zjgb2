package com.slasherx.zjgb.peripherals.rom;

import com.slasherx.zjgb.peripherals.RomLoader;

public abstract class AbstractRom implements RomInterface {
	public static byte[] romData;
	
	@SuppressWarnings(value = "unused")
	private RomLoader romLoader;
	
	public AbstractRom(RomLoader romLoader) {
		this.romLoader = romLoader;
		
		romData = new byte[8192];
	}
}