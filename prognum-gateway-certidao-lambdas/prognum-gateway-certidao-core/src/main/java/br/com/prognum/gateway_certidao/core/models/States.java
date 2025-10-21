package br.com.prognum.gateway_certidao.core.models;

import java.util.ArrayList;
import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.CityNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class States {
	public static final String RJ_STATE_ID = "ff49bbbe-e6bc-4e2a-b8cd-7eb3b877dcc3";
	public static final String RIO_DE_JANEIRO_CITY_ID = "5c24c842-49e1-4064-9eff-3ab8efad5bfb";
	public static final String NITEROI_CITY_ID = "b7ce7750-56f1-415e-b414-b9a1014cc1e5";

	public static final String SP_STATE_ID = "a3c97d37-a766-4233-8884-65914d23bacc";
	public static final String SAO_PAULO_CITY_ID = "8032de59-9607-48ba-a362-e0bafee74c86";

	public static final String PR_STATE_ID = "3a03af54-8a1f-4bb3-a732-2eab9b0a4334";
	public static final String CURITIBA_CITY_ID = "19b09a22-7dfc-4904-8843-278c08ea066f";

	public static final String DF_STATE_ID = "07b17fdd-d2dc-4691-a07a-dc908075a1b2";
	public static final String BRASILIA_CITY_ID = "3d0a6f91-69bb-44a3-a24f-5120aac8b3be";

	private List<State> states;

	public States() {
		states = new ArrayList<>();

		City rioDeJaneiroCity = new City(RIO_DE_JANEIRO_CITY_ID, "Rio de Janeiro");
		City niteroiCity = new City(NITEROI_CITY_ID, "Niterói");
		State rjState = new State(RJ_STATE_ID, "RJ", "Rio de Janeiro", List.of(rioDeJaneiroCity, niteroiCity));
		states.add(rjState);

		City saoPauloCity = new City(SAO_PAULO_CITY_ID, "São Paulo");
		State spState = new State(SP_STATE_ID, "SP", "São Paulo", List.of(saoPauloCity));
		states.add(spState);

		City curitibaCity = new City(CURITIBA_CITY_ID, "Curitiba");
		State prState = new State(PR_STATE_ID, "PR", "Paraná", List.of(curitibaCity));
		states.add(prState);

		City brasiliaCity = new City(BRASILIA_CITY_ID, "Brasília");
		State dfState = new State(DF_STATE_ID, "DF", "Brasília", List.of(brasiliaCity));
		states.add(dfState);
	}

	public City getCityByIdOrName(String cityIdOrName) {
		for (State state : states) {
			try {
				return state.getCityByIdOrName(cityIdOrName);
			} catch (CityNotFoundException e) {
				log.debug("Cidade {} não encontrada pelo id ou nome no Estado {}", cityIdOrName, state, e);
			}
		}

		throw new CityNotFoundException(cityIdOrName);
	}

	public State getStateByAcronymn(String stateAcronymn) {
		State state = states.stream().filter(s -> s.getAcronymn().equals(stateAcronymn)).findAny()
				.orElseThrow(() -> new StateNotFoundException(stateAcronymn));
		return state;
	}

	public State getStateById(String stateId) {
		State state = states.stream().filter(s -> s.getId().equals(stateId)).findAny()
				.orElseThrow(() -> new StateNotFoundException(stateId));
		return state;
	}

	public State getStateByIdOrAcronymnOrName(String stateIdOrAcronymnOrName) {
		try {
			return getStateById(stateIdOrAcronymnOrName);
		} catch (StateNotFoundException e) {
			log.debug("Estado não encontrado pelo id: {}", stateIdOrAcronymnOrName);
		}

		try {
			return getStateByAcronymn(stateIdOrAcronymnOrName);
		} catch (StateNotFoundException e) {
			log.debug("Estado não encontrado pela sigla: {}", stateIdOrAcronymnOrName);
		}

		try {
			return getStateByName(stateIdOrAcronymnOrName);
		} catch (StateNotFoundException e) {
			log.debug("Estado não encontrado pelo nome", stateIdOrAcronymnOrName);
		}

		throw new StateNotFoundException(stateIdOrAcronymnOrName);
	}

	public State getStateByName(String stateName) {
		State state = states.stream().filter(s -> s.getName().equals(stateName)).findAny()
				.orElseThrow(() -> new StateNotFoundException(stateName));
		return state;
	}
}