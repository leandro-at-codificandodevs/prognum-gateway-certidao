const { ListObjectsCommand, S3Client } = require("@aws-sdk/client-s3");

// "arn:aws:s3:::prognum-certidao-dev-bucket"
const s3Client = new S3Client({});

const s3ListObjectKeys = async (bucketName, prefix, marker) => {
    const input = {
        Bucket: bucketName,
        Prefix: prefix,
        Marker: marker,
    };
    const command = new ListObjectsCommand(input);
    const output = await s3Client.send(command);
    return { data: output.Contents.map(content => content.Key), marker: output.Marker }
};

module.exports = s3ListObjectKeys;
