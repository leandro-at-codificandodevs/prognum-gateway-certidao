package br.com.prognum.gateway_certidao.core.models;

import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.CityNotFoundException;
import lombok.Data;

@Data
public class State {
	private String acronymn;
	private List<City> cities;
	
	public City getCityByName(String name) throws CityNotFoundException {
		for (City city : this.cities) {
			if (city.getName().equalsIgnoreCase(name)) {
				return city;
			}
		}
		throw new CityNotFoundException(name);
	}
}
