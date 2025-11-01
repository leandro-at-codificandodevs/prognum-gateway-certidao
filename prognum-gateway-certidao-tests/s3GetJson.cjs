const { GetObjectCommand, S3Client } = require("@aws-sdk/client-s3");

// "arn:aws:s3:::prognum-certidao-dev-bucket"
const s3Client = new S3Client({});

const s3GetJson = async (bucketName, objectKey) => {
    const input = {
        Bucket: bucketName,
        Key: objectKey,
    };
    const command = new GetObjectCommand(input);
    const output = await s3Client.send(command);
    const body = await output.Body.transformToString();
    return { data: JSON.parse(body) };
};
module.exports = s3GetJson;
