package br.com.prognum.gateway_certidao.docket.services;

import br.com.prognum.gateway_certidao.core.services.BucketService;
import br.com.prognum.gateway_certidao.docket.models.DocketMetadata;

public class DocketMetadataServiceImpl implements DocketMetadataService {

	private static final String DOCKET_METADATA_FILE_NAME = "docket.json";
	
	private BucketService bucketService;
	
	public DocketMetadataServiceImpl(BucketService bucketService) {
		this.bucketService = bucketService;
	}
	
	@Override
	public DocketMetadata read(String bucketName, String bucketObjectKey) {
		return bucketService.readObject(DocketMetadata.class, bucketName, getDocketMetadataBucketObjectKey(bucketObjectKey));
	}

	@Override
	public void write(String bucketName, String bucketObjectKey, DocketMetadata metadata) {
		this.bucketService.writeObject(bucketName, getDocketMetadataBucketObjectKey(bucketObjectKey), metadata);
	}

	private String getDocketMetadataBucketObjectKey(String bucketObjectKey) {
		return String.format("%s/%s", bucketObjectKey, DOCKET_METADATA_FILE_NAME);
	}
}
