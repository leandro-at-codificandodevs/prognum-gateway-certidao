package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class CityNotFoundException extends RuntimeException {
	public CityNotFoundException(String cityName) {
		super(String.format("Cidade %s não foi encontrada", cityName));
	}
}
