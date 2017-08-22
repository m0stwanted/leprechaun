package pro.sholokhov.utils;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;

/**
 * @author Artem Sholokhov
 */
public class CustomLocalDTSerializerTest {

  private static CustomLocalDTSerializer serializer = new CustomLocalDTSerializer();

  @Test
  public void shouldSerializeDateTimeProperly() throws Exception {
    LocalDateTime now = LocalDateTime.now();

    Writer jsonWriter = new StringWriter();
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
    SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
    serializer.serialize(now, jsonGenerator, serializerProvider);
    jsonGenerator.flush();

    assertEquals("String representation doesn't matches.",
        "\"" + now.toString() + "\"", jsonWriter.toString());
  }

}