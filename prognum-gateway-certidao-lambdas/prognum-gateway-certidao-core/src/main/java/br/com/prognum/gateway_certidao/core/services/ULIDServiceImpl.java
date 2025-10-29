package br.com.prognum.gateway_certidao.core.services;

import de.huxhorn.sulky.ulid.ULID;

public class ULIDServiceImpl implements ULIDService {

	private ULID ulid;
	
	public ULIDServiceImpl() {
		this.ulid = new ULID();
	}

	@Override
	public String next() {
		return ulid.nextULID();
	}
}
