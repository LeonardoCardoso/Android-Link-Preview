package com.leocardz.link.preview.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by adamleedy on 8/1/17.
 */

public abstract class LinkPreviewView extends View{
    protected SourceContent sourceContent;

    public LinkPreviewView(Context context) {
        super(context);
    }

    public LinkPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void applySourceContent(SourceContent sourceContent){
        this.sourceContent = sourceContent;
    }
}
