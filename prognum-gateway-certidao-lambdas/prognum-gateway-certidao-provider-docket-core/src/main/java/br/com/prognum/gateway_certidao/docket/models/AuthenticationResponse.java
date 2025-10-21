package br.com.prognum.gateway_certidao.docket.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AuthenticationResponse {
	private String token;
}
