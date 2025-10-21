package br.com.prognum.gateway_certidao.core.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentGroupStatus {

	READY, PREPARING;

	@JsonValue
	public String toJsonValue() {
		return name().toLowerCase();
	}

}
