package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import lombok.Data;

@Data
public class DocumentoMetadata {

	private List<DocumentoMetadatum> documentos;

	public DocumentoMetadatum getDocumentoByDocumentTypeId(String documentTypeId) {
		return documentos.stream().filter(documento -> documento.getDocumentTypeId().equals(documentTypeId)).findAny().orElseThrow(
			() -> new InternalServerException(String.format("Documento Docket não encontrado para tipo de documento %s",  documentTypeId)));
	}
}
