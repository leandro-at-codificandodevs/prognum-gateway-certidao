package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class InvalidFormatterDateException extends Exception {
	public InvalidFormatterDateException(String field) {
		super(String.format("a data %s está inválida. Utilize o padrão yyyy-MM-dd", field));
	}
}