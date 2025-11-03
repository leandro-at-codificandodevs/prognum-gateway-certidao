package br.com.prognum.gateway_certidao.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;

class StatesTest {
	@Test
	void testGetStateByAcronymnWithKnownState() throws StateNotFoundException {
		States states123 = new States();
		State state1 = new State();
		String acronymn1 = "RJ";
		state1.setAcronymn(acronymn1);
		State state2 = new State();
		String acronymn2 = "SP";
		state2.setAcronymn(acronymn2);
		State state3 = new State();
		String acronymn3 = "MG";
		state3.setAcronymn(acronymn3);
		states123.setStates(List.of(state1, state2, state3));

		assertEquals(state1, states123.getStateByAcronymn(acronymn1));
	}
	
	@Test()
	void testGetStateByAcronymnWithUnknownState() {
		States states123 = new States();
		State state1 = new State();
		String acronymn1 = "RJ";
		state1.setAcronymn(acronymn1);
		State state2 = new State();
		String acronymn2 = "SP";
		state2.setAcronymn(acronymn2);
		State state3 = new State();
		String acronymn3 = "MG";
		state3.setAcronymn(acronymn3);
		states123.setStates(List.of(state1, state2, state3));
		
		String acronymn4 = "RS";

		assertThrows(StateNotFoundException.class, () -> states123.getStateByAcronymn(acronymn4));
	}
	
	@Test
	void testGetStatesByAcronymnWithKnownState() throws StateNotFoundException {
		States states123 = new States();
		State state1 = new State();
		String acronymn1 = "RJ";
		state1.setAcronymn(acronymn1);
		State state2 = new State();
		String acronymn2 = "SP";
		state2.setAcronymn(acronymn2);
		State state3 = new State();
		String acronymn3 = "MG";
		state3.setAcronymn(acronymn3);
		states123.setStates(List.of(state1, state2, state3));

		States states13 = new States();
		states13.setStates(List.of(state1, state3));

		assertEquals(states13, states123.getStatesByAcronymn(acronymn1, acronymn3));
	}
	
	@Test()
	void testGetStatesByAcronymnWithUnknownState() {
		States states123 = new States();
		State state1 = new State();
		String acronymn1 = "RJ";
		state1.setAcronymn(acronymn1);
		State state2 = new State();
		String acronymn2 = "SP";
		state2.setAcronymn(acronymn2);
		State state3 = new State();
		String acronymn3 = "MG";
		state3.setAcronymn(acronymn3);
		states123.setStates(List.of(state1, state2, state3));
		
		String acronymn4 = "RS";

		assertThrows(StateNotFoundException.class, () -> states123.getStatesByAcronymn(acronymn1, acronymn4));
	}
}
