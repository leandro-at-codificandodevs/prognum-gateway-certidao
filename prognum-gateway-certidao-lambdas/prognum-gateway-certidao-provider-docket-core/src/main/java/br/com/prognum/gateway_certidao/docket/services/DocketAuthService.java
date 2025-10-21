package br.com.prognum.gateway_certidao.docket.services;

public interface DocketAuthService {

	String getToken();

	void invalidateToken();
}