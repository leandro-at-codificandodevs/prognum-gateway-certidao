package br.com.prognum.gateway_certidao.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;

public class JsonServiceImpl implements JsonService {

	private ObjectMapper objectMapper;

	public JsonServiceImpl() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
	}
	
	@Override
	public <T> T fromJson(String content, Class<T> type) throws FromJsonException {
		try {
			return this.objectMapper.readValue(content, type);
		} catch (JsonProcessingException e) {
			throw new FromJsonException(e);
		}
	}
	
	@Override
	public <T> String toJson(T object) throws ToJsonException {
		try {
			return this.objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new ToJsonException(e);
		}
	}
}
