package br.com.prognum.gateway_certidao.core.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class BucketServiceImpl implements BucketService {

	private S3Client s3Client;

	private JsonService jsonService;

	private S3Presigner s3Presigner;

	public BucketServiceImpl(S3Client s3Client, S3Presigner s3Presigner, JsonService jsonService) {
		super();
		this.s3Client = s3Client;
		this.jsonService = jsonService;
		this.s3Presigner = s3Presigner;
	}

	@Override
	public <V> V readObject(Class<V> type, String bucketName, String objectKey) {
		GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
		try (ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(request)) {
			String content = new String(responseInputStream.readAllBytes(), StandardCharsets.UTF_8);
			return jsonService.fromJson(content, type);
		} catch (FromJsonException | IOException e) {
			throw new InternalServerException(e);
		}
	}

	@Override
	public <V> void writeObject(String bucketName, String objectKey, V value) {
		String body;
		try {
			body = jsonService.toJson(value);
		} catch (ToJsonException e) {
			throw new InternalServerException(e);
		}
		PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(objectKey)
				.contentType("application/json").build();
		s3Client.putObject(request, RequestBody.fromString(body));
	}

	@Override
	public void writeBytes(String bucketName, String objectKey, String contentType, byte[] bytes) {
		PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(objectKey).contentType(contentType)
				.build();
		s3Client.putObject(request, RequestBody.fromBytes(bytes));
	}

	@Override
	public String generatePresignedUrl(String bucketName, String objectKey) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
		GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
				.signatureDuration(Duration.ofHours(24)).getObjectRequest(getObjectRequest).build();
		PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
		return presignedGetObjectRequest.url().toString();
	}

	@Override
	public boolean hasObject(String bucketName, String objectKey) {
		try {
			HeadObjectRequest headRequest = HeadObjectRequest.builder().bucket(bucketName).key(objectKey).build();
			s3Client.headObject(headRequest);
			return true;
		} catch (NoSuchKeyException e) {
			if (e.statusCode() == 404) {
				return false;
			} else {
				throw e;
			}
		}
	}
}
