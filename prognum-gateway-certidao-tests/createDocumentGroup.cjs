const createDocumentGroup = async ({url, xApiKey, payload}) => {
    const init = {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "x-api-key": xApiKey,
        },
        body: JSON.stringify(payload),
    };
    const response = await fetch(url, init);
    const { status } = response;

    const text = await response.text();

    if (status !== 201) {
        return {
            status,
            payload: text,
        };
    }

    const responsePayload = JSON.parse(text);
    return {
        status,
        payload: responsePayload,
    };
}

module.exports = createDocumentGroup;
