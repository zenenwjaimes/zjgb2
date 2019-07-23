package com.slasherx.zjgb.cpu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.*;
import com.slasherx.zjgb.cpu.CpuAbstract;
import com.slasherx.zjgb.peripherals.rom.AbstractRom;

/**
 * @author Zenen Jaimes
 *
 */
public class LR35902 implements CpuAbstract {
	private static Logger logger;
	
	private byte nextPC;
	private byte nextOpcode;

	private AbstractRom rom;
	
	private Byte regA, regF = 0x0;
	private Byte regB, regC = 0x0;
	private Byte regD, regE = 0x0;
	private Byte regH, regL = 0x0;
 
	private Short SP = 0x0;
	private Short PC = 0x0;
	
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
		// logger.info("run next step");
		
	}
	
	@Override
	public void fetchNextInstruction() throws UnsupportedCpuOperation {

	}
	
	@Override
	public void runNextInstruction() {
		
	}
	
	/**
	 * @param regA the regA to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegA(Byte regA) {
		this.regA = regA;
		return this;
	}

	/**
	 * @param regF the regF toLR35902 set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegF(Byte regF) {
		this.regF = regF;
		return this;
	}

	/**
	 * @param regB the regB to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegB(Byte regB) {
		this.regB = regB;
		return this;
	}

	/**
	 * @param regC the regC to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegC(Byte regC) {
		this.regC = regC;
		return this;
	}

	/**
	 * @param regD the regD to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegD(Byte regD) {
		this.regD = regD;
		return this;
	}

	/**
	 * @param regE the regE to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegE(Byte regE) {
		this.regE = regE;
		return this;
	}

	/**
	 * @param regH the regH to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegH(Byte regH) {
		this.regH = regH;
		return this;
	}

	/**
	 * @param regL the regL to set
	 */
	@SuppressWarnings("unused")
	private LR35902 setRegL(Byte regL) {
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
	public Byte getRegA() {
		return regA;
	}

	/**
	 * @return the regF
	 */
	public Byte getRegF() {
		return regF;
	}

	/**
	 * @return the regB
	 */
	public Byte getRegB() {
		return regB;
	}

	/**
	 * @return the regC
	 */
	public Byte getRegC() {
		return regC;
	}

	/**
	 * @return the regD
	 */
	public Byte getRegD() {
		return regD;
	}

	/**
	 * @return the regE
	 */
	public Byte getRegE() {
		return regE;
	}

	/**
	 * @return the regH
	 */
	public Byte getRegH() {
		return regH;
	}

	/**
	 * @return the regL
	 */
	public Byte getRegL() {
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
				"A: " + getRegA().toString(),
				"F: " + getRegF().toString(),
				"B: " + getRegB().toString(),
				"C: " + getRegC().toString(),
				"D: " + getRegD().toString(),
				"E: " + getRegE().toString(),
				"H: " + getRegH().toString(),
				"L: " + getRegL().toString(),
				"AF: " + regAF().toString(),
				"BC: " + regBC().toString(),
				"DE: " + regDE().toString(),
				"HL: " + regHL().toString(),
				"Program Counter: " + getPC().toString(),
				"Stack Pointer: " + getSP().toString()
		);
		
		return cpuToString;
	}
}
