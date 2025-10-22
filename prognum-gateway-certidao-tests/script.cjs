const createDocumentGroup = require("./createDocumentGroup.cjs");
const getDocumentGroupById = require("./getDocumentGroupById.cjs");
const {
  getCreateDocumentGroupUrl,
  getGetDocumentGroupByIdUrl,
  getXApiKey,
} = require("./urls.cjs");

const environment = "local";

const main = async () => {
  const createDocumentGroupUrl = getCreateDocumentGroupUrl(environment);

  const xApiKey = getXApiKey(environment);

  const cpf = "79427201010";
  const estado = "RJ";
  const cidade = "Niterói";
  const nomeCompleto = "José da Silva";

  const payload = {
    "document-type-ids": [
      "cert-executivos-fiscais-justica-estadual-1a-instancia-pf",
    ],
    fields: {
      cpf,
      estado,
      cidade,
      "nome-completo": nomeCompleto,
    },
  };

  const createDocumentGroupRequest = {
    url: createDocumentGroupUrl,
    xApiKey,
    payload,
  };
  const createDocumentGroupResponse = await createDocumentGroup(createDocumentGroupRequest);
  console.log(createDocumentGroupResponse);

  const id = createDocumentGroupResponse.payload.id;

  const getDocumentGroupByIdUrl = getGetDocumentGroupByIdUrl(environment, id);
  const getDocumentGroupByIdRequest = {
    url: getDocumentGroupByIdUrl,
    xApiKey,
    payload,
  };
  const getDocumentGroupByIdResponse = await getDocumentGroupById(getDocumentGroupByIdRequest);
  console.log(getDocumentGroupByIdResponse);
};

main().then(() => console.log("OK")).catch((e) => console.error(e));
