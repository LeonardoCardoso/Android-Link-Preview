package com.leocardz.link.preview.library;

import java.util.concurrent.CountDownLatch;

/**
 * A LinkPreviewCallback that allows the test to wait until parsing is complete before evaluating.
 */
class TestLinkPreviewCallback implements LinkPreviewCallback {
    SourceContent sourceContent;
    boolean isNull;
    final CountDownLatch signal;

    public TestLinkPreviewCallback(CountDownLatch signal) {
        super();
        this.signal = signal;
    }

    @Override
    public void onPre() {

    }

    @Override
    public void onPos(SourceContent sourceContent, boolean isNull) {
        this.sourceContent = sourceContent;
        this.isNull = isNull;
        signal.countDown();
    }
}
