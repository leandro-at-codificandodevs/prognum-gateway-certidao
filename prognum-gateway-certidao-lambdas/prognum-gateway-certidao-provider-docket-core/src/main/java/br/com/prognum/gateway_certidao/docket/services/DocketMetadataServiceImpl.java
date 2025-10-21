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
		return bucketService.readObject(DocketMetadata.class, bucketName, String.format("%s/%s", DOCKET_METADATA_FILE_NAME, bucketObjectKey));
	}

	@Override
	public void write(String bucketName, String bucketObjectKey, DocketMetadata metadata) {
		this.bucketService.writeObject(bucketName, String.format("%s/%s", DOCKET_METADATA_FILE_NAME, bucketObjectKey), metadata);
	}

}
