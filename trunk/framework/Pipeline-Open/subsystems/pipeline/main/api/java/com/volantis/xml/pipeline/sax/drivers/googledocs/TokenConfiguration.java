package com.volantis.xml.pipeline.sax.drivers.googledocs;

import com.volantis.xml.pipeline.sax.config.Configuration;

import java.util.HashMap;

public class TokenConfiguration implements Configuration {
    HashMap<String, AuthData> cache = new HashMap<String, AuthData>();

    public HashMap<String, AuthData> getCache() {
        return cache;
    }
}
