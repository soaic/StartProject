package com.soaic.libcommon.network.requestbody;

import androidx.annotation.Nullable;

import com.soaic.libcommon.network.listener.ProgressListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import java.io.File;
import java.io.IOException;

/**
 * Created by sxiao on 19/4/12.
 */
public class CustomRequestBody extends RequestBody {
    private static final int SEGMENT_SIZE = 2048; // okio.Segment.SIZE
    private final File file;
    private final ProgressListener listener;
    private final MediaType contentType;

    private CustomRequestBody(MediaType contentType, File file, ProgressListener listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
    }

    public static CustomRequestBody create(@Nullable MediaType contentType, File file, ProgressListener listener) {
        return new CustomRequestBody(contentType, file, listener);
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();
                if (listener != null) {
                    listener.onProgress(total);
                }
            }
        } finally {
            Util.closeQuietly(source);
        }
    }

}
