package pro.sholokhov.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Artem Sholokhov
 */
public class CustomLocalDTDeserializer extends StdDeserializer<LocalDateTime> {

  public CustomLocalDTDeserializer() {
    super(LocalDateTime.class);
  }

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
      JsonProcessingException {
    return LocalDateTime.parse(p.getValueAsString());
  }

}