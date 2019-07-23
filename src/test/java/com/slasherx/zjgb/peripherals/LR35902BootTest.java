package com.slasherx.zjgb.peripherals;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


import com.slasherx.zjgb.cpu.LR35902;

@DisplayName("After Loading Boot Rom Test")
@TestInstance(Lifecycle.PER_CLASS)

/**
 * @author Zenen Jaimes
 *
 */
public class LR35902BootTest {
	
	private final static Logger logger = LogManager.getRootLogger();

	private LR35902 gameboyCpu; 
	@BeforeAll
	void setUp() throws Exception {
		Map<String, Map<String, ?>> registers = new HashMap<String, Map<String, ?>>();

		registers.put("regA", Map.of("clazz", Byte.class.getName(), "value", (byte) 0x01));
		registers.put("regF", Map.of("clazz", Byte.class.getName(), "value", (byte) 0xB0));
		registers.put("regB", Map.of("clazz", Byte.class.getName(), "value", (byte) 0x00));
		registers.put("regC", Map.of("clazz", Byte.class.getName(), "value", (byte) 0x13));
		registers.put("regD", Map.of("clazz", Byte.class.getName(), "value", (byte) 0x00));
		registers.put("regE", Map.of("clazz", Byte.class.getName(), "value", (byte) 0xD8));
		registers.put("regH", Map.of("clazz", Byte.class.getName(), "value", (byte) 0x01));
		registers.put("regL", Map.of("clazz", Byte.class.getName(), "value", (byte) 0x4D));
		
		registers.put("SP", Map.of("clazz", Short.class.getName(), "value", (short) 0xFFFE));
		registers.put("PC", Map.of("clazz", Short.class.getName(), "value", (short) 0x100));


		gameboyCpu = new LR35902(LR35902BootTest.logger, new RomLoader(logger, "/Users/slasherx/test.gb").romInstance, registers);
		logger.info(gameboyCpu);
	}

	@AfterAll
	void tearDown() throws Exception {
		gameboyCpu = null;
	}

	@Test
	@DisplayName("Reg A")
	void testBootRegA() {
		assertEquals((byte) 0x01, gameboyCpu.getRegA());
	}
	
	@Test
	@DisplayName("Reg F")
	void testBootRegF() {
		assertEquals((byte) 0xB0, gameboyCpu.getRegF());
	}
	
	@Test
	@DisplayName("Reg B")
	void testBootRegB() {
		assertEquals((byte) 0x00, gameboyCpu.getRegB());
	}
	
	@Test
	@DisplayName("Reg C")
	void testBootRegC() {
		assertEquals((byte) 0x13, gameboyCpu.getRegC());
	}
	
	@Test
	@DisplayName("Reg D")
	void testBootRegD() {
		assertEquals((byte) 0x00, gameboyCpu.getRegD());
	}
	
	@Test
	@DisplayName("Reg E")
	void testBootRegE() {
		assertEquals((byte) 0xD8, gameboyCpu.getRegE());
	}
	
	@Test
	@DisplayName("Reg H")
	void testBootRegH() {
		assertEquals((byte) 0x01, gameboyCpu.getRegH());
	}
	
	@Test
	@DisplayName("Reg L")
	void testBootRegL() {
		assertEquals((byte) 0x4D, gameboyCpu.getRegL());
	}
	
	@Test
	@DisplayName("Program Counter")
	void testBootRegPC() {
		assertEquals((short) 0x100, gameboyCpu.getPC());
	}
	
	@Test
	@DisplayName("Stack Pointer")
	void testBootRegSP() {
		assertEquals((short) 0xFFFE, gameboyCpu.getSP());
	}
	
	@Test
	@DisplayName("Reg AF")
	void testBootRegAF() {
		assertEquals((short) 0x01B0, gameboyCpu.regAF());
	}
	
	@Test
	@DisplayName("Reg BC")
	void testBootReBC() {
		assertEquals((short) 0x0013, gameboyCpu.regBC());
	}
	
	@Test
	@DisplayName("Reg DE")
	void testBootReDE() {
		assertEquals((short) 0x00D8, gameboyCpu.regDE());
	}
	
	@Test
	@DisplayName("Reg HL")
	void testBootReHL() {
		assertEquals((short) 0x014D, gameboyCpu.regHL());
	}
}
