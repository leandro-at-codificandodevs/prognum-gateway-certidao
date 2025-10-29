package br.com.prognum.gateway_certidao.core.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentGroupNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.DocumentNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.models.Document;
import br.com.prognum.gateway_certidao.core.models.DocumentGroup;
import br.com.prognum.gateway_certidao.core.models.DocumentGroupMetadata;
import br.com.prognum.gateway_certidao.core.models.DocumentGroupStatus;
import br.com.prognum.gateway_certidao.core.models.DocumentMetadata;
import br.com.prognum.gateway_certidao.core.models.DocumentStatus;

public class DocumentGroupServiceImpl implements DocumentGroupService {

	private BucketService bucketService;
	
	private ULIDService ulidService;

	public DocumentGroupServiceImpl(BucketService bucketService, ULIDService ulidService) {
		this.bucketService = bucketService;
		this.ulidService = ulidService;
	}

	@Override
	public DocumentGroup createDocumentGroup(String bucketName, List<String> documentTypeIds) {
		String documentGroupId = ulidService.next();

		Instant now = Instant.now();

		DocumentGroup documentGroup = new DocumentGroup();
		documentGroup.setId(documentGroupId);
		documentGroup.setTimestamp(now);
		documentGroup.setDocuments(new LinkedList<>());
		documentGroup.setStatus(DocumentGroupStatus.PREPARING);

		DocumentGroupMetadata documentGroupMetadata = new DocumentGroupMetadata();
		documentGroupMetadata.setDocumentGroupId(documentGroupId);
		documentGroupMetadata.setTimestamp(now);
		documentGroupMetadata.setDocumentIds(new HashSet<>());
		String documentGroupMetadataObjectKey = getDocumentGroupMetadataObjectKey(documentGroupId);

		for (String documentTypeId : documentTypeIds) {
			Document document = new Document();
			String documentId = ulidService.next();
			document.setId(documentId);
			document.setTypeId(documentTypeId);
			document.setTimestamp(now);
			document.setStatus(DocumentStatus.PREPARING);
			documentGroup.getDocuments().add(document);

			DocumentMetadata documentMetadata = new DocumentMetadata();
			documentMetadata.setDocumentId(documentId);
			documentMetadata.setDocumentTypeId(documentTypeId);
			documentMetadata.setTimestamp(Instant.now());
			documentGroupMetadata.getDocumentIds().add(documentId);

			String documentMetadataObjectKey = getDocumentMetadataObjectKey(documentGroupId, documentId);

			bucketService.writeObject(bucketName, documentMetadataObjectKey, documentMetadata);
		}

		bucketService.writeObject(bucketName, documentGroupMetadataObjectKey, documentGroupMetadata);

		return documentGroup;
	}

	@Override
	public DocumentGroup getDocumentGroupById(String bucketName, String documentGroupId)
			throws DocumentGroupNotFoundException {
		DocumentGroupMetadata metadata = getDocumentGroupMetadata(bucketName, documentGroupId);

		DocumentGroup documentGroup = new DocumentGroup();
		documentGroup.setId(metadata.getDocumentGroupId());
		documentGroup.setTimestamp(metadata.getTimestamp());
		documentGroup.setDocuments(new ArrayList<>());

		DocumentGroupStatus status = DocumentGroupStatus.READY;

		for (String documentId : metadata.getDocumentIds()) {
			DocumentMetadata documentMetadata;
			try {
				documentMetadata = getDocumentMetadata(bucketName, documentGroupId, documentId);
			} catch (DocumentNotFoundException e) {
				throw new InternalServerException(e);
			}

			Document document = new Document();
			document.setId(documentId);
			document.setTypeId(documentMetadata.getDocumentTypeId());
			document.setTimestamp(documentMetadata.getTimestamp());

			String contentKey = getDocumentContentObjectKey(documentGroupId, documentId);

			boolean fileExists = bucketService.verifyIfObjectExists(bucketName, contentKey);

			if (fileExists) {
				document.setStatus(DocumentStatus.READY);

				String downloadUrl = bucketService.generatePresignedUrl(bucketName, contentKey);
				document.setDownloadUrl(downloadUrl);
			} else {
				document.setStatus(DocumentStatus.PREPARING);

				status = DocumentGroupStatus.PREPARING;
			}
			documentGroup.getDocuments().add(document);
		}

		documentGroup.setStatus(status);
		return documentGroup;
	}

	@Override
	public DocumentGroupMetadata getDocumentGroupMetadata(String bucketName, String documentGroupId)
			throws DocumentGroupNotFoundException {
		String metadataKey = getDocumentGroupMetadataObjectKey(documentGroupId);

		if (!bucketService.verifyIfObjectExists(bucketName, metadataKey)) {
			throw new DocumentGroupNotFoundException(documentGroupId);
		}

		return bucketService.readObject(DocumentGroupMetadata.class, bucketName, metadataKey);
	}

	@Override
	public DocumentMetadata getDocumentMetadata(String bucketName, String documentGroupId, String documentId)
			throws DocumentNotFoundException {
		String metadataKey = getDocumentMetadataObjectKey(documentGroupId, documentId);

		if (!bucketService.verifyIfObjectExists(bucketName, metadataKey)) {
			throw new DocumentNotFoundException(documentGroupId);
		}

		return bucketService.readObject(DocumentMetadata.class, bucketName, metadataKey);
	}

	public String getDocumentGroupObjectKey(String documentGroupId) {
		return String.format("groups/%s", documentGroupId);
	}

	public String getDocumentObjectKey(String documentGroupId, String documentId) {
		return String.format("groups/%s/documents/%s", documentGroupId, documentId);
	}

	private String getDocumentGroupMetadataObjectKey(String documentGroupId) {
		return String.format("%s/%s", getDocumentGroupObjectKey(documentGroupId), DOCUMENT_METADATA_FILE_NAME);
	}

	private String getDocumentMetadataObjectKey(String documentGroupId, String documentId) {
		return String.format("%s/%s", getDocumentObjectKey(documentGroupId, documentId), DOCUMENT_METADATA_FILE_NAME);
	}

	private String getDocumentContentObjectKey(String documentGroupId, String documentId) {
		return String.format("%s/%s", getDocumentObjectKey(documentGroupId, documentId), DOCUMENT_CONTENT_FILE_NAME);
	}
}