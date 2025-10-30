package br.com.prognum.gateway_certidao.docket_core.services;

public interface DocketAuthService {

	String getToken();

	void invalidateToken();
}