package br.com.prognum.gateway_certidao.docket.services;

import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_1;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_2;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_3;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_4;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_5;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_6;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.prognum.gateway_certidao.core.exceptions.CityNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.InvalidDateException;
import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;
import br.com.prognum.gateway_certidao.core.models.CreateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.models.FieldType;
import br.com.prognum.gateway_certidao.core.models.FieldTypes;
import br.com.prognum.gateway_certidao.core.utils.DateUtils;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest.Documento;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest.Pedido;
import br.com.prognum.gateway_certidao.docket.models.GetCidadesByEstadoResponse;
import br.com.prognum.gateway_certidao.docket.models.GetEstadosResponse;

public class DocketMapperServiceImpl implements DocketMapperService {

	private static final String CENTRO_CUSTO_ID = "feec1170-1f20-49b8-bd39-91315961e4bb";
	private static final String TIPO_OPERACAO_ID = "95e04d49-1926-498b-a68d-18273fe1287c";
	private static final String LEAD = "Operacao de credito - API - Docket - Prognum";
	private static final String GRUPO_ID = "ed892d79-7f0e-4248-98ef-a32382b0ab13";

	private DocketApiService docketApiService;

	private FieldTypes fieldTypes;

	private Map<String, Documento> documentosByDocumentTypeId;


	public DocketMapperServiceImpl(DocketApiService docketApiService) {
		this.docketApiService = docketApiService;

		fieldTypes = new FieldTypes();

		Documento doc1 = doc1();
		Documento doc2 = doc2();
		Documento doc3 = doc3();
		Documento doc4 = doc4();
		Documento doc5 = doc5();
		Documento doc6 = doc6();
		documentosByDocumentTypeId = new HashMap<>();
		documentosByDocumentTypeId.put(DOCUMENT_TYPE_1, doc1);
		documentosByDocumentTypeId.put(DOCUMENT_TYPE_2, doc2);
		documentosByDocumentTypeId.put(DOCUMENT_TYPE_3, doc3);
		documentosByDocumentTypeId.put(DOCUMENT_TYPE_4, doc4);
		documentosByDocumentTypeId.put(DOCUMENT_TYPE_5, doc5);
		documentosByDocumentTypeId.put(DOCUMENT_TYPE_6, doc6);
	}

	private Documento doc1() {
		String documentKitId = "23a66e13-336d-4aff-aab0-8bc765424d16";
		String produtoId = "c008556d-7391-d0e1-74ad-a7fc964b63db";
		String kitId = "4ded5a61-abca-4cda-81c2-7347b70defb1";

		String kitNome = "DOCUMENTOS DA JUSTIÇA ESTADUAL";
		String titularTipo = "PESSOA_FISICA";
		String documentoNome = "Certidão de Executivos Fiscais - Justiça Estadual (1° instância)";

		Documento document = new Documento();
		document.setDocumentKitId(documentKitId);
		document.setProdutoId(produtoId);
		document.setKitId(kitId);
		document.setKitNome(kitNome);
		document.setTitularTipo(titularTipo);
		document.setDocumentoNome(documentoNome);
		return document;
	}

	private Documento doc2() {
		String documentKitId = "7184c130-9d51-44dc-a244-30c3fb256dcc";
		String produtoId = "f31a19f4-dab5-c50b-4027-2c6d5a361df1";
		String kitId = "fcb6ce95-6f84-4c48-b5df-ef831b5e7d84";

		String kitNome = "RECEITA FEDERAL";
		String titularTipo = "PESSOA_FISICA";
		String documentoNome = "Certidão Conjunta de Débitos Relativos a Tributos Federais e a Dívida Ativa da União - Receita Federal";

		Documento document = new Documento();
		document.setDocumentKitId(documentKitId);
		document.setProdutoId(produtoId);
		document.setKitId(kitId);
		document.setKitNome(kitNome);
		document.setTitularTipo(titularTipo);
		document.setDocumentoNome(documentoNome);
		return document;
	}

	private Documento doc3() {
		String documentKitId = "6434a5bd-c5a8-4f51-8870-d7484529b53c";
		String produtoId = "df68df31-5b57-8e48-44c4-a4344a757b69";
		String kitId = "50d343c7-5a50-41a9-ac5b-dcd2911bdb6a";

		String kitNome = "DOCUMENTOS TRABALHISTAS";
		String titularTipo = "PESSOA_FISICA";
		String documentoNome = "Certidão Negativa de Débitos Trabalhistas - CNDT";

		Documento document = new Documento();
		document.setDocumentKitId(documentKitId);
		document.setProdutoId(produtoId);
		document.setKitId(kitId);
		document.setKitNome(kitNome);
		document.setTitularTipo(titularTipo);
		document.setDocumentoNome(documentoNome);
		return document;
	}

	private Documento doc4() {
		String documentKitId = "23a66e13-336d-4aff-aab0-8bc765424d16";
		String produtoId = "c008556d-7391-d0e1-74ad-a7fc964b63db";
		String kitId = "4ded5a61-abca-4cda-81c2-7347b70defb1";

		String kitNome = "DOCUMENTOS DA JUSTIÇA ESTADUAL";
		String titularTipo = "PESSOA_FISICA";
		String documentoNome = "Certidão de Executivos Fiscais - Justiça Estadual (1° instância)";

		Documento document = new Documento();
		document.setDocumentKitId(documentKitId);
		document.setProdutoId(produtoId);
		document.setKitId(kitId);
		document.setKitNome(kitNome);
		document.setTitularTipo(titularTipo);
		document.setDocumentoNome(documentoNome);
		return document;
	}

