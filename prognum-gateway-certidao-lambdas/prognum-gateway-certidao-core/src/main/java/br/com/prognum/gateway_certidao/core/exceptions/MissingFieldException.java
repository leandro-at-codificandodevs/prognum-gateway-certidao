package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class MissingFieldException  extends Exception {
	public MissingFieldException(String field) {
		super(String.format("Campo obrigatório ausente %s", field));
	}
}
