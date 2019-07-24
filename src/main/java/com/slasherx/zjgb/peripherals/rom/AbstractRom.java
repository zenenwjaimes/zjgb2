package com.slasherx.zjgb.peripherals.rom;

import com.slasherx.zjgb.peripherals.RomLoader;

public abstract class AbstractRom implements RomInterface {
	public byte[] romData;
	
	@SuppressWarnings(value = "unused")
	private RomLoader romLoader;
	
	public AbstractRom() {
		romData = new byte[8192];
	}
	
	public AbstractRom(RomLoader romLoader) {
		super();
		this.romLoader = romLoader;
	}
}