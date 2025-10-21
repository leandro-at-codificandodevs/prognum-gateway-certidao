package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class InternalServerException extends RuntimeException {

	public InternalServerException(String message) {
		super(message);
	}
	
	public InternalServerException(Throwable cause) {
		super(cause);
	}
}
