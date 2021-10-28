package com.endryha.aws.image;

import java.util.Locale;

public enum ImageType {
    JPG, PNG, BMP, GIF, PDF("application/pdf");

    private final String mimeType;

    ImageType(String mimeType) {
        this.mimeType = mimeType;
    }

    ImageType() {
        this.mimeType = "image/" + name().toLowerCase(Locale.ROOT);
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getType() {
        return name().toLowerCase(Locale.ROOT);
    }
}
