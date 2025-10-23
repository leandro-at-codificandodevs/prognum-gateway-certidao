package br.com.prognum.gateway_certidao.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;

public class JsonServiceImpl implements JsonService {

	private ObjectMapper objectMapper;

	public JsonServiceImpl() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	@Override
	public <T> T fromJson(String content, Class<T> type) throws FromJsonException {
		try {
			T obj = this.objectMapper.readValue(content, type);
			return obj;
		} catch (JsonProcessingException e) {
			throw new FromJsonException(e);
		}
	}
	
	@Override
	public <T> String toJson(T object) throws ToJsonException {
		try {
			String content = this.objectMapper.writeValueAsString(object);
			return content;
		} catch (JsonProcessingException e) {
			throw new ToJsonException(e);
		}
	}
}
