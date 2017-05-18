package com.sedatcan.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseJsonDateDeserializer extends JsonDeserializer<Date> {

    protected abstract SimpleDateFormat getSimpleDateFormat();

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonToken jsonToken = jsonParser.getCurrentToken();
        if (jsonToken == JsonToken.VALUE_STRING) {
            String text = jsonParser.getText().trim();
            try {
                if ("".equals(text)) {
                    return null;
                }
                return getSimpleDateFormat().parse(text);
            } catch (ParseException e) {
                throw new IOException(e);
            }
        } else if (jsonToken == JsonToken.VALUE_NUMBER_INT) {
            long dateValue = jsonParser.getLongValue();
            return new Date(dateValue);
        } else {
            throw new IOException("Expecting String for Date Deserializing Token:" + jsonToken);
        }
    }

}
