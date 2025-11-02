package br.com.prognum.gateway_certidao.core.models;

import java.util.LinkedList;
import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;
import lombok.Data;

@Data
public class States {
	private List<State> states;
	
	public State getStateByAcronymn(String acronymn) throws StateNotFoundException {
		for (State state : this.states) {
			if (state.getAcronymn().equalsIgnoreCase(acronymn)) {
				return state;
			}
		}
		throw new StateNotFoundException(acronymn);
	}
	
	public States getStatesByAcronymn(String... acronymns) throws StateNotFoundException {
		States states = new States();
		List<State> selectedStates = new LinkedList<State>();
		for (String acronymn : acronymns) {
			selectedStates.add(getStateByAcronymn(acronymn));
		}
		states.setStates(selectedStates);
		return states;
	}
}
