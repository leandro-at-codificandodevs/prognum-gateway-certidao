package br.com.prognum.gateway_certidao.docket_core.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DocketUser {

	@JsonProperty("login")
	private String username;

	@JsonProperty("senha")
	private String password;
}
