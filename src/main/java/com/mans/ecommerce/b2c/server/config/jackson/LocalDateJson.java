package com.mans.ecommerce.b2c.server.config.jackson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateJson
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    static class LocalDateDeserializer extends JsonDeserializer<java.time.LocalDate>
    {

        @Override
        public java.time.LocalDate deserialize(JsonParser parser, DeserializationContext context)
                throws IOException
        {
            switch (parser.getCurrentToken())
            {
            case VALUE_STRING:
                String rawDate = parser.getText();
                return FORMATTER.parse(rawDate, java.time.LocalDate::from);
            }
            throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected string.");
        }
    }

    static class LocalDateSerializer extends JsonSerializer<LocalDate>
    {

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException
        {
            gen.writeString(FORMATTER.format(value));
        }

    }
}
