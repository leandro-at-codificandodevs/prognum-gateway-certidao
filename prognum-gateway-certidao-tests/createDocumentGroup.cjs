//01 - criar, com sucesso, um grupo de documentos, com um único documento, dos tipos:
/*
public static final String DOCUMENT_TYPE_1 = "cert-executivos-fiscais-justica-estadual-1a-instancia-pf";
public static final String DOCUMENT_TYPE_2 = "cert-debitos-relativos-tributos-federais-divida-ativa-uniao-pf";
public static final String DOCUMENT_TYPE_3 = "cert-negativa-debitos-trabalhistas-cndt-pf";
public static final String DOCUMENT_TYPE_4 = "cert-executivos-fiscais-justica-estadual-1a-instancia-pf-f";
public static final String DOCUMENT_TYPE_5 = "cert-debitos-relativos-tributos-federais-divida-ativa-uniao-pf-f";
public static final String DOCUMENT_TYPE_6 = "cert-negativa-debitos-trabalhistas-cndt-pf-f";
*/     
//Resposta: 
//httpStatus 201
//payload{id, status} -> status = preparing

//02 - criar, com falha, com ausencia de fields, para cada um deles, para todos os tipos de documento
//Resposta: 
//httpStatus 400

//03 - criar, com falha, sem x-api-key
//Resposta: 
//httpStatus 401

//04 - criar, com falha, com x-api-key errado
//Resposta: 
//httpStatus 401


const createDocumentGroup = async ({url, xApiKey, payload}) => {
    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "x-api-key": xApiKey,
        },
        body: JSON.stringify(payload),
    });
    const { status } = response;

    const text = await response.text();
    let responsePayload = null;

    if (text && text.trim().length > 0) {
        try {
            responsePayload = JSON.parse(text);
        } catch (error) {
            console.error('Erro ao parsear JSON:', error);
            responsePayload = { error: 'Invalid JSON response', rawResponse: text };
        }
    }
    
    return {
        status,
        payload: responsePayload,
    };
}

module.exports = createDocumentGroup;
