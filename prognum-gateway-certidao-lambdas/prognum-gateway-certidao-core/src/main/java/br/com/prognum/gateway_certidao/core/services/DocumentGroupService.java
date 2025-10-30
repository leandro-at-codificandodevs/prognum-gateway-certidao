package br.com.prognum.gateway_certidao.core.services;

import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentGroupNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.DocumentNotFoundException;
import br.com.prognum.gateway_certidao.core.models.DocumentGroup;
import br.com.prognum.gateway_certidao.core.models.DocumentGroupFailure;
import br.com.prognum.gateway_certidao.core.models.DocumentGroupMetadata;
import br.com.prognum.gateway_certidao.core.models.DocumentMetadata;

public interface DocumentGroupService {
	
	public static final String DOCUMENT_CONTENT_FILE_NAME = "content.pdf";
	public static final String DOCUMENT_METADATA_FILE_NAME = "metadata.json";
	public static final String DOCUMENT_FAILURE_FILE_NAME = "failure.json";

	DocumentGroup createDocumentGroup(String bucketName, List<String> documentTypeIds);

	DocumentGroup getDocumentGroupById(String bucketName, String documentGroupId) throws DocumentGroupNotFoundException;

	DocumentGroupMetadata getDocumentGroupMetadata(String bucketName, String documentGroupId)
			throws DocumentGroupNotFoundException;

	DocumentMetadata getDocumentMetadata(String bucketName, String documentGroupId, String documentId)
			throws DocumentNotFoundException;
	
	String getDocumentGroupObjectKey(String documentGroupId);

	String getDocumentObjectKey(String documentGroupId, String documentId);

	DocumentGroupFailure createDocumentGroupFailure(String bucketName, String documentGroupId);

	DocumentGroupFailure getDocumentGroupFailure(String bucketName, String documentGroupId);
}