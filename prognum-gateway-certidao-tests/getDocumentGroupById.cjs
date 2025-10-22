const getDocumentGroupById = async ({ url, xApiKey }) => {
  const response = await fetch(url, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "x-api-key": xApiKey,
    },
  });

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

module.exports = getDocumentGroupById;
