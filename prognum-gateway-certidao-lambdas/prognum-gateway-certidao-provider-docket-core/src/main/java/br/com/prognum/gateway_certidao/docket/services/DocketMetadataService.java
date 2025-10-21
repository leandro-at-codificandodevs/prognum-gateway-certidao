package br.com.prognum.gateway_certidao.docket.services;

import br.com.prognum.gateway_certidao.docket.models.DocketMetadata;

public interface DocketMetadataService {
	DocketMetadata read(String bucketName, String bucketObjectKey);
	void write(String bucketName, String bucketObjectKey, DocketMetadata metadata);
}