	private Documento doc5() {
		String documentKitId = "7184c130-9d51-44dc-a244-30c3fb256dcc";
		String produtoId = "f31a19f4-dab5-c50b-4027-2c6d5a361df1";
		String kitId = "fcb6ce95-6f84-4c48-b5df-ef831b5e7d84";

		String kitNome = "RECEITA FEDERAL";
		String titularTipo = "PESSOA_FISICA";
		String documentoNome = "Certidão Conjunta de Débitos Relativos a Tributos Federais e a Dívida Ativa da União - Receita Federal";

		Documento document = new Documento();
		document.setDocumentKitId(documentKitId);
		document.setProdutoId(produtoId);
		document.setKitId(kitId);
		document.setKitNome(kitNome);
		document.setTitularTipo(titularTipo);
		document.setDocumentoNome(documentoNome);
		return document;
	}

	private Documento doc6() {
		String documentKitId = "6434a5bd-c5a8-4f51-8870-d7484529b53c";
		String produtoId = "df68df31-5b57-8e48-44c4-a4344a757b69";
		String kitId = "50d343c7-5a50-41a9-ac5b-dcd2911bdb6a";

		String kitNome = "DOCUMENTOS TRABALHISTAS";
		String titularTipo = "PESSOA_FISICA";
		String documentoNome = "Certidão Negativa de Débitos Trabalhistas - CNDT";

		Documento document = new Documento();
		document.setDocumentKitId(documentKitId);
		document.setProdutoId(produtoId);
		document.setKitId(kitId);
		document.setKitNome(kitNome);
		document.setTitularTipo(titularTipo);
		document.setDocumentoNome(documentoNome);
		return document;
	}
	
	private String getEstadoId(CreateProviderDocumentGroupInput createProviderDocumentGroupInput) {
		String value = createProviderDocumentGroupInput.getFields().get(FieldTypes.ESTADO_FIELD_TYPE_ID);
		GetEstadosResponse response = docketApiService.getEstados();
		for (GetEstadosResponse.Estado estado : response.getEstados()) {
			if  (estado.getSigla().equals(value)) {
				return estado.getId();
			}
		}
		throw new StateNotFoundException(value);
	}
	
	private String getCidadeId(CreateProviderDocumentGroupInput createProviderDocumentGroupInput) {
		String value = createProviderDocumentGroupInput.getFields().get(FieldTypes.CIDADE_FIELD_TYPE_ID);
		String estadoId = getEstadoId(createProviderDocumentGroupInput);
		GetCidadesByEstadoResponse response = docketApiService.getCidadesByEstado(estadoId);
		for (GetCidadesByEstadoResponse.Cidade cidade : response.getCidades()) {
			if  (cidade.getNome().equals(value)) {
				return cidade.getId();
			}
		}
		throw new CityNotFoundException(value);
	}

	@Override
	public CreatePedidoRequest getCreatePedidoRequest(
			CreateProviderDocumentGroupInput createProviderDocumentGroupInput) {

		Map<String, String> fieldsFromRequest = createProviderDocumentGroupInput.getFields();
		Map<String, Object> fieldsToRequest = new LinkedHashMap<>();

		for (Map.Entry<String, String> entry : fieldsFromRequest.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			FieldType fieldType = fieldTypes.getFieldTypeById(key);

			switch (fieldType.getId()) {
			case FieldTypes.CPF_FIELD_TYPE_ID:
				fieldsToRequest.put("cpf", value);
				break;
			case FieldTypes.ESTADO_FIELD_TYPE_ID: {
				value = getEstadoId(createProviderDocumentGroupInput);
				fieldsToRequest.put("estado", value);
				break;
			}
			case FieldTypes.CIDADE_FIELD_TYPE_ID: {
				value = getCidadeId(createProviderDocumentGroupInput);
				fieldsToRequest.put("cidade", value);
				break;
			}
			case FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID:
				long dataNascimento = toEpochSeconds(value);
				fieldsToRequest.put("dataNascimento", dataNascimento);
				break;
			case FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID:
				fieldsToRequest.put("nomeCompleto", value);
				break;
			default:
				throw new IllegalStateException(String.format("O tipo %s não foi tratado", fieldType.getId()));
			}
		}
		CreatePedidoRequest.Pedido pedido = new Pedido();
		pedido.setCentroCustoId(CENTRO_CUSTO_ID);
		pedido.setGrupoId(GRUPO_ID);
		pedido.setLead(LEAD);
		pedido.setTipoOperacaoId(TIPO_OPERACAO_ID);

		for (String documentTypeId : createProviderDocumentGroupInput.getDocumentTypeIds()) {
			Documento prototype = documentosByDocumentTypeId.get(documentTypeId);
			Documento documento = clone(prototype);
			documento.setCampos(fieldsToRequest);
			pedido.getDocumentos().add(documento);
		}

		CreatePedidoRequest createPedidoRequest = new CreatePedidoRequest();
		createPedidoRequest.setPedido(pedido);

		return createPedidoRequest;
	}

	private Documento clone(Documento prototype) {
		Documento documento = new Documento();
		documento.setDocumentKitId(prototype.getDocumentKitId());
		documento.setProdutoId(prototype.getProdutoId());
		documento.setKitId(prototype.getKitId());
		documento.setKitNome(prototype.getKitNome());
		documento.setTitularTipo(prototype.getTitularTipo());
		documento.setDocumentoNome(prototype.getDocumentoNome());
		return documento;
	}

	private long toEpochSeconds(String date) {
		try {
			return DateUtils.fromScci(date).getEpochSecond();
		} catch (InvalidDateException e) {
			throw new InternalServerException(e);
		}
	}
}
