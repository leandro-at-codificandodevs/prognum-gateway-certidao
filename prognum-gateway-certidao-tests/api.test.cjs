const createDocumentGroup = require("./createDocumentGroup.cjs");
const { getCreateDocumentGroupUrl, getXApiKey } = require("./urls.cjs");


describe("Solicitando grupo de documentos", () => {
  const createDocumentGroupUrl = getCreateDocumentGroupUrl("local");
  const validXApiKey = getXApiKey("local");
  const invalidXApiKey = "chave-invalida-123";

  const cpf = "79427201010";
  const estado = "RJ";
  const cidade = "Niterói";
  const dataNascimento = "2000-01-15";
  const nomeCompleto = "José da Silva";

  const documentTypesConfig = [
    {
      id: "cert-executivos-fiscais-justica-estadual-1a-instancia-pf",
      fields: {
        cpf,
        estado,
        cidade,
        "nome-completo": nomeCompleto,
      },
    },
    {
      id: "cert-debitos-relativos-tributos-federais-divida-ativa-uniao-pf",
      fields: {
        cpf,
        estado,
        cidade,
        "nome-completo": nomeCompleto,
        "data-nascimento": dataNascimento,
      },
    },
    {
      id: "cert-negativa-debitos-trabalhistas-cndt-pf",
      fields: {
        cpf,
        estado,
        cidade,
        "nome-completo": nomeCompleto,
      },
    },
    {
      id: "cert-executivos-fiscais-justica-estadual-1a-instancia-pf-f",
      fields: {
        cpf,
        estado,
        cidade,
        "nome-completo": nomeCompleto,
      },
    },
    {
      id: "cert-debitos-relativos-tributos-federais-divida-ativa-uniao-pf-f",
      fields: {
        cpf,
        estado,
        cidade,
        "nome-completo": nomeCompleto,
        "data-nascimento": dataNascimento,
      },
    },
    {
      id: "cert-negativa-debitos-trabalhistas-cndt-pf-f",
      fields: {
        cpf,
        estado,
        cidade,
        "nome-completo": nomeCompleto,
      },
    },
  ];

  describe.each(documentTypesConfig)(
    "Quando a execução é bem sucedida para o documento: $id",
    (docConfig) => {
      const payload = {
        "document-type-ids": [docConfig.id],
        fields: docConfig.fields,
      };

      it("Deve retornar 201 como status code", async () => {
        const request = {
          url: createDocumentGroupUrl,
          xApiKey: validXApiKey,
          payload,
        };
        const response = await createDocumentGroup(request);
        expect(response.status).toBe(201);
      });

      it("Deve retornar o payload esperado", async () => {
        const request = {
          url: createDocumentGroupUrl,
          xApiKey: validXApiKey,
          payload,
        };
        const response = await createDocumentGroup(request);
        expect(response.payload).toMatchObject({
          id: expect.any(String),
          status: "preparing",
        });
      });
    }
  );

  describe("Quando a execução é bem sucedida com TODOS os tipos de documentos", () => {
    const allDocumentTypeIds = documentTypesConfig.map(doc => doc.id);
    const allRequiredFields = {
      cpf: "79427201010",
      estado: "RJ",
      cidade: "Niterói",
      "data-nascimento": "2000-01-15",
      "nome-completo": "José da Silva",
    };

    const requestPayload = {
      "document-type-ids": allDocumentTypeIds,
      fields: allRequiredFields,
    };

    it("Deve retornar 201 como status code", async () => {
      const request = {
        url: createDocumentGroupUrl,
        xApiKey: validXApiKey,
        payload: requestPayload,
      };
      const response = await createDocumentGroup(request);
      expect(response.status).toBe(201);
    });

    it("Deve retornar o payload esperado", async () => {
      const request = {
        url: createDocumentGroupUrl,
        xApiKey: validXApiKey,
        payload: requestPayload,
      };
      const response = await createDocumentGroup(request);
      expect(response.payload).toMatchObject({
        id: expect.any(String),
        status: "preparing",
      });
    });
  });

  describe.each(documentTypesConfig)(
    "Ausência de Campos obrigatórios para o documento: $id",
    (docConfig) => {
      const requiredFields = Object.keys(docConfig.fields);

      test.each(requiredFields)(
        "Deve retornar 400 quando ausente o campo: %s",
        async (fieldToRemove) => {
          const fieldsWithMissingField = { ...docConfig.fields };
          delete fieldsWithMissingField[fieldToRemove];

          const requestPayload = {
            "document-type-ids": [docConfig.id],
            fields: fieldsWithMissingField,
          };

          const request = {
            url: createDocumentGroupUrl,
            xApiKey: validXApiKey,
            payload: requestPayload,
          };

          const response = await createDocumentGroup(request);
          expect(response.status).toBe(400);
        }
      );
    }
  );

  describe.each(documentTypesConfig)(
    "Campo obrigatório com valor null para o documento: $id",
    (docConfig) => {
      const requiredFields = Object.keys(docConfig.fields);

      test.each(requiredFields)(
        "Deve retornar 400 quando o campo %s tiver valor null",
        async (fieldWithNullValue) => {
          const fieldsWithNullValue = { ...docConfig.fields };
          fieldsWithNullValue[fieldWithNullValue] = null;

          const requestPayload = {
            "document-type-ids": [docConfig.id],
            fields: fieldsWithNullValue,
          };

          const request = {
            url: createDocumentGroupUrl,
            xApiKey: validXApiKey,
            payload: requestPayload,
          };

          const response = await createDocumentGroup(request);
          expect(response.status).toBe(400);
        }
      );
    }
  );

  describe.each(documentTypesConfig)(
    "Campo obrigatório com valor vazio para o documento: $id",
    (docConfig) => {
      const requiredFields = Object.keys(docConfig.fields);

      it.each(requiredFields)(
        "Deve retornar 400 quando o campo %s tiver valor vazio",
        async (fieldWithEmptyValue) => {
          const fieldsWithEmptyValue = { ...docConfig.fields };
          fieldsWithEmptyValue[fieldWithEmptyValue] = "";

          const requestPayload = {
            "document-type-ids": [docConfig.id],
            fields: fieldsWithEmptyValue,
          };

          const request = {
            url: createDocumentGroupUrl,
            xApiKey: validXApiKey,
            payload: requestPayload,
          };

          const response = await createDocumentGroup(request);
          expect(response.status).toBe(400);
        }
      );
    }
  );

  describe.each(documentTypesConfig)(
    "Campo desconhecido enviado para o documento: $id",
    (docConfig) => {
      it("Deve retornar 400 quando enviar campo desconhecido", async () => {
        const fieldsWithUnknownField = {
          ...docConfig.fields,
          "campo-desconhecido-xyz": "valor-qualquer",
        };

        const requestPayload = {
          "document-type-ids": [docConfig.id],
          fields: fieldsWithUnknownField,
        };

        const request = {
          url: createDocumentGroupUrl,
          xApiKey: validXApiKey,
          payload: requestPayload,
        };

        const response = await createDocumentGroup(request);
        expect(response.status).toBe(400);
      });
    }
  );

  describe.each(documentTypesConfig)(
    "Quando não há x-api-key para o documento: $id",
    (docConfig) => {
      it("Deve retornar 403", async () => {
        const requestPayload = {
          "document-type-ids": [docConfig.id],
          fields: docConfig.fields,
        };

        const request = {
          url: createDocumentGroupUrl,
          xApiKey: undefined,
          payload: requestPayload,
        };

        const response = await createDocumentGroup(request);
        expect(response.status).toBe(403);
      });
    }
  );

  describe.each(documentTypesConfig)(
    "Quando o x-api-key está incorreto para o documento: $id",
    (docConfig) => {
      it("Deve retornar 403", async () => {
        const requestPayload = {
          "document-type-ids": [docConfig.id],
          fields: docConfig.fields,
        };

        const request = {
          url: createDocumentGroupUrl,
          xApiKey: invalidXApiKey,
          payload: requestPayload,
        };

        const response = await createDocumentGroup(request);
        expect(response.status).toBe(403);
      });
    }
  );
});
