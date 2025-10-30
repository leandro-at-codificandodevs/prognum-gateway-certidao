package br.com.prognum.gateway_certidao.docket.services;

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


	public DocketMapperServiceImpl(DocketApiService docketApiService, DocumentoMetadataService documentoMetadataService) {
		this.docketApiService = docketApiService;
		
		this.documentoMetadata = documentoMetadataService.getDocumentoMetadata();

		fieldTypes = new FieldTypes();
	}

	private String getEstadoId(CreateProviderDocumentGroupInput createProviderDocumentGroupInput) {
		String value = createProviderDocumentGroupInput.getFields().get(FieldTypes.ESTADO_FIELD_TYPE_ID);
		GetEstadosResponse response = docketApiService.getEstados();
		for (GetEstadosResponse.Estado estado : response.getEstados()) {
			if  (estado.getUf().equalsIgnoreCase(value)) {
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
			if  (cidade.getNome().equalsIgnoreCase(value)) {
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
			case FieldTypes.ESTADO_FIELD_TYPE_ID:
				fieldsToRequest.put("estado", getEstadoId(createProviderDocumentGroupInput));
				break;
			case FieldTypes.CIDADE_FIELD_TYPE_ID:
				fieldsToRequest.put("cidade", getCidadeId(createProviderDocumentGroupInput));
				break;
				
			case FieldTypes.RG_FIELD_TYPE_ID:
				fieldsToRequest.put("rg", value);
				break;
			case FieldTypes.CPF_FIELD_TYPE_ID:
				fieldsToRequest.put("cpf", value);
				break;
			case FieldTypes.CEI_FIELD_TYPE_ID:
				fieldsToRequest.put("cei", value);
				break;
			case FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID:
				fieldsToRequest.put("nomeCompleto", value);
				break;
			case FieldTypes.NOME_MAE_FIELD_TYPE_ID:
				fieldsToRequest.put("nomeMae", value);
				break;
			case FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID:
				fieldsToRequest.put("dataNascimento", toEpochSeconds(value));
				break;
			case FieldTypes.CARTORIO_FIELD_TYPE_ID:
				fieldsToRequest.put("cartorio", value);
				break;

			case FieldTypes.CNPJ_FIELD_TYPE_ID:
				fieldsToRequest.put("cnpj", value);
				break;
			case FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID:
				fieldsToRequest.put("razaoSocial", value);
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
			DocumentoMetadatum documentoMetadatum = documentoMetadata.getDocumentoByDocumentTypeId(documentTypeId);

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
