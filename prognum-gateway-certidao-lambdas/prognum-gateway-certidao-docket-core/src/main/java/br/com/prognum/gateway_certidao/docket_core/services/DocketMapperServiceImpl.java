package br.com.prognum.gateway_certidao.docket_core.services;

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
import br.com.prognum.gateway_certidao.docket_core.models.Cidade;
import br.com.prognum.gateway_certidao.docket_core.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket_core.models.DocumentoMetadata;
import br.com.prognum.gateway_certidao.docket_core.models.DocumentoMetadatum;
import br.com.prognum.gateway_certidao.docket_core.models.DocumentoRequest;
import br.com.prognum.gateway_certidao.docket_core.models.Estado;
import br.com.prognum.gateway_certidao.docket_core.models.GetCidadesByEstadoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetEstadosResponse;
import br.com.prognum.gateway_certidao.docket_core.models.PedidoRequest;

public class DocketMapperServiceImpl implements DocketMapperService {
    private String docketApiCentroCustoId;
    private String docketApiTipoOperacaoId;
    private String docketApiLead;
    private String docketApiGrupoId;
    
	private DocumentoMetadata documentoMetadata;

	private DocketApiService docketApiService;

	private FieldTypes fieldTypes;

	public DocketMapperServiceImpl(DocketApiService docketApiService, DocumentoMetadataService documentoMetadataService,
			String docketApiCentroCustoId, String docketApiTipoOperacaoId, String docketApiLead,
			String docketApiGrupoId) {
		this.docketApiService = docketApiService;

		this.docketApiCentroCustoId = docketApiCentroCustoId;
		this.docketApiTipoOperacaoId = docketApiTipoOperacaoId;
		this.docketApiLead = docketApiLead;
		this.docketApiGrupoId = docketApiGrupoId;

		this.documentoMetadata = documentoMetadataService.getDocumentoMetadata();

		
		fieldTypes = new FieldTypes();
	}

	private String getEstadoId(CreateProviderDocumentGroupInput createProviderDocumentGroupInput) {
		String value = createProviderDocumentGroupInput.getFields().get(FieldTypes.ESTADO_FIELD_TYPE_ID);
		GetEstadosResponse response = docketApiService.getEstados();
		for (Estado estado : response.getEstados()) {
			if (estado.getUf().equalsIgnoreCase(value)) {
				return estado.getId();
			}
		}
		throw new StateNotFoundException(value);
	}

	private String getCidadeId(CreateProviderDocumentGroupInput createProviderDocumentGroupInput) {
		String value = createProviderDocumentGroupInput.getFields().get(FieldTypes.CIDADE_FIELD_TYPE_ID);
		String estadoId = getEstadoId(createProviderDocumentGroupInput);
		GetCidadesByEstadoResponse response = docketApiService.getCidadesByEstado(estadoId);
		for (Cidade cidade : response.getCidades()) {
			if (cidade.getNome().equalsIgnoreCase(value)) {
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

			case FieldTypes.CNPJ_FIELD_TYPE_ID:
				fieldsToRequest.put("cnpj", value);
				break;
			case FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID:
				fieldsToRequest.put("razaoSocial", value);
				break;

			case FieldTypes.CEP_FIELD_TYPE_ID:
				fieldsToRequest.put("cep", value);
				break;
			case FieldTypes.COMARCA_FIELD_TYPE_ID:
				fieldsToRequest.put("comarca", value);
				break;
			case FieldTypes.CARTORIO_FIELD_TYPE_ID:
				fieldsToRequest.put("cartorio", value);
				break;
			case FieldTypes.NUMERO_MATRICULA_IMOVEL_FIELD_TYPE_ID:
				fieldsToRequest.put("numeroMatriculaImovel", value);
				break;
			case FieldTypes.NUMERO_INSCRICAO_IMOBILIARIA_FIELD_TYPE_ID:
				fieldsToRequest.put("numeroInscricaoImobiliaria", value);
				break;

			default:
				throw new IllegalStateException(String.format("O tipo %s não foi tratado", fieldType.getId()));
			}
		}

		PedidoRequest pedido = new PedidoRequest();
		pedido.setCentroCustoId(this.docketApiCentroCustoId);
		pedido.setGrupoId(this.docketApiGrupoId);
		pedido.setLead(this.docketApiLead);
		pedido.setTipoOperacaoId(this.docketApiTipoOperacaoId);

		for (String documentTypeId : createProviderDocumentGroupInput.getDocumentTypeIds()) {
			DocumentoMetadatum documentoMetadatum = documentoMetadata.getDocumentoByDocumentTypeId(documentTypeId);

			DocumentoRequest documento = new DocumentoRequest();
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
