package com.slasherx.zjgb;

import org.apache.logging.log4j.Logger;

import com.slasherx.zjgb.cpu.LR35902;
import com.slasherx.zjgb.peripherals.RomLoader;

public class GameBoy implements Runnable {
	private LR35902 cpu;
	private RomLoader romLoader;
	private boolean isRunning = false;
	private volatile boolean isCanceled = false;
	private static Logger logger;

	private GameBoy() {
		// logger.info("* ChaChing sound from turning on gameboy *");
	}
	
	public GameBoy(Logger logger4j, RomLoader loader) {
		this();
		logger = logger4j;
		romLoader = loader;
		cpu = new LR35902(logger, loader.romInstance);
	}
	
	public RomLoader getRomLoader() {
		return romLoader;
	}

	public GameBoy setRomLoader(RomLoader romLoader) {
		this.romLoader = romLoader;
		
		return this;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public GameBoy setRunning(boolean isRunning) {
		this.isRunning = isRunning;
		return this;
	}

	public boolean isCanceled() {
		return isCanceled;
	}

	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	@Override
	public void run() {
		while(!isCanceled) {
			if (isRunning) {
				cpu.run();
			}
		}
	}
}
