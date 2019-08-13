package com.slasherx.zjgb.peripherals;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.slasherx.zjgb.cpu.LR35902;
import com.slasherx.zjgb.cpu.UnsupportedCpuOperation;
import com.slasherx.zjgb.utils.BitUtils;

@DisplayName("Cpu Operations")
@TestInstance(Lifecycle.PER_CLASS)

/**
 * @author Zenen Jaimes
 *
 */
public class LR35902OpsTest {
	
	private final static Logger logger = LogManager.getRootLogger();

	private LR35902 gameboyCpu;
	
	@BeforeAll
	void setupCpu() throws Exception {
		gameboyCpu = new LR35902(LR35902OpsTest.logger, new RomLoader(logger, "/Users/slasherx/test.gb").romInstance);
	}
	
	@BeforeEach
	void setUp() throws Exception {
		Map<String, Map<String, ?>> registers = new HashMap<String, Map<String, ?>>();

		registers.put("regA", Map.of("clazz", "byte", "value", (byte) 0x01));
		registers.put("regF", Map.of("clazz", "byte", "value", (byte) 0xB0));
		registers.put("regB", Map.of("clazz", "byte", "value", (byte) 0x00));
		registers.put("regC", Map.of("clazz", "byte", "value", (byte) 0x13));
		registers.put("regD", Map.of("clazz", "byte", "value", (byte) 0x00));
		registers.put("regE", Map.of("clazz", "byte", "value", (byte) 0xD8));
		registers.put("regH", Map.of("clazz", "byte", "value", (byte) 0x01));
		registers.put("regL", Map.of("clazz", "byte", "value", (byte) 0x4D));
		
		registers.put("SP", Map.of("clazz", "short", "value", (short) 0xFFFE));
		registers.put("PC", Map.of("clazz", "short", "value", (short) 0x100));
		
		gameboyCpu.setInitialState(registers);
	}

	@AfterAll
	void tearDown() throws Exception {
		logger.info("after tear down at parent level class");

		gameboyCpu = null;
	}
	
	@Test
	@DisplayName("Read random rom value from bank 0")
	void testRandomRomRead() {
		byte readValue = gameboyCpu.readValueFromMemory((short)0x1E6);
		assertEquals(readValue, (byte)0xF0);
	}
	
	@Test
	@DisplayName("Can Throw Invalid Operation Exception")
	void testCanThrowUnsupportedCpuOperation() {
		assertThrows(UnsupportedCpuOperation.class, () -> {
			gameboyCpu.handleInvalidCpuOperation();
		});
	}

	@Test
	@DisplayName("Invalid Operation Test")
	void testUnsupportedCpuOperation() {
		assertThrows(UnsupportedCpuOperation.class, () -> {
			gameboyCpu.setCpuStatus("Invalid Op", 1, (short)0x76C, 4);
			gameboyCpu.runNextInstruction();
		});
	}
	
	@Nested
	class XorTests {
		@Test
		@DisplayName("Test XOR B with 0x1")
		void testXorB_zero() {
			Map<String, Map<String, ?>> registers = new HashMap<String, Map<String, ?>>();
			registers.put("regA", Map.of("clazz", "byte", "value", (byte) 0x01));
			registers.put("regB", Map.of("clazz", "byte", "value", (byte) 0x01));
			registers.put("nextOpcode", Map.of("clazz", "byte", "value", (byte) 0xA8));
			gameboyCpu.setInitialState(registers);			
			gameboyCpu.runNextInstruction();
			
			assertEquals(gameboyCpu.getRegA(), (byte)0x0, "Result of A is not 0x0");
		}
	}
}
