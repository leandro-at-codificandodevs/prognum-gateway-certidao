package br.com.prognum.gateway_certidao.core.models;

import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.CityNotFoundException;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class State {
	private String id;
	private String acronymn;
	private String name;
	private List<City> cities;

	public City getCityById(String cityId) {
		City city = cities.stream().filter(c -> c.getId().equals(cityId)).findAny()
				.orElseThrow(() -> new CityNotFoundException(cityId));
		return city;
	}

	public City getCityByIdOrName(String cityIdOrName) {
		try {
			return getCityById(cityIdOrName);
		} catch (CityNotFoundException e) {
			log.debug("Cidade não encontrada pelo ID: {}", cityIdOrName);
		}

		try {
			return getCityByName(cityIdOrName);
		} catch (CityNotFoundException e) {
			log.debug("Cidade não encontrada pelo nome: {}", cityIdOrName);
		}

		throw new CityNotFoundException(cityIdOrName);
	}

	public City getCityByName(String cityName) {
		City city = cities.stream().filter(c -> c.getName().equals(cityName)).findAny()
				.orElseThrow(() -> new CityNotFoundException(cityName));
		return city;
	}
}
