package com.slasherx.zjgb.cpu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;
import com.slasherx.zjgb.cpu.CpuAbstract;
import com.slasherx.zjgb.peripherals.rom.AbstractRom;
import com.slasherx.zjgb.utils.BitUtils;

/**
 * @author Zenen Jaimes
 *
 */
public class LR35902 implements CpuAbstract {
	private static Logger logger;
	
	private byte nextOpcode = 0x0;
	private byte opcode2 = 0x0;
	private byte opcode3 = 0x0;

	private AbstractRom rom;
	
	private byte regA, regF = 0x0;
	private byte regB, regC = 0x0;
	private byte regD, regE = 0x0;
	private byte regH, regL = 0x0;
 
	private short SP = 0x0;
	private short PC = 0x0;
	private short nextPC = 0x0;
	private int counter = 0x0;
	
	private byte[] memory;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LR35902(Logger logger4j, AbstractRom rom) {
		logger = logger4j;
		setRom(rom);
		setMemory(new byte[0x10000]);
		
		// Load the rom data into memory
		// both banks into memory
		loadRomData(0);
		loadRomData(1);
	}
	
	public LR35902(Logger logger4j, AbstractRom rom, Map<String, Map<String, ?>> registers) {
		this(logger4j, rom);

		registers.forEach((k, v) -> {
			try {
				String normalizedSetterString = "set".concat(StringUtils.capitalize(k));						
				Method regSetter = this.getClass().getDeclaredMethod(normalizedSetterString, Class.forName((String) v.get("clazz")));
				
				regSetter.setAccessible(true);
				regSetter.invoke(this, v.get("value"));
				
				logger.info(normalizedSetterString);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException e) {
				e.printStackTrace();
				logger.error(e);
			}
		});
	}
	
	public void loadRomData(int bank) {
		int address = 0x8000 * bank;
		
		for (int i = 0; i < 0x8000; i++) {
			memory[address + i] = getRom().romData[address + i];
		}		
	}
	
	@Override
	public void run() {
		fetchNextInstruction();
		runNextInstruction();
	}
	
	public void setZeroFlag() {
		setRegF((byte)BitUtils.setBit(getRegF(), 7));
	}
	
	public void logCurrentCpuDebug() {
		logger.info("current pc {}", BitUtils.shortToString(PC));
		logger.info("current sp {}", BitUtils.shortToString(SP));
		logger.info("next pc {}", BitUtils.shortToString(nextPC));
		logger.info("next opcode {}", BitUtils.byteToString(nextOpcode));
		logger.info("op1: {} op2: {}", BitUtils.byteToString(opcode2), BitUtils.byteToString(opcode3));
	}
	
	public void handleInvalidCpuOperation() throws UnsupportedCpuOperation  {
		logCurrentCpuDebug();
		logger.info(this);
		throw new UnsupportedCpuOperation("Unexpected value: " + BitUtils.byteToString(nextOpcode));
	}
	
	public void setCpuStatus(short jumpToPC, int i) {
		nextPC = jumpToPC;
		counter += i;
	}
	
	@Override
	public void fetchNextInstruction() throws UnsupportedCpuOperation {
		PC = nextPC;
		nextOpcode = getRom().romData[PC];
		opcode2 = getRom().romData[PC + 1];
		opcode3 = getRom().romData[PC + 2];
	}
	
	@Override
	public void runNextInstruction() {
		logger.info("RUNNING INSTRUCTION AT PC: {}", BitUtils.shortToString(PC));
		logCurrentCpuDebug();
		
		switch ((nextOpcode & 0xFF)) {
			// LD
			case 0x01: //
			case 0x02:
			case 0x11:
			case 0x12:
			case 0x21:
			case 0x22:
			case 0x31:
			case 0x32:
			case 0x06: //
			case 0x16:
			case 0x26:
			case 0x36:
			case 0x08: //
			case 0x0A: //
			case 0x1A:
			case 0x2A:
			case 0x3A:
			case 0x0E: //
			case 0x1E:
			case 0x2E:
			case 0x3E:
			case 0xE0: //
			case 0xF0:
			case 0xE2: //
			case 0xF2:
			case 0xF8: //
			case 0xF9:
			case 0xFA:
			case 0xEA:
			case 0x40: //
			case 0x41:
			case 0x42:
			case 0x43:
			case 0x44:
			case 0x45:
			case 0x46:
			case 0x47:
			case 0x48:
			case 0x49:
			case 0x4a:
			case 0x4b:
			case 0x4c:
			case 0x4d:
			case 0x4e:
			case 0x4f:
			case 0x50:
			case 0x51:
			case 0x52:
			case 0x53:
			case 0x54:
			case 0x55:
			case 0x56:
			case 0x57:
			case 0x58:
			case 0x59:
			case 0x5a:
			case 0x5b:
			case 0x5c:
			case 0x5d:
			case 0x5e:
			case 0x5f:
			case 0x60:
			case 0x61:
			case 0x62:
			case 0x63:
			case 0x64:
			case 0x65:
			case 0x66:
			case 0x67:
			case 0x68:
			case 0x69:
			case 0x6a:
			case 0x6b:
			case 0x6c:
			case 0x6d:
			case 0x6e:
			case 0x6f:
			case 0x70:
			case 0x71:
			case 0x72:
			case 0x73:
			case 0x74:
			case 0x75:
			case 0x77:
			case 0x78:
			case 0x79:
			case 0x7a:
			case 0x7b:
			case 0x7c:
			case 0x7d:
			case 0x7e:
			case 0x7f:
				handleLoadMemory();
				break;
			// JP
			case 0xC2:
			case 0xC3:
			case 0xCA:
			case 0xD2:
			case 0xDA:
			case 0xE9:
				handleJump();
				break;
			// XOR
			case 0xA8:
			case 0xA9:
			case 0xAA:
			case 0xAB:
			case 0xAC:
			case 0xAD:
			case 0xAE:
			case 0xAF:
			case 0xEE:
				handleXor();
				break;
		
			default:
				handleInvalidCpuOperation();
		}
	}
	
