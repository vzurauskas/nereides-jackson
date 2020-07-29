package com.vzurauskas.nereides.jackson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

final class ByteArray {

    private final byte[] bytes;

    ByteArray(Json json) {
        this(json.bytes());
    }

    ByteArray(InputStream stream) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[1024];
            while (true) {
                int size = stream.read(data, 0, data.length);
                if (size == -1) {
                    stream.close();
                    break;
                }
                output.write(data, 0, size);
            }
            output.flush();
            bytes = output.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public byte[] value() {
        return bytes.clone();
    }
}
