package com.vzurauskas.nereides.jackson;

interface Checked<T> {
    T value() throws Exception;
}
