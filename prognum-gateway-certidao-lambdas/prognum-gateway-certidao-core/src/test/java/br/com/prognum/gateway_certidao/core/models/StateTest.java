package br.com.prognum.gateway_certidao.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.prognum.gateway_certidao.core.exceptions.CityNotFoundException;

class StateTest {
	@Test
	void testGetCityBynameWithKnownCity() throws CityNotFoundException {
		State state = new State();
		City city1  = new City();
		String name1 = "Rio de Janeiro";
		city1 .setName(name1);
		City city2  = new City();
		String name2 = "Duque de Caxias";
		city2 .setName(name2);
		City city3 = new City();
		String name3 = "Niterói";
		city3.setName(name3);
		state.setCities(List.of(city1 , city2 , city3));

		assertEquals(city1 , state.getCityByName(name1));
	}
	
	@Test()
	void testGetCityBynameWithUnknownCity() {
		State state = new State();
		City city1  = new City();
		String name1 = "Rio de Janeiro";
		city1 .setName(name1);
		City city2  = new City();
		String name2 = "Duque de Caxias";
		city2 .setName(name2);
		City city3 = new City();
		String name3 = "Niterói";
		city3.setName(name3);
		state.setCities(List.of(city1 , city2 , city3));
		
		String name4 = "Porto Alegre";

		assertThrows(CityNotFoundException.class, () -> state.getCityByName(name4));
	}
}
