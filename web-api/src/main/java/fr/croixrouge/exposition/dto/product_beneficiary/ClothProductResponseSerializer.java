package fr.croixrouge.exposition.dto.product_beneficiary;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import fr.croixrouge.exposition.dto.product.ClothProductResponse;

import java.io.IOException;
import java.io.StringWriter;

public class ClothProductResponseSerializer extends JsonSerializer<ClothProductResponse> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(ClothProductResponse value,
                          JsonGenerator gen,
                          SerializerProvider serializers)
            throws IOException, JsonProcessingException {

        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, value);
        gen.writeFieldName(writer.toString());
    }
}
