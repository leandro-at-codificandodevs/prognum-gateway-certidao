const { colorize } = require("json-colorizer");
const createDocumentGroup = require("./createDocumentGroup.cjs");
const getDocumentGroupById = require("./getDocumentGroupById.cjs");
const getDocumentTypes = require("./getDocumentTypes.cjs");

const {
  getCreateDocumentGroupUrl,
  getGetDocumentGroupByIdUrl,
  getGetDocumentTypesUrl,
  getXApiKey,
} = require("./urls.cjs");

const environment = "dev";

const main = async () => {
  const getDocumentTypesUrl = getGetDocumentTypesUrl(environment);
  const createDocumentGroupUrl = getCreateDocumentGroupUrl(environment);

  const xApiKey = getXApiKey(environment);

  const getDocumentTypesRequest = {
    url: getDocumentTypesUrl,
    xApiKey,
  };
  const getDocumentTypesResponse = await getDocumentTypes(getDocumentTypesRequest);
  console.log(colorize(JSON.stringify(getDocumentTypesResponse, null, 2)));

  const cpf = "79427201010";
  const estado = "RJ";
  const cidade = "Rio de Janeiro";
  const nomeCompleto = "José da Silva";

  const payload = {
    "document-type-ids": [
      "cert-acoes-civeis-justica-federal-1a-instancia-pf",
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
  console.log(colorize(JSON.stringify(createDocumentGroupResponse, null, 2)));

  const id = createDocumentGroupResponse.payload.id;

 // const id = "b6e13b8d-c128-4252-9f5a-9d5af63f2b57";

  const getDocumentGroupByIdUrl = getGetDocumentGroupByIdUrl(environment, id);
  const getDocumentGroupByIdRequest = {
    url: getDocumentGroupByIdUrl,
    xApiKey,
  };
  const getDocumentGroupByIdResponse = await getDocumentGroupById(getDocumentGroupByIdRequest);
  console.log(colorize(JSON.stringify(getDocumentGroupByIdResponse, null, 2)));
};

main().then(() => console.log("OK")).catch((e) => console.error(e));

/*
pedido: 6d5edd1a-b8b4-4b3b-b10d-0c2179f142f1
documento: 70872d28-b5df-4239-87d4-ec47e79b9ae2
*/