	/**
	 * Hard implementation of the xor ops
	 * 
	 * @throws UnsupportedCpuOperation
	 */
	public void handleXor() throws UnsupportedCpuOperation {
		int cycles = 4;
		int opSize = 1;
		switch ((nextOpcode & 0xFF)) {
			case 0xA8:
				xor(regB);
				break;
			case 0xA9:
				xor(regC);
				break;
			case 0xAA:
				xor(regD);
				break;
			case 0xAB:
				xor(regE);
				break;
			case 0xAC:
				xor(regH);
				break;
			case 0xAD:
				xor(regL);
				break;
			//case 0xAE:
				//cycles = 8;
				//break;
			case 0xAF:
				xor(regA);
				break;
			case 0xEE:
				xor(opcode2); // Immediate xor
				cycles = 8;
				opSize = 2;
				break;
	
			default:
				handleInvalidCpuOperation();				
		}
		
		setCpuStatus((short)((PC & 0xFFFF) + opSize), cycles);
	}
	
	public void xor(byte op) {
		byte xored = (byte)((regA ^ op) & 0xFF);
		setRegA(xored);
		
		// set zero flag
		if (xored == 0) {
			setZeroFlag();
		}
	}
	
	public void handleLoadMemory() throws UnsupportedCpuOperation {
		int opSize = 1;
		int cycles = 4;
		switch ((nextOpcode & 0xFF)) {
//			case 0x01: //
//			case 0x02:
//			case 0x11:
//			case 0x12:
			case 0x21: // LD HL, imm16
				opSize = 3;
				cycles = 12;
				
				setRegL(opcode2); // Least significant bit
				setRegH(opcode3); 
				break;
//			case 0x22:
//			case 0x31:
//			case 0x32:
//			case 0x06: //
//			case 0x16:
//			case 0x26:
//			case 0x36:
//			case 0x08: //
//			case 0x0A: //
//			case 0x1A:
//			case 0x2A:
//			case 0x3A:
//			case 0x0E: //
//			case 0x1E:
//			case 0x2E:
//			case 0x3E:
//			case 0xE0: //
//			case 0xF0:
//			case 0xE2: //
//			case 0xF2:
//			case 0xF8: //
//			case 0xF9:
//			case 0xFA:
//			case 0xEA:
//			case 0x40: //
//			case 0x41:
//			case 0x42:
//			case 0x43:
//			case 0x44:
//			case 0x45:
//			case 0x46:
//			case 0x47:
//			case 0x48:
//			case 0x49:
//			case 0x4a:
//			case 0x4b:
//			case 0x4c:
//			case 0x4d:
//			case 0x4e:
//			case 0x4f:
//			case 0x50:
//			case 0x51:
//			case 0x52:
//			case 0x53:
//			case 0x54:
//			case 0x55:
//			case 0x56:
//			case 0x57:
//			case 0x58:
//			case 0x59:
//			case 0x5a:
//			case 0x5b:
//			case 0x5c:
//			case 0x5d:
//			case 0x5e:
//			case 0x5f:
//			case 0x60:
//			case 0x61:
//			case 0x62:
//			case 0x63:
//			case 0x64:
//			case 0x65:
//			case 0x66:
//			case 0x67:
//			case 0x68:
//			case 0x69:
//			case 0x6a:
//			case 0x6b:
//			case 0x6c:
//			case 0x6d:
//			case 0x6e:
//			case 0x6f:
//			case 0x70:
//			case 0x71:
//			case 0x72:
//			case 0x73:
//			case 0x74:
//			case 0x75:
//			case 0x77:
//			case 0x78:
//			case 0x79:
//			case 0x7a:
//			case 0x7b:
//			case 0x7c:
//			case 0x7d:
//			case 0x7e:
//			case 0x7f:
			default:
				handleInvalidCpuOperation();
		}
		setCpuStatus((short)((PC & 0xFFFF) + opSize), cycles);

	}
	
