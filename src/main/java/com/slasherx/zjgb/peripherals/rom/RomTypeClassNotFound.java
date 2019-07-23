/**
 * 
 */
package com.slasherx.zjgb.peripherals.rom;

/**
 * @author zenen jaimes
 *
 */
public class RomTypeClassNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4061435354807300990L;

	/**
	 * 
	 */
	public RomTypeClassNotFound() {
		
	}

	/**
	 * @param s
	 */
	public RomTypeClassNotFound(String s) {
		super(s);
	}

	/**
	 * @param s
	 * @param ex
	 */
	public RomTypeClassNotFound(String s, Throwable ex) {
		super(s, ex);
	}
}
