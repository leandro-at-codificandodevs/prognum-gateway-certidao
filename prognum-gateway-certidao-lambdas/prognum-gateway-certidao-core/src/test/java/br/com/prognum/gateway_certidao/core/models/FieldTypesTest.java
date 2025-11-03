package br.com.prognum.gateway_certidao.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FieldTypesTest {

	@Test
	void testGetFieldTypeById_Catorio() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("cartorio", "Cartório", "Cartório");
		FieldType actual = fieldTypes.getFieldTypeById("cartorio");
		assertEquals(expected, actual);
	}

	@Test
	void testGetFieldTypeById_CEI() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("cei", "CEI", "Cadastro Específico do INSS");
		FieldType actual = fieldTypes.getFieldTypeById("cei");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_CEP() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("cep", "CEP", "Código de Endereço Postal");
		FieldType actual = fieldTypes.getFieldTypeById("cep");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_CPF() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("cpf", "CPF", "Cadastro de Pessoa Física");
		FieldType actual = fieldTypes.getFieldTypeById("cpf");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_Cidade() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("cidade", "Cidade", "Cidade");
		FieldType actual = fieldTypes.getFieldTypeById("cidade");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_CNPJ() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("cnpj", "CNPJ", "Cadastro Nacional de Pessoa Jurídica");
		FieldType actual = fieldTypes.getFieldTypeById("cnpj");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_DataDeCasamento() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("data-casamento", "Data de Casamento", "Data de Casamento");
		FieldType actual = fieldTypes.getFieldTypeById("data-casamento");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_DataDeNascimento() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("data-nascimento", "Data de Nascimento", "Data de Nascimento");
		FieldType actual = fieldTypes.getFieldTypeById("data-nascimento");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_Estado() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("estado", "Estado", "Estado");
		FieldType actual = fieldTypes.getFieldTypeById("estado");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_Folha() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("folha", "Folha", "Folha");
		FieldType actual = fieldTypes.getFieldTypeById("folha");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_Livro() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("livro", "Livro", "Livro");
		FieldType actual = fieldTypes.getFieldTypeById("livro");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_NomeCompleto() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("nome-completo", "Nome Completo", "Nome Completo");
		FieldType actual = fieldTypes.getFieldTypeById("nome-completo");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_NomeDaMae() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("nome-mae", "Nome da Mãe", "Nome da Mãe");
		FieldType actual = fieldTypes.getFieldTypeById("nome-mae");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_NumeroInscricaoImobiliaria() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("numero-inscricao-imobiliaria", "Número Inscrição Imobiliária", "Número Inscrição Imobiliária");
		FieldType actual = fieldTypes.getFieldTypeById("numero-inscricao-imobiliaria");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_NumeroMatriculaImovel() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("numero-matricula-imovel", "Número de Matrícula do Imóvel", "Número de Matrícula do Imóvel");
		FieldType actual = fieldTypes.getFieldTypeById("numero-matricula-imovel");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_RazaoSocial() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("razao-social", "Razão Social", "Razão Social");
		FieldType actual = fieldTypes.getFieldTypeById("razao-social");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_RG() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("rg", "RG", "Registro Geral");
		FieldType actual = fieldTypes.getFieldTypeById("rg");
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetFieldTypeById_Termo() {
		FieldTypes fieldTypes = new FieldTypes();
		
		FieldType expected = new FieldType("termo", "Termo", "Termo");
		FieldType actual = fieldTypes.getFieldTypeById("termo");
		assertEquals(expected, actual);
	}
}
