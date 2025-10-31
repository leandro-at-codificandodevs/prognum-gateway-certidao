package br.com.prognum.gateway_certidao.iac;

import java.util.Map;

import lombok.Data;
import software.constructs.Construct;

@Data
public class Config {
	private String environment;
	private String system;
	private String logLevel;
	private String docketApiAuthUrl;
	private String docketApiCreatePedidoUrl;
	private String docketApiGetPedidoByIdUrl;
	private String docketApiGetPedidosUrl;
	private String docketApiDownloadArquivoUrl;
	private String docketApiGetEstadosUrl;
	private String docketApiGetCidadesByEstadoUrl;
	private String docketApiCentroCustoId;
	private String docketApiTipoOperacaoId;
	private String docketApiLead;
	private String docketApiGrupoId;

	public static Config create(Construct scope) {
		String environment = (String) scope.getNode().tryGetContext("environment");
		if (environment == null) {
			throw new IllegalArgumentException("Ambiente não foi passado no deploy");
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> envConfig = (Map<String, Object>) scope.getNode().tryGetContext(environment);
		if (envConfig == null) {
			throw new RuntimeException("Configuração não encontrada para ambiente: " + environment);
		}

		String system = (String) envConfig.get("system");
		String logLevel = (String) envConfig.get("logLevel");
		String docketApiAuthUrl = (String) envConfig.get("docketApiAuthUrl");
		String docketApiCreatePedidoUrl = (String) envConfig.get("docketApiCreatePedidoUrl");
		String docketApiGetPedidoByIdUrl = (String) envConfig.get("docketApiGetPedidoByIdUrl");
		String docketApiGetPedidosUrl = (String) envConfig.get("docketApiGetPedidosUrl");
		String docketApiDownloadArquivoUrl = (String) envConfig.get("docketApiDownloadArquivoUrl");
		String docketApiGetEstadosUrl = (String) envConfig.get("docketApiGetEstadosUrl");
		String docketApiGetCidadesByEstadoUrl = (String) envConfig.get("docketApiGetCidadesByEstadoUrl");
		String docketApiCentroCustoId = (String) envConfig.get("docketApiCentroCustoId");
		String docketApiTipoOperacaoId = (String) envConfig.get("docketApiTipoOperacaoId");
		String docketApiLead = (String) envConfig.get("docketApiLead");
		String docketApiGrupoId = (String) envConfig.get("docketApiGrupoId");

		Config config = new Config();
		config.setSystem(system);
		config.setLogLevel(logLevel);
		config.setDocketApiAuthUrl(docketApiAuthUrl);
		config.setDocketApiCreatePedidoUrl(docketApiCreatePedidoUrl);
		config.setDocketApiGetPedidoByIdUrl(docketApiGetPedidoByIdUrl);
		config.setDocketApiGetPedidosUrl(docketApiGetPedidosUrl);
		config.setDocketApiDownloadArquivoUrl(docketApiDownloadArquivoUrl);
		config.setDocketApiGetEstadosUrl(docketApiGetEstadosUrl);
		config.setDocketApiGetCidadesByEstadoUrl(docketApiGetCidadesByEstadoUrl);
		config.setDocketApiCentroCustoId(docketApiCentroCustoId);
		config.setDocketApiTipoOperacaoId(docketApiTipoOperacaoId);
		config.setDocketApiLead(docketApiLead);
		config.setDocketApiGrupoId(docketApiGrupoId);

		return config;
	}
}
