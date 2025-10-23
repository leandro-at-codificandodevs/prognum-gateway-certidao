package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class InvalidDateException extends Exception {

	public InvalidDateException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDateException(String message) {
		super(message);
	}

	public InvalidDateException(Throwable cause) {
		super(cause);
	}

}