package com.leocardz.link.preview.library;

public abstract class BaseImagePickingStrategy implements ImagePickingStrategy {
    private int imageQuantity = TextCrawler.ALL;

    @Override
    public void setImageQuantity(int imageQuantity) {
        this.imageQuantity = imageQuantity;
    }

    @Override
    public int getImageQuantity() {
        return imageQuantity;
    }
}
