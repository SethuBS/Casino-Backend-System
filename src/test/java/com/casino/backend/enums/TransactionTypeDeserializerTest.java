package com.casino.backend.enums;

import com.casino.backend.exception.InvalidTransactionTypeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

public class TransactionTypeDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDeserializeWAGER() throws IOException {
        String json = "\"WAGER\"";
        TransactionType type = mapper.readValue(json, TransactionType.class);
        assertEquals(TransactionType.WAGER, type);
    }

    @Test
    public void testDeserializeWIN() throws IOException {
        String json = "\"WIN\"";
        TransactionType type = mapper.readValue(json, TransactionType.class);
        assertEquals(TransactionType.WIN, type);
    }

    @Test
    public void testDeserializeInvalidTransactionType() throws IOException {
        String json = "\"invalid\"";
        try {
            mapper.readValue(json, TransactionType.class);
            fail("Expected InvalidTransactionTypeException to be thrown");
        } catch (InvalidTransactionTypeException e) {
            assertEquals("Invalid transaction type: invalid. Please ensure that you use 'WIN' or 'WAGER' as the transaction type.", e.getMessage());
        }
    }
}
