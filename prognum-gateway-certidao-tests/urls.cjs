const getCreateDocumentGroupUrl = (environment) => {
    switch (environment) {
        case "dev":
            return "https://gateway-certidao-dev.prognum.com.br/document-groups";
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getGetDocumentGroupByIdUrl = (environment, id) => {
    switch (environment) {
        case "dev":
            return `https://gateway-certidao-dev.prognum.com.br/document-groups/${id}`;
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getGetDocumentTypesUrl = (environment, id) => {
    switch (environment) {
        case "dev":
            return "https://gateway-certidao-dev.prognum.com.br/document-types";
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getXApiKey = (environment) => {
    switch (environment) {
        case "dev":
            return "54ADo9WMRM6QeO6FJ804YxbuZ8RoRrO1DkkeqRT0";
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
