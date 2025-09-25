package edu.ccrm.util;

public class MaxCreditLimitExceededException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MaxCreditLimitExceededException(String message) {
        super(message);
    }
}