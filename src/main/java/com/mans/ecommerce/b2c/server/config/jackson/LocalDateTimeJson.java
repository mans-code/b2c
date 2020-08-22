package com.mans.ecommerce.b2c.server.config.jackson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateTimeJson
{
    static public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime>
    {

        private final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
                throws IOException
        {
            switch (parser.getCurrentToken())
            {
            case VALUE_STRING:
                String rawDate = parser.getText();

                return FORMATTER.parse(rawDate, LocalDateTime::from);
            }
            throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected string.");
        }
    }

    static public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime>
    {

        private final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public void serialize(java.time.LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException
        {
            gen.writeString(FORMATTER.format(value));
        }
    }
}
