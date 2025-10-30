package br.com.prognum.gateway_certidao.docket_core.services;

import br.com.prognum.gateway_certidao.docket_core.models.DocketMetadata;

public interface DocketMetadataService {
	DocketMetadata read(String bucketName, String bucketObjectKey);
	void write(String bucketName, String bucketObjectKey, DocketMetadata metadata);
}
