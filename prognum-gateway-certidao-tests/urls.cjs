const getCreateDocumentGroupUrl = (environment) => {
    switch (environment) {
        case "local":
            return "https://il3c1fp9di.execute-api.us-east-2.amazonaws.com/prod/document-groups";
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getGetDocumentGroupByIdUrl = (environment, id) => {
    switch (environment) {
        case "local":
            return `https://il3c1fp9di.execute-api.us-east-2.amazonaws.com/prod/document-groups/${id}`;
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getXApiKey = (environment) => {
    switch (environment) {
        case "local":
            return "h7uIo5ziGC5uOSEwTExKM5gc6lrefulkom61Bu10";
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
}

module.exports = {
    getCreateDocumentGroupUrl,
    getGetDocumentGroupByIdUrl,
    getXApiKey,
};
