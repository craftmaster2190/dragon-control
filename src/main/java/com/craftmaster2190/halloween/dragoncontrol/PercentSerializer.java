package com.craftmaster2190.halloween.dragoncontrol;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class PercentSerializer extends JsonSerializer<Percent> {

  @Override
  public void serialize(Percent value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeString(value == null ? null : value.toString());
  }
}
