package com.cqvip.moblelib.net;

public class BookException  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1618723987275550959L;
	private int statusCode = -1;
	
    public BookException(String msg) {
        super(msg);
    }

    public BookException(Exception cause) {
        super(cause);
    }

    public BookException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public BookException(String msg, Exception cause) {
        super(msg, cause);
    }

    public BookException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {   
        return this.statusCode;
    }
    
    
	public BookException() {
		super(); 
	}

	public BookException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BookException(Throwable throwable) {
		super(throwable);
	}

	public BookException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
