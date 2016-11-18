package com.test.mytest.testMain;

/**
 ************************************************************************************ 
 * 自定义异常
 * @author liuxiang
 * @since 2014-11-28 下午3:38:16
 * @version 1.0
 ************************************************************************************ 
 */
public class CustomException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomException() {
		super();
	}

	public CustomException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public CustomException(String detailMessage) {
		super(detailMessage);
	}

	public CustomException(Throwable throwable) {
		super(throwable);
	}

}
