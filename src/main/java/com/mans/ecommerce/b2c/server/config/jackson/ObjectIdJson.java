package com.mans.ecommerce.b2c.server.config.jackson;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;

public class ObjectIdJson
{
    static class ObjectIDDeserializer extends JsonDeserializer<ObjectId>
    {

        @Override
        public ObjectId deserialize(JsonParser parser, DeserializationContext context)
                throws IOException
        {
            switch (parser.getCurrentToken())
            {
            case VALUE_STRING:
                String rawDate = parser.getText();
                return new ObjectId(rawDate);
            }
            throw context.wrongTokenException(parser, JsonToken.START_ARRAY, "Expected string.");
        }
    }

    static class ObjectIDSerializer extends JsonSerializer<ObjectId>
    {

        @Override
        public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException
        {
            gen.writeString(value.toHexString());
        }

    }
}
