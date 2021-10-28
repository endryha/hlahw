package com.endryha.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.endryha.aws.exception.ConvertImageException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// Handler value: example.Handler
public class Handler implements RequestHandler<S3Event, String> {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    private static final String JPG_TYPE = "jpg";
    private static final String JPG_MIME = "image/jpeg";
    private static final String PNG_TYPE = "png";
    private static final String PNG_MIME = "image/png";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(S3Event s3event, Context context) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("EVENT: {}", gson.toJson(s3event));
            }

            S3EventNotificationRecord s3Record = s3event.getRecords().get(0);

            // Object key may have spaces or unicode non-ASCII characters.
            String srcKey = s3Record.getS3().getObject().getUrlDecodedKey();
            String imageType = Utils.getExtension(srcKey);

            if (!JPG_TYPE.equals(imageType)) {
                logger.info("Skipping non jpg file {}", srcKey);
                return "";
            }

            // Download the image from S3 into a stream
            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            String srcBucket = s3Record.getS3().getBucket().getName();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));
            InputStream objectData = s3Object.getObjectContent();

            // Read the source image
            BufferedImage srcImage = ImageIO.read(objectData);

            saveAs(srcBucket, srcKey, srcImage, PNG_TYPE, PNG_MIME);

            return "Ok";
        } catch (IOException e) {
            throw new ConvertImageException(e);
        }
    }

    private void saveAs(String bucket, String srcKey, BufferedImage srcImage, String imgType, String mimeType) throws IOException {
        // Re-encode image to target format
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(srcImage, imgType, os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        // Set Content-Length and Content-Type
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(os.size());
        meta.setContentType(mimeType);


        String fileName = Utils.getFileName(srcKey);
        fileName = fileName.substring(0, fileName.indexOf("."));


        String dstKey = imgType + "/" + fileName + "." + imgType;
        // Uploading to S3 destination bucket
        logger.info("Writing to: {} / {}", bucket, dstKey);
        try {
            AmazonS3ClientBuilder.defaultClient().putObject(bucket, dstKey, is, meta);
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
            System.exit(1);
        }
        logger.info("Successfully converted {}/{} and uploaded to {}/{}", bucket, srcKey, bucket, dstKey);

    }
}