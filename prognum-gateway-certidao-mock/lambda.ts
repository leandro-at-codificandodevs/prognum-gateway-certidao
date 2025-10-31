
import { LambdaFunctionURLHandler } from 'aws-lambda';


export interface APIGatewayProxyStructuredResultV2 {
    statusCode?: number | undefined;
    headers?:
        | {
            [header: string]: boolean | number | string;
        }
        | undefined;
    body?: string | undefined;
    isBase64Encoded?: boolean | undefined;
    cookies?: string[] | undefined;
}

const url = "https://sandbox-saas.docket.com.br/api/v2/prognum/shopping-documentos/alpha/pedidos";

/*

    public class GetPedidoStatusResponse {

	private Pedido pedido;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Pedido {
		private String id;
		private String status;
		private String lead;
		private String centroCustoId;
		private String urlWebHookEntregaDocumento;
		private String grupoId;
		private Integer idExibicao;
		private String dataCriacao;
		private String usuarioCriacaoId;
		private String usuarioCriacaoNome;
		private String tipoOperacaoId;
		private String grupoNome;
		private List<Documento> documentos;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Documento {
		private String documentKitId;
		private String produtoId;
		private String kitId;
		private String kitNome;
		private String titularTipo;
		private String documentoNome;
		private String id;
		private String status;
		private String progresso;
		private String dataEntrega;
		private List<Arquivo> arquivos;
		private Integer idExibicao;
		private String titularId;
		private Integer titularIdExibicao;
		private String pedidoId;
		private Map<String, Object> campos;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Arquivo {
		private String nome;
		private String tipo;
		private String id;
		private List<Link> links;
		private String dataCriacao;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Link {
		private String href;
		private String nome;
	}
}
    */
export const handler: LambdaFunctionURLHandler = (event, context) => {
    console.log(event);
    console.log(context);

    return {
        statusCode: 200;
        headers: [],
        body: {

        }
    }
};

