package com.mans.ecommerce.b2c.server.config.jackson;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class InstantJson
{
    static public class InstanceDeserializer extends JsonDeserializer<Instant>
    {
        @Override
        public Instant deserialize(JsonParser parser, DeserializationContext context)
                throws IOException
        {
            switch (parser.getCurrentToken())
            {
            case VALUE_NUMBER_INT:
                long rawDate = parser.getLongValue();
                return Instant.ofEpochSecond(rawDate);
            }
            throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected string.");
        }
    }

    static public class InstanceSerializer extends JsonSerializer<Instant>
    {

        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException
        {
            gen.writeNumber(value.getEpochSecond());
        }
    }
}
