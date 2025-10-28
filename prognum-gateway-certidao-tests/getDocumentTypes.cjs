const getDocumentTypes = async ({ url, xApiKey }) => {
  const init = {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "x-api-key": xApiKey,
    },
  };
  const response = await fetch(url, init);

  const { status } = response;

  const text = await response.text();

  if (status !== 200) {
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
};

module.exports = getDocumentTypes;