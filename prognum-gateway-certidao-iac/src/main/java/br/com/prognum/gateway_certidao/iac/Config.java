package br.com.prognum.gateway_certidao.iac;

import java.util.Map;
import java.util.Objects;

import software.constructs.Construct;

public class Config {
	private String environment;
	private String system;
	private String tenantId;
	private String logLevel;
	private String docketApiAuthUrl;
	private String docketApiCreatePedidoUrl;
	private String docketApiGetPedidoUrl;
	private String docketApiDownloadDocumentoUrl;

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
		String tenantId = (String) envConfig.get("tenantId");
		String logLevel = (String) envConfig.get("logLevel");
		String docketApiAuthUrl = (String) envConfig.get("docketApiAuthUrl");
		String docketApiCreatePedidoUrl = (String) envConfig.get("docketApiCreatePedidoUrl");
		String docketApiGetPedidoUrl = (String) envConfig.get("docketApiGetPedidoUrl");
		String docketApiDownloadDocumentoUrl = (String) envConfig.get("docketApiDownloadDocumentoUrl");

		Config config = new Config(environment, system, tenantId, logLevel, docketApiAuthUrl, docketApiCreatePedidoUrl,
				docketApiGetPedidoUrl, docketApiDownloadDocumentoUrl);

		return config;
	}

	public Config(String environment, String system, String tenantId, String logLevel, String docketApiAuthUrl,
			String docketApiCreatePedidoUrl, String docketApiGetPedidoUrl, String docketApiDownloadDocumentoUrl) {
		super();
		this.environment = environment;
		this.system = system;
		this.tenantId = tenantId;
		this.logLevel = logLevel;
		this.docketApiAuthUrl = docketApiAuthUrl;
		this.docketApiCreatePedidoUrl = docketApiCreatePedidoUrl;
		this.docketApiGetPedidoUrl = docketApiGetPedidoUrl;
		this.docketApiDownloadDocumentoUrl = docketApiDownloadDocumentoUrl;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getSystem() {
		return system;
	}

	public String getTenantId() {
		return tenantId;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public String getDocketApiAuthUrl() {
		return docketApiAuthUrl;
	}

	public String getDocketApiCreatePedidoUrl() {
		return docketApiCreatePedidoUrl;
	}

	public String getDocketApiGetPedidoUrl() {
		return docketApiGetPedidoUrl;
	}

	public String getDocketApiDownloadDocumentoUrl() {
		return docketApiDownloadDocumentoUrl;
	}

	@Override
	public int hashCode() {
		return Objects.hash(docketApiAuthUrl, docketApiCreatePedidoUrl, docketApiDownloadDocumentoUrl,
				docketApiGetPedidoUrl, environment, logLevel, system, tenantId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Config other = (Config) obj;
		return Objects.equals(docketApiAuthUrl, other.docketApiAuthUrl)
				&& Objects.equals(docketApiCreatePedidoUrl, other.docketApiCreatePedidoUrl)
				&& Objects.equals(docketApiDownloadDocumentoUrl, other.docketApiDownloadDocumentoUrl)
				&& Objects.equals(docketApiGetPedidoUrl, other.docketApiGetPedidoUrl)
				&& Objects.equals(environment, other.environment) && Objects.equals(logLevel, other.logLevel)
				&& Objects.equals(system, other.system) && Objects.equals(tenantId, other.tenantId);
	}

}
