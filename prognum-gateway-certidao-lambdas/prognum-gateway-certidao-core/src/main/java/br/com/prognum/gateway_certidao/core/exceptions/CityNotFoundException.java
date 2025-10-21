package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class CityNotFoundException extends RuntimeException {
	public CityNotFoundException(String cityIdOrName) {
		super(String.format("Cidade %s não encontrada", cityIdOrName));
	}
}
