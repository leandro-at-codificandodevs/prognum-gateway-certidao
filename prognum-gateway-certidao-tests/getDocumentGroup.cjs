const getDocumentGroup = async ({url, xApiKey}) => {
    return response = await fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "x-api-key": xApiKey,
        },
    });
}

module.exports = getDocumentGroup;