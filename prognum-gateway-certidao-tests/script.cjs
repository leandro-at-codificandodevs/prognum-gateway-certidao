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
  console.log(colorize(JSON.stringify(createDocumentGroupResponse, null, 2)));

  const id = createDocumentGroupResponse.payload.id;

  // const id = "b04822ce-ab72-45f9-891b-c871d9129de8";

  const getDocumentGroupByIdUrl = getGetDocumentGroupByIdUrl(environment, id);
  const getDocumentGroupByIdRequest = {
    url: getDocumentGroupByIdUrl,
    xApiKey,
  };
  const getDocumentGroupByIdResponse = await getDocumentGroupById(getDocumentGroupByIdRequest);
  console.log(colorize(JSON.stringify(getDocumentGroupByIdResponse, null, 2)));
};

main().then(() => console.log("OK")).catch((e) => console.error(e));
