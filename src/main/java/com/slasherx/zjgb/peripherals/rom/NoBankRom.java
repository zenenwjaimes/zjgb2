/**
 * 
 */
package com.slasherx.zjgb.peripherals.rom;

import com.slasherx.zjgb.peripherals.RomLoader;
import com.slasherx.zjgb.utils.BitUtils;

/**
 * @author slasherx
 *
 */
public class NoBankRom extends AbstractRom {
	public NoBankRom() {
		super();
	}
	
	public NoBankRom(byte[] data) {
		super();
		romData = data;
	}
	
	public NoBankRom(RomLoader romLoader) {
		super(romLoader);
		
		setRomData(BitUtils.getByteRange(romLoader.rom, 0x0000, 0x10000));
	}
}