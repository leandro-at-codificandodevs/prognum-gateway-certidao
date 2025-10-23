package br.com.prognum.gateway_certidao.core.models;

import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;
import lombok.Data;

@Data
public class States {
	private List<State> states;
	
	public State getStateByAcronymn(String acronymn) {
		for (State state : this.states) {
			if (state.getAcronymn().equals(acronymn)) {
				return state;
			}
		}
		throw new StateNotFoundException(acronymn);
	}
}
