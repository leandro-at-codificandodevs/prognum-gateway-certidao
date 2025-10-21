package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class UnknownMessageTypeException extends Exception {
	public UnknownMessageTypeException(String messageType) {
		super(String.format("Tipo de mensagem %s desconhecida", messageType));
	}
}
