package br.com.prognum.gateway_certidao.docket.services;

import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_1;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_2;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_3;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_4;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_5;
import static br.com.prognum.gateway_certidao.core.models.DocumentTypes.DOCUMENT_TYPE_6;
import static br.com.prognum.gateway_certidao.core.models.States.BRASILIA_CITY_ID;
import static br.com.prognum.gateway_certidao.core.models.States.CURITIBA_CITY_ID;
import static br.com.prognum.gateway_certidao.core.models.States.DF_STATE_ID;
import static br.com.prognum.gateway_certidao.core.models.States.NITEROI_CITY_ID;
import static br.com.prognum.gateway_certidao.core.models.States.PR_STATE_ID;
import static br.com.prognum.gateway_certidao.core.models.States.RIO_DE_JANEIRO_CITY_ID;
import static br.com.prognum.gateway_certidao.core.models.States.RJ_STATE_ID;
import static br.com.prognum.gateway_certidao.core.models.States.SAO_PAULO_CITY_ID;
import static br.com.prognum.gateway_certidao.core.models.States.SP_STATE_ID;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.models.City;
import br.com.prognum.gateway_certidao.core.models.CreateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.models.FieldType;
import br.com.prognum.gateway_certidao.core.models.FieldTypes;
import br.com.prognum.gateway_certidao.core.models.State;
import br.com.prognum.gateway_certidao.core.models.States;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest.Documento;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest.Pedido;

public class DocketMapperServiceImpl implements DocketMapperService {

	private String centroCustoId = "feec1170-1f20-49b8-bd39-91315961e4bb";
	private String tipoOperacaoId = "95e04d49-1926-498b-a68d-18273fe1287c";
	private String lead = "Operacao de credito - API - Docket - Prognum";
	private String grupoId = "ed892d79-7f0e-4248-98ef-a32382b0ab13";

	private static final States states = new States();

	private static final FieldTypes fieldTypes = new FieldTypes();

	private final Map<String, Documento> documentosByDocumentTypeId;
	private final Map<String, String> estadosByStateId;
	private final Map<String, String> cidadesByCityId;

	public DocketMapperServiceImpl() {

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

		estadosByStateId = new HashMap<>();
		cidadesByCityId = new HashMap<>();

		estadosByStateId.put(RJ_STATE_ID, "074c0821-bf46-c6b6-5f7c-e39f5df39f98");
		cidadesByCityId.put(NITEROI_CITY_ID, "c6f93c94-fdfa-ba2b-7ab1-d78c6d773804");
		cidadesByCityId.put(RIO_DE_JANEIRO_CITY_ID, "34e92f60-8e63-31c8-2b9b-e459471cbb6a");

		estadosByStateId.put(SP_STATE_ID, "52f0da38-2fb5-4a87-22ef-32670b94d916");
		cidadesByCityId.put(SAO_PAULO_CITY_ID, "41500f13-56d8-8690-35f2-7af0fa911611");

		estadosByStateId.put(DF_STATE_ID, "607abcf9-d7a6-8b26-96c6-13cf0301f3de");
		cidadesByCityId.put(BRASILIA_CITY_ID, "6dab9712-b4c4-79f4-854f-edacd47d99fd");

		estadosByStateId.put(PR_STATE_ID, "e0992d76-53fb-4933-cec1-eb51b79d63bb");
		cidadesByCityId.put(CURITIBA_CITY_ID, "44aded0e-cb9d-a3d4-8687-6d9126965da9");
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
				State state = states.getStateByIdOrAcronymnOrName(value);
				value = estadosByStateId.get(state.getId());
				fieldsToRequest.put("estado", value);
				break;
			}
			case FieldTypes.CIDADE_FIELD_TYPE_ID: {
				City city = states.getCityByIdOrName(value);
				value = cidadesByCityId.get(city.getId());
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
		pedido.setCentroCustoId(centroCustoId);
		pedido.setGrupoId(grupoId);
		pedido.setLead(lead);
		pedido.setTipoOperacaoId(tipoOperacaoId);

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
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return Instant.ofEpochMilli(fmt.parse(date).getTime()).getEpochSecond();
		} catch (ParseException e) {
			throw new InternalServerException(e);
		}
	}
}
