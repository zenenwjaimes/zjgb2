/**
 * 
 */
package com.slasherx.zjgb.peripherals;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.logging.log4j.*;

import com.slasherx.zjgb.peripherals.rom.AbstractRom;
import com.slasherx.zjgb.peripherals.rom.NoBankRom;
import com.slasherx.zjgb.peripherals.rom.RomTypeClassNotFound;
import com.slasherx.zjgb.utils.BitUtils;

import org.apache.commons.io.*;
/**
 * @author Zenen Jaimes
 *
 */
public class RomLoader {
	public static byte[] rom = new byte[0x800000];
	public AbstractRom romInstance;

	private static Logger logger;

	public String romTitle;
	public String manufacterCode;
	public byte romType;
	public byte romSize;
	public byte ramSize;
	public byte destinationCode;
	public byte licenseeCode;
	public byte romVersionNumber;
	public byte headerChecksum;
	public byte[] checksum;

	// Just be certain that our array is zeroed out
	static {
		for (int i = 0; i < rom.length; i++) {
			rom[i] = 0;
		}
	}
	
	public RomLoader(Logger logger4j, String filePath) throws IOException {
		logger = logger4j;

		// Read the whole rom and just plop it into the 8MB byte array
		ByteBuffer buf = ByteBuffer.wrap(rom);
		buf.position(0);
		byte[] file = FileUtils.readFileToByteArray(new File(filePath));
		buf.put(file);
		
		// Read all the metadata
		parseCartHeader();
		
		// Load up the needed Rom Type
		loadRomInstance();
	}
	
	public void loadRomInstance() throws RomTypeClassNotFound {
		switch(getRomType()) {
			case 0x00:
				romInstance = new NoBankRom(this);
				break;
			default:
				throw new RomTypeClassNotFound("Could not load this rom type");
		}
	}
	
	// Cart Header from 0x0100 to 0x014F
	public void parseCartHeader() {
		romTitle = new String(BitUtils.getByteRange(rom, 0x0134, 0x0143 + 1)).strip();
		manufacterCode = new String(BitUtils.getByteRange(rom, 0x013F, 0x0142 + 1));
		romType = BitUtils.getByte(rom, 0x0147);
		romSize = BitUtils.getByte(rom, 0x0148);
		ramSize = BitUtils.getByte(rom, 0x0149);
		destinationCode = BitUtils.getByte(rom, 0x014A);
		licenseeCode = BitUtils.getByte(rom, 0x014B);
		romVersionNumber = BitUtils.getByte(rom, 0x014C);
		headerChecksum = BitUtils.getByte(rom, 0x014D);
		checksum = BitUtils.getByteRange(rom, 0x014E, 0x014F + 1);
		
		// see what we're working with
		logger.info(this);
	}
	
	public byte getRomType() {
		return romType;
	}
	
	public String getManufacterCode() {
		return manufacterCode;
	}

	public byte getRomSize() {
		return romSize;
	}

	public byte getRamSize() {
		return ramSize;
	}

	public byte getDestinationCode() {
		return destinationCode;
	}

	public byte getLicenseeCode() {
		return licenseeCode;
	}

	public byte getRomVersionNumber() {
		return romVersionNumber;
	}

	public byte getHeaderChecksum() {
		return headerChecksum;
	}

	public byte[] getChecksum() {
		return checksum;
	}

	public String getRomTitle() {
		return romTitle;
	}
	
	@Override
	public String toString() {
		return Map.of(
			"title", getRomTitle(),
			"checksum", new BigInteger(1, getChecksum()).toString(16),
			"header checksum", getHeaderChecksum(),
			"version number", getRomVersionNumber(),
			"licensee code", getLicenseeCode(),
			"destination code", getDestinationCode(),
			"rom type", getRomType(),
			"manufacturer code", getManufacterCode(),
			"rom size", getRomSize(),
			"ram size", getRamSize()
		).toString();
	}
}