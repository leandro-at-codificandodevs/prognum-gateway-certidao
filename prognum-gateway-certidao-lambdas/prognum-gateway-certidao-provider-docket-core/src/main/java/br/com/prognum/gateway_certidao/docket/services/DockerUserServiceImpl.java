package br.com.prognum.gateway_certidao.docket.services;

import br.com.prognum.gateway_certidao.core.services.SecretService;
import br.com.prognum.gateway_certidao.docket.models.DocketUser;

public class DockerUserServiceImpl implements DocketUserService {

	private String docketUserSecretName;
	
	private SecretService secretService;
	
	private DocketUser docketUser;

	public DockerUserServiceImpl(String docketUserSecretName, SecretService secretService) {
		this.docketUserSecretName = docketUserSecretName;
		this.secretService = secretService;
	}

	public DocketUser getDocketUser() {
		if (docketUser == null) {
			docketUser = secretService.read(docketUserSecretName, DocketUser.class);
		}
		return docketUser;
	}
}
