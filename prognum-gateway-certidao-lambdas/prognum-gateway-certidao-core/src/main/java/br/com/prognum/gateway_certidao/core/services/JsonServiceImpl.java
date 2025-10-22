package br.com.prognum.gateway_certidao.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;

public class JsonServiceImpl implements JsonService {

	private static final Logger logger = LoggerFactory.getLogger(JsonServiceImpl.class);

	private ObjectMapper objectMapper;

	public JsonServiceImpl() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
	}
	
	@Override
	public <T> T fromJson(String content, Class<T> type) throws FromJsonException {
		try {
			logger.debug("Unmarshall - a fazer: tipo: {} conteúdo: {}", type.getSimpleName(), content);
			T obj = this.objectMapper.readValue(content, type);
			logger.debug("Unmarshall - feito: {}  tipo: {} conteúdo: {} objeto: {}", type.getSimpleName(), content, obj);
			return obj;
		} catch (JsonProcessingException e) {
			throw new FromJsonException(e);
		}
	}
	
	@Override
	public <T> String toJson(T object) throws ToJsonException {
		try {
			logger.debug("Marshall - a fazer: objeto: {}", object);
			String content = this.objectMapper.writeValueAsString(object);
			logger.debug("Marshall - feito: objeto: {} - conteúdo: {}", object, content);
			return content;
		} catch (JsonProcessingException e) {
			throw new ToJsonException(e);
		}
	}
}
