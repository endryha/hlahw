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
import com.aspose.imaging.Image;
import com.aspose.imaging.ImageOptionsBase;
import com.aspose.imaging.imageoptions.BmpOptions;
import com.aspose.imaging.imageoptions.GifOptions;
import com.aspose.imaging.imageoptions.JpegOptions;
import com.aspose.imaging.imageoptions.PdfOptions;
import com.aspose.imaging.imageoptions.PngOptions;
import com.endryha.aws.exception.ConvertImageException;
import com.endryha.aws.image.ImageType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

// Handler value: example.Handler
public class Handler implements RequestHandler<S3Event, String> {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<ImageType, ImageOptionsBase> IMAGE_OPTIONS = new EnumMap<>(ImageType.class);

    static {
        IMAGE_OPTIONS.put(ImageType.JPG, new JpegOptions());
        IMAGE_OPTIONS.put(ImageType.PNG, new PngOptions());
        IMAGE_OPTIONS.put(ImageType.BMP, new BmpOptions());
        IMAGE_OPTIONS.put(ImageType.GIF, new GifOptions());
        IMAGE_OPTIONS.put(ImageType.PDF, new PdfOptions());
    }

    @Override
    public String handleRequest(S3Event s3event, Context context) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("EVENT: {}", gson.toJson(s3event));
            }

            S3EventNotificationRecord s3Record = s3event.getRecords().get(0);
            String srcKey = s3Record.getS3().getObject().getUrlDecodedKey();

            // Download the image from S3 into a stream
            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            String srcBucket = s3Record.getS3().getBucket().getName();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));

            try (InputStream objectData = s3Object.getObjectContent()) {
                Image srcImage = Image.load(objectData);

                IMAGE_OPTIONS.forEach((type, options) -> {
                    String dstKey = buildDestinationKey(srcKey, type);

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    srcImage.save(os, options);
                    InputStream is = new ByteArrayInputStream(os.toByteArray());

                    ObjectMetadata meta = new ObjectMetadata();
                    meta.setContentLength(os.size());
                    meta.setContentType(type.getMimeType());
                    meta.addUserMetadata("Generated", Boolean.TRUE.toString());

                    // Uploading to S3 destination bucket
                    if (upload(srcBucket, dstKey, is, meta)) {
                        logger.info("Successfully converted {}/{} and uploaded to {}/{}", srcBucket, srcKey, srcBucket, dstKey);
                    } else {
                        logger.info("Failed to convert {}/{} into {}/{}", srcBucket, srcKey, srcBucket, dstKey);
                    }
                });
            }
            return "Ok";
        } catch (IOException e) {
            throw new ConvertImageException(e);
        }
    }

    private static boolean upload(String bucket, String dstKey, InputStream is, ObjectMetadata meta) {
        logger.info("Writing to: {} / {}", bucket, dstKey);
        try {
            AmazonS3ClientBuilder.defaultClient().putObject(bucket, dstKey, is, meta);
            return true;
        } catch (AmazonServiceException e) {
            logger.error(e.getErrorMessage());
            return false;
        }
    }

    private static String buildDestinationKey(String srcKey, ImageType type) {
        String fileName = Utils.getFileName(srcKey);
        fileName = fileName.substring(0, fileName.indexOf("."));
        return type.getType() + "/" + fileName + "." + type.getType();
    }
}