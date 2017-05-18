package com.sedatcan.util;

import java.text.SimpleDateFormat;

public class JsonSimpleDateDeserializer extends BaseJsonDateDeserializer {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    protected SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(DATE_FORMAT);
    }

}
