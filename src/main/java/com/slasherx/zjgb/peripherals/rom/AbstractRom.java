package com.slasherx.zjgb.peripherals.rom;

import com.slasherx.zjgb.peripherals.RomLoader;

public abstract class AbstractRom implements RomInterface {
	public byte[] romData;

	@SuppressWarnings(value = "unused")
	public RomLoader romLoader;
	
	public AbstractRom() {
		setRomData(new byte[0x10000]);
	}
	
	public AbstractRom(RomLoader romLoader) {
		super();
		setRomLoader(romLoader);
	}

	/**
	 * @return the romData
	 */
	public byte[] getRomData() {
		return romData;
	}

	/**
	 * @param romData the romData to set
	 */
	public void setRomData(byte[] romData) {
		this.romData = romData;
	}

	/**
	 * @return the romLoader
	 */
	public RomLoader getRomLoader() {
		return romLoader;
	}

	/**
	 * @param romLoader the romLoader to set
	 */
	public void setRomLoader(RomLoader romLoader) {
		this.romLoader = romLoader;
	}
}