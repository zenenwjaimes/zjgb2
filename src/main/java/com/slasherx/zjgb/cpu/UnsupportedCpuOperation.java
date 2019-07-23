/**
 * 
 */
package com.slasherx.zjgb.cpu;

/**
 * @author slasherx
 *
 */
public class UnsupportedCpuOperation extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -401556550378467853L;

	/**
	 * 
	 */
	public UnsupportedCpuOperation() {

	}

	/**
	 * @param message
	 */
	public UnsupportedCpuOperation(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnsupportedCpuOperation(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnsupportedCpuOperation(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnsupportedCpuOperation(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
