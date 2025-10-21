package br.com.prognum.gateway_certidao.core.services;

public interface BucketService {
	<V> V readObject(Class<V> type, String bucketName, String objectKey);

	<V> void writeObject(String bucketName, String objectKey, V value);
	
	void writeBytes(String bucketName, String objectKey, String contentType, byte[] bytes);

	String generatePresignedUrl(String bucketName, String objectKey);

	boolean verifyIfObjectExists(String bucketName, String objectKey);
}
