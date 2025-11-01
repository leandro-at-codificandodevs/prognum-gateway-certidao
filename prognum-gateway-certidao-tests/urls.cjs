//const baseUrl = "https://5som14qz8b.execute-api.sa-east-1.amazonaws.com/prod";
const baseUrl = "https://gateway-certidao-dev.prognum.com.br";

const getCreateDocumentGroupUrl = (environment) => {
    switch (environment) {
        case "dev":
            return `${baseUrl}/document-groups`;
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getGetDocumentGroupByIdUrl = (environment, id) => {
    switch (environment) {
        case "dev":
            return `${baseUrl}/document-groups/${id}`;
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getGetDocumentTypesUrl = (environment, id) => {
    switch (environment) {
        case "dev":
            return `${baseUrl}/document-types`;
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getXApiKey = (environment) => {
    switch (environment) {
        case "dev":
            return "ufH3fkY5ah9knfaqJgQao6sYALYGvVbq3nRtfVKx";
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
}

module.exports = {
    getCreateDocumentGroupUrl,
    getGetDocumentGroupByIdUrl,
    getGetDocumentTypesUrl,
    getXApiKey,
};
