package br.com.prognum.gateway_certidao.iac;

import java.util.Map;
import java.util.Objects;

import software.constructs.Construct;

public class Config {
	private String environment;
	private String system;
	private String logLevel;
	private String docketApiAuthUrl;
	private String docketApiCreatePedidoUrl;
	private String docketApiGetPedidoUrl;
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
		String docketApiGetPedidoUrl = (String) envConfig.get("docketApiGetPedidoUrl");
		String docketApiDownloadArquivoUrl = (String) envConfig.get("docketApiDownloadArquivoUrl");
		String docketApiGetEstadosUrl = (String) envConfig.get("docketApiGetEstadosUrl");
		String docketApiGetCidadesByEstadoUrl = (String) envConfig.get("docketApiGetCidadesByEstadoUrl");
		String docketApiCentroCustoId = (String) envConfig.get("docketApiCentroCustoId");
		String docketApiTipoOperacaoId = (String) envConfig.get("docketApiTipoOperacaoId");
		String docketApiLead = (String) envConfig.get("docketApiLead");
		String docketApiGrupoId = (String) envConfig.get("docketApiGrupoId");

		Config config = new Config(environment, system, logLevel, docketApiAuthUrl, docketApiCreatePedidoUrl,
				docketApiGetPedidoUrl, docketApiDownloadArquivoUrl, docketApiGetEstadosUrl,
				docketApiGetCidadesByEstadoUrl, docketApiCentroCustoId, docketApiTipoOperacaoId, docketApiLead,
				docketApiGrupoId);

		return config;
	}

	public Config(String environment, String system, String logLevel, String docketApiAuthUrl,
			String docketApiCreatePedidoUrl, String docketApiGetPedidoUrl, String docketApiDownloadArquivoUrl,
			String docketApiGetEstadosUrl, String docketApiGetCidadesByEstadoUrl, String docketApiCentroCustoId,
			String docketApiTipoOperacaoId, String docketApiLead, String docketApiGrupoId) {
		super();
		this.environment = environment;
		this.system = system;
		this.logLevel = logLevel;
		this.docketApiAuthUrl = docketApiAuthUrl;
		this.docketApiCreatePedidoUrl = docketApiCreatePedidoUrl;
		this.docketApiGetPedidoUrl = docketApiGetPedidoUrl;
		this.docketApiDownloadArquivoUrl = docketApiDownloadArquivoUrl;
		this.docketApiGetEstadosUrl = docketApiGetEstadosUrl;
		this.docketApiGetCidadesByEstadoUrl = docketApiGetCidadesByEstadoUrl;
		this.docketApiCentroCustoId = docketApiCentroCustoId;
		this.docketApiTipoOperacaoId = docketApiTipoOperacaoId;
		this.docketApiLead = docketApiLead;
		this.docketApiGrupoId = docketApiGrupoId;
	}

	public String getEnvironment() {
		return environment;
	}

	public String getSystem() {
		return system;
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

	public String getDocketApiDownloadArquivoUrl() {
		return docketApiDownloadArquivoUrl;
	}

	public String getDocketApiGetEstadosUrl() {
		return docketApiGetEstadosUrl;
	}

	public String getDocketApiGetCidadesByEstadoUrl() {
		return docketApiGetCidadesByEstadoUrl;
	}

	public String getDocketApiCentroCustoId() {
		return docketApiCentroCustoId;
	}

	public String getDocketApiTipoOperacaoId() {
		return docketApiTipoOperacaoId;
	}

	public String getDocketApiLead() {
		return docketApiLead;
	}

	public String getDocketApiGrupoId() {
		return docketApiGrupoId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(docketApiAuthUrl, docketApiCentroCustoId, docketApiCreatePedidoUrl,
				docketApiDownloadArquivoUrl, docketApiGetCidadesByEstadoUrl, docketApiGetEstadosUrl,
				docketApiGetPedidoUrl, docketApiGrupoId, docketApiLead, docketApiTipoOperacaoId, environment, logLevel,
				system);
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
				&& Objects.equals(docketApiCentroCustoId, other.docketApiCentroCustoId)
				&& Objects.equals(docketApiCreatePedidoUrl, other.docketApiCreatePedidoUrl)
				&& Objects.equals(docketApiDownloadArquivoUrl, other.docketApiDownloadArquivoUrl)
				&& Objects.equals(docketApiGetCidadesByEstadoUrl, other.docketApiGetCidadesByEstadoUrl)
				&& Objects.equals(docketApiGetEstadosUrl, other.docketApiGetEstadosUrl)
				&& Objects.equals(docketApiGetPedidoUrl, other.docketApiGetPedidoUrl)
				&& Objects.equals(docketApiGrupoId, other.docketApiGrupoId)
				&& Objects.equals(docketApiLead, other.docketApiLead)
				&& Objects.equals(docketApiTipoOperacaoId, other.docketApiTipoOperacaoId)
				&& Objects.equals(environment, other.environment) && Objects.equals(logLevel, other.logLevel)
				&& Objects.equals(system, other.system);
	}

	@Override
	public String toString() {
		return "Config [environment=" + environment + ", system=" + system + ", logLevel="
				+ logLevel + ", docketApiAuthUrl=" + docketApiAuthUrl + ", docketApiCreatePedidoUrl="
				+ docketApiCreatePedidoUrl + ", docketApiGetPedidoUrl=" + docketApiGetPedidoUrl
				+ ", docketApiDownloadArquivoUrl=" + docketApiDownloadArquivoUrl + ", docketApiGetEstadosUrl="
				+ docketApiGetEstadosUrl + ", docketApiGetCidadesByEstadoUrl=" + docketApiGetCidadesByEstadoUrl
				+ ", docketApiCentroCustoId=" + docketApiCentroCustoId + ", docketApiTipoOperacaoId="
				+ docketApiTipoOperacaoId + ", docketApiLead=" + docketApiLead + ", docketApiGrupoId="
				+ docketApiGrupoId + "]";
	}
}
