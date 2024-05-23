package com.casino.backend.enums;

import com.casino.backend.exception.InvalidTransactionTypeException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

import java.io.IOException;

@Getter
// Specifies that TransactionType should be deserialized using the custom deserializer defined below
@JsonDeserialize(using = TransactionType.TransactionTypeDeserializer.class)
public enum TransactionType {
    WAGER("WAGER"),
    WIN("WIN");

    // Getter method to retrieve the value of the enum constant
    private final String value;

    // Constructor to initialize the enum constants with the provided value
    TransactionType(String value) {
        this.value = value;
    }

    // Override toString method to return the value in uppercase
    @Override
    public String toString() {
        return value.toUpperCase();
    }

    /**
     * The purpose of this code is to ensure that the TransactionType enum can be deserialized from JSON in a case-insensitive manner.
     * This allows JSON input such as "wager", "WAGER", "win", or "WIN" to be correctly mapped to the corresponding TransactionType enum constant without causing errors.
     * This is particularly useful for handling user input or external data sources that may not adhere to a strict case format.
     */
    // Custom deserializer for the TransactionType enum
    public static class TransactionTypeDeserializer extends JsonDeserializer<TransactionType> {
        @Override
        public TransactionType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            // Retrieve the text (value) from the JSON parser
            String value = jsonParser.getText();
            // Iterate through all enum constants to find a match (case-insensitive)
            for (TransactionType type : TransactionType.values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;  // Return the matching enum constant
                }
            }
            // Throw an exception if no matching enum constant is found
            throw new InvalidTransactionTypeException("Invalid transaction type: " + value + ". Please ensure that you use 'WIN' or 'WAGER' as the transaction type.");
        }
    }
}
