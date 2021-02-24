package ar.com.atsa.services;


public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -262040695050447131L;
	
	public ServiceException(String msg, Throwable e) {
		super(msg, e);
	}

}
