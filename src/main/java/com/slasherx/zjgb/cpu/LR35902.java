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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LR35902(Logger logger4j, AbstractRom rom) {
		logger = logger4j;
		this.rom = rom;
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
		nextOpcode = rom.romData[PC];
		opcode2 = rom.romData[PC + 1];
		opcode3 = rom.romData[PC + 2];
	}
	
	@Override
	public void runNextInstruction() {
		logger.info("RUNNING INSTRUCTION AT PC: {}", BitUtils.shortToString(PC));
		logCurrentCpuDebug();
		
		switch ((nextOpcode & 0xFF)) {
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

	public Short regAF() {
		return (short) (((getRegA() & 0xFF) << 8) + (getRegF() & 0xFF));
	}
	
	public Short regBC() {
		return (short) (((getRegB() & 0xFF) << 8) + (getRegC() & 0xFF));
	}
	
	public Short regDE() {
		return (short) (((getRegD() & 0xFF) << 8) + (getRegE() & 0xFF));
	}
	
	
	public Short regHL() {
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
