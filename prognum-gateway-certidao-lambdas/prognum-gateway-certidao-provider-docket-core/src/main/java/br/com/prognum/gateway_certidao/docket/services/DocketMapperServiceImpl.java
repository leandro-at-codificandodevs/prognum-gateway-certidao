package br.com.prognum.gateway_certidao.docket.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.prognum.gateway_certidao.core.exceptions.CityNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.InvalidDateException;
import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;
import br.com.prognum.gateway_certidao.core.models.CreateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.models.DocumentTypes;
import br.com.prognum.gateway_certidao.core.models.FieldType;
import br.com.prognum.gateway_certidao.core.models.FieldTypes;
import br.com.prognum.gateway_certidao.core.utils.DateUtils;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest.Documento;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest.Pedido;
import br.com.prognum.gateway_certidao.docket.models.DocumentoMetadata;
import br.com.prognum.gateway_certidao.docket.models.DocumentoMetadatum;
import br.com.prognum.gateway_certidao.docket.models.GetCidadesByEstadoResponse;
import br.com.prognum.gateway_certidao.docket.models.GetEstadosResponse;

public class DocketMapperServiceImpl implements DocketMapperService {

	private static final String CENTRO_CUSTO_ID = "feec1170-1f20-49b8-bd39-91315961e4bb";
	private static final String TIPO_OPERACAO_ID = "95e04d49-1926-498b-a68d-18273fe1287c";
	private static final String LEAD = "Operacao de credito - API - Docket - Prognum";
	private static final String GRUPO_ID = "ed892d79-7f0e-4248-98ef-a32382b0ab13";
	
	private DocumentoMetadata documentoMetadata;

	private DocketApiService docketApiService;

	private FieldTypes fieldTypes;

	private List<String> documentTypeIds;


	public DocketMapperServiceImpl(DocketApiService docketApiService, DocumentoMetadataService documentoMetadataService) {
		this.docketApiService = docketApiService;
		
		this.documentoMetadata = documentoMetadataService.getDocumentoMetadata();

		fieldTypes = new FieldTypes();

		documentTypeIds = List.of(DocumentTypes.DOCUMENT_TYPE_ID_1, DocumentTypes.DOCUMENT_TYPE_ID_2, DocumentTypes.DOCUMENT_TYPE_ID_3, DocumentTypes.DOCUMENT_TYPE_ID_4);
	}

	private String getEstadoId(CreateProviderDocumentGroupInput createProviderDocumentGroupInput) {
		String value = createProviderDocumentGroupInput.getFields().get(FieldTypes.ESTADO_FIELD_TYPE_ID);
		GetEstadosResponse response = docketApiService.getEstados();
		for (GetEstadosResponse.Estado estado : response.getEstados()) {
			if  (estado.getUf().equals(value)) {
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
			int index = documentTypeIds.indexOf(documentTypeId);
			DocumentoMetadatum documentoMetadatum = documentoMetadata.getDocuments().get(index);

			Documento documento = new Documento();
			documento.setDocumentKitId(documentoMetadatum.getDocumentKitId());
			documento.setProdutoId(documentoMetadatum.getProdutoId());
			documento.setKitId(documentoMetadatum.getKitId());
			documento.setKitNome(documentoMetadatum.getKitNome());
			documento.setTitularTipo(documentoMetadatum.getTitularTipo());
			documento.setDocumentoNome(documentoMetadatum.getDocumentoNome());
			documento.setCampos(fieldsToRequest);
			pedido.getDocumentos().add(documento);
		}

		CreatePedidoRequest createPedidoRequest = new CreatePedidoRequest();
		createPedidoRequest.setPedido(pedido);

		return createPedidoRequest;
	}

	private long toEpochSeconds(String date) {
		try {
			return DateUtils.fromScci(date).getEpochSecond();
		} catch (InvalidDateException e) {
			throw new InternalServerException(e);
		}
	}
}
