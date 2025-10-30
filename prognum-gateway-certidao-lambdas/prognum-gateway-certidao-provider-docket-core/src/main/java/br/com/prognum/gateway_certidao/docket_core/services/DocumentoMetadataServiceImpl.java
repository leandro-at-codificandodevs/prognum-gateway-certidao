package br.com.prognum.gateway_certidao.docket_core.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.models.DocumentoMetadata;

public class DocumentoMetadataServiceImpl implements DocumentoMetadataService {

	private DocumentoMetadata documentoMetadata;
	
	public DocumentoMetadataServiceImpl() {
		JsonService jsonService = new JsonServiceImpl();
		try (InputStream inputStream = this.getClass().getResourceAsStream("/documentos.json");
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter)) {
			String line = bufferedReader.readLine();
			while (line != null) {
				printWriter.println(line);
				line = bufferedReader.readLine();
			}
			String content = stringWriter.toString();
			documentoMetadata = jsonService.fromJson(content, DocumentoMetadata.class);
		} catch (IOException | FromJsonException e) {
			throw new InternalServerException(e);
		}
	}

	@Override
	public DocumentoMetadata getDocumentoMetadata() {
		return documentoMetadata;
	}
}
