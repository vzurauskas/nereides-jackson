package com.vzurauskas.nereides.jackson;

import java.io.IOException;
import java.io.InputStream;

final class AutoResetInputStream extends InputStream {

    private final InputStream origin;

    AutoResetInputStream(InputStream origin) {
        super();
        origin.mark(1 << 24);
        this.origin = origin;
    }

    @Override
    public void close() throws IOException {
        super.close();
        origin.reset();
    }

    @Override
    public int read() throws IOException {
        return origin.read();
    }
}
