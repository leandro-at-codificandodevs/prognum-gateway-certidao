package br.com.prognum.gateway_certidao.docket_core.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentTypeNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import br.com.prognum.gateway_certidao.core.models.CreateDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.models.DocumentType;
import br.com.prognum.gateway_certidao.core.models.DocumentTypes;
import br.com.prognum.gateway_certidao.core.models.FieldType;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.core.services.StateService;
import br.com.prognum.gateway_certidao.core.services.StateServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.models.DocumentoMetadata;
import br.com.prognum.gateway_certidao.docket_core.models.DocumentoMetadatum;
import br.com.prognum.gateway_certidao.docket_core.services.DocumentoMetadataService;
import br.com.prognum.gateway_certidao.docket_core.services.DocumentoMetadataServiceImpl;

public class DocketDocumentosDumper {
	public static final void main(String [] args) throws DocumentTypeNotFoundException, ToJsonException {
		JsonService jsonService = new JsonServiceImpl();
		StateService stateService = new StateServiceImpl();
		DocumentTypes documentTypes = new DocumentTypes(stateService);
		DocumentoMetadataService service = new DocumentoMetadataServiceImpl();
		DocumentoMetadata metadata = service.getDocumentoMetadata();
		System.out.printf("ID Docket\tNome Docket\tID Gateway\tNome Gateway\tPayload\tEstados\n");
		for (DocumentoMetadatum metadatum : metadata.getDocumentos()) {
			String docketId = metadatum.getProdutoId();
			String docketName = metadatum.getDocumentoNome();
			String documentTypeId = metadatum.getDocumentTypeId();
			DocumentType documentType = documentTypes.getDocumentTypeById(documentTypeId);
			String documentTypeName = documentType.getName();
			Map<String, String> fields = new HashMap<String, String>();
			for (FieldType fieldType : documentType.getFieldTypes()) {
				fields.put(fieldType.getId(), "?");
			}
			CreateDocumentGroupInput input = new CreateDocumentGroupInput();
			input.setDocumentTypeIds(List.of(documentTypeId));
			input.setFields(fields);
			String payload = jsonService.toJson(input);
			List<String> stateAcronynms = documentType.getStateAcronymns();
			String states = stateAcronynms.stream().collect(Collectors.joining(", "));
			System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\n", docketId, docketName, documentTypeId, documentTypeName, payload, states);
		}
	}
}
