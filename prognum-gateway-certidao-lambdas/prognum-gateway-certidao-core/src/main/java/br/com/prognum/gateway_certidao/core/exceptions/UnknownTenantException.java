package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class UnknownTenantException extends Exception {
	public UnknownTenantException(String tenant) {
		super(String.format("Tenant %s desconhecido", tenant));
	}

}