	/**
	 * Hard implementation of the jump ops
	 * 
	 * @throws UnsupportedCpuOperation
	 */
	public void handleJump() throws UnsupportedCpuOperation {
		switch ((nextOpcode & 0xFF)) {
			//case 0xC2:
			case 0xC3:
				setCpuStatus(BitUtils.bytesToShort(opcode3, opcode2), 12);
				break;
			//case 0xCA:
			//case 0xD2:
			//case 0xDA:
			//case 0xE9:
	
			default:
				handleInvalidCpuOperation();
		}
	}
	
	/**
	 * @return the rom
	 */
	public AbstractRom getRom() {
		return rom;
	}

	/**
	 * @param rom the rom to set
	 */
	public void setRom(AbstractRom rom) {
		this.rom = rom;
	}

	/**
	 * @return the memory
	 */
	public byte[] getMemory() {
		return memory;
	}

	/**
	 * @param memory the memory to set
	 */
	public void setMemory(byte[] memory) {
		this.memory = memory;
	}

	/**
	 * @param regA the regA to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegA(byte regA) {
		this.regA = regA;
		return this;
	}

	/**
	 * @param regF the regF toLR35902 set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegF(byte regF) {
		this.regF = regF;
		return this;
	}

	/**
	 * @param regB the regB to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegB(byte regB) {
		this.regB = regB;
		return this;
	}

	/**
	 * @param regC the regC to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegC(byte regC) {
		this.regC = regC;
		return this;
	}

	/**
	 * @param regD the regD to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegD(byte regD) {
		this.regD = regD;
		return this;
	}

	/**
	 * @param regE the regE to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegE(byte regE) {
		this.regE = regE;
		return this;
	}

	/**
	 * @param regH the regH to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegH(byte regH) {
		this.regH = regH;
		return this;
	}

	/**
	 * @param regL the regL to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegL(byte regL) {
		this.regL = regL;
		return this;
	}

	/**
	 * @param sP the sP to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setSP(Short SP) {
		this.SP = SP;
		return this;
	}

	/**
	 * @param pC the pC to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setPC(Short PC) {
		this.PC = PC;
		return this;
	}

	public short regAF() {
		return (short) (((getRegA() & 0xFF) << 8) + (getRegF() & 0xFF));
	}
	
	public short regBC() {
		return (short) (((getRegB() & 0xFF) << 8) + (getRegC() & 0xFF));
	}
	
	public short regDE() {
		return (short) (((getRegD() & 0xFF) << 8) + (getRegE() & 0xFF));
	}
	
	
	public short regHL() {
		return (short) (((getRegH() & 0xFF) << 8) + (getRegL() & 0xFF));
	}

	/**
	 * @return the regA
	 */
	public byte getRegA() {
		return regA;
	}

	/**
	 * @return the regF
	 */
	public byte getRegF() {
		return regF;
	}

	/**
	 * @return the regB
	 */
	public byte getRegB() {
		return regB;
	}

	/**
	 * @return the regC
	 */
	public byte getRegC() {
		return regC;
	}

	/**
	 * @return the regD
	 */
	public byte getRegD() {
		return regD;
	}

	/**
	 * @return the regE
	 */
	public byte getRegE() {
		return regE;
	}

	/**
	 * @return the regH
	 */
	public byte getRegH() {
		return regH;
	}

	/**
	 * @return the regL
	 */
	public byte getRegL() {
		return regL;
	}

	/**
	 * @return the sP
	 */
	public Short getSP() {
		return SP;
	}

	/**
	 * @return the pC
	 */
	public Short getPC() {
		return PC;
	}
	
	@Override
	public String toString() {
		String cpuToString = String.join("\n","",
				"A: " + BitUtils.byteToString(getRegA()),
				"F: " + BitUtils.byteToString(getRegF()),
				"B: " + BitUtils.byteToString(getRegB()),
				"C: " + BitUtils.byteToString(getRegC()),
				"D: " + BitUtils.byteToString(getRegD()),
				"E: " + BitUtils.byteToString(getRegE()),
				"H: " + BitUtils.byteToString(getRegH()),
				"L: " + BitUtils.byteToString(getRegL()),
				"AF: " + BitUtils.shortToString(regAF()),
				"BC: " + BitUtils.shortToString(regBC()),
				"DE: " + BitUtils.shortToString(regDE()),
				"HL: " + BitUtils.shortToString(regHL()),
				"Program Counter: " + BitUtils.shortToString(getPC()),
				"Stack Pointer: " + BitUtils.shortToString(getSP())
		);
		
		return cpuToString;
	}
}
