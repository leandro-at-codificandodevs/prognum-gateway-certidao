package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class StateNotFoundException extends RuntimeException {
	public StateNotFoundException(String stateId) {
		super(String.format("Estado %s não encontrado", stateId));
	}
}
