package fr.croixrouge.exposition.dto.product_beneficiary;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import fr.croixrouge.exposition.dto.product.FoodProductResponse;

import java.io.IOException;
import java.io.StringWriter;

public class FoodProductResponseSerializer extends JsonSerializer<FoodProductResponse> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(FoodProductResponse value,
                          JsonGenerator gen,
                          SerializerProvider serializers)
            throws IOException, JsonProcessingException {

        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, value);
        gen.writeFieldName(writer.toString());
    }
}