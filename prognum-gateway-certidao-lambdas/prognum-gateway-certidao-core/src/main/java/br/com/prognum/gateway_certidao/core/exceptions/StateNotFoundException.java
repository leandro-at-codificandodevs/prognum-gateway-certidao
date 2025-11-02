package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class StateNotFoundException extends Exception {
	public StateNotFoundException(String stateAcronymn) {
		super(String.format("Estado %s não encontrado", stateAcronymn));
	}
}
