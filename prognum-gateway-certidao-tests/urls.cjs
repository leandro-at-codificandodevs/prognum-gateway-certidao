const getCreateDocumentGroupUrl = (environment) => {
    switch (environment) {
        case "local":
            return "https://sjtlbk8se2.execute-api.us-east-2.amazonaws.com/prod/document-groups";
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getGetDocumentGroupByIdUrl = (environment, id) => {
    switch (environment) {
        case "local":
            return `https://sjtlbk8se2.execute-api.us-east-2.amazonaws.com/prod/document-groups/${id}`;
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
};

const getXApiKey = (environment) => {
    switch (environment) {
        case "local":
            return "1fpgj351k65sJgG9AWQ7C1A1pXWAHo1z58wGmst7";
        default:
            throw new Error(`Ambiente ${environment} desconhecido`);
    }
}

module.exports = {
    getCreateDocumentGroupUrl,
    getGetDocumentGroupByIdUrl,
    getXApiKey,
};
