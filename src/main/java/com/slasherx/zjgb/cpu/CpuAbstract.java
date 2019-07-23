package com.slasherx.zjgb.cpu;

import java.io.Serializable;

/**
 * @author Zenen Jaimes
 *
 */
public interface CpuAbstract extends Serializable {
	public void run();
	public void fetchNextInstruction() throws UnsupportedCpuOperation;
	public void runNextInstruction();
}
