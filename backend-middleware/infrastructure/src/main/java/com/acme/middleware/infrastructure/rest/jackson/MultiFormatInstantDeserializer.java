package com.acme.middleware.infrastructure.rest.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Jackson deserializer that accepts multiple date formats and converts them to an Instant.
 *
 * Supported inputs include:
 * - ISO_INSTANT (e.g., 2025-09-06T23:59:59Z)
 * - ISO_OFFSET_DATE_TIME (e.g., 2025-09-06T23:59:59-03:00)
 * - ISO_LOCAL_DATE_TIME (assumed UTC) (e.g., 2025-09-06T23:59:59)
 * - ISO_LOCAL_DATE (yyyy-MM-dd, start of day UTC)
 * - Epoch milliseconds (number)
 */
public class MultiFormatInstantDeserializer extends JsonDeserializer<Instant> {

    private static final List<DateTimeFormatter> FORMATTERS = new ArrayList<>();

    static {
        // Strict ISO instant (always Z)
        FORMATTERS.add(DateTimeFormatter.ISO_INSTANT);
        // Offset date-time (with zone offset)
        FORMATTERS.add(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        // Local date-time (assume UTC)
        FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        // Local date only (assume start of day UTC)
        FORMATTERS.add(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.getCurrentToken();

        if (token == JsonToken.VALUE_NUMBER_INT) {
            // Treat as epoch milliseconds
            long epochMillis = p.getLongValue();
            return Instant.ofEpochMilli(epochMillis);
        }

        if (token == JsonToken.VALUE_STRING) {
            String text = p.getText();
            if (text == null || text.isBlank()) {
                return null;
            }
            String trimmed = text.trim();

            // Try each formatter in order
            for (DateTimeFormatter fmt : FORMATTERS) {
                try {
                    if (fmt == DateTimeFormatter.ISO_INSTANT) {
                        return Instant.parse(trimmed);
                    }
                    if (fmt == DateTimeFormatter.ISO_OFFSET_DATE_TIME) {
                        OffsetDateTime odt = OffsetDateTime.parse(trimmed, fmt);
                        return odt.toInstant();
                    }
                    if (fmt == DateTimeFormatter.ISO_LOCAL_DATE_TIME) {
                        LocalDateTime ldt = LocalDateTime.parse(trimmed, fmt);
                        return ldt.toInstant(ZoneOffset.UTC);
                    }
                    if (fmt == DateTimeFormatter.ISO_LOCAL_DATE) {
                        LocalDate ld = LocalDate.parse(trimmed, fmt);
                        return ld.atStartOfDay(ZoneOffset.UTC).toInstant();
                    }
                } catch (DateTimeParseException ignored) {
                    // try next
                }
            }

            // Fallback: try to parse as epoch seconds if it looks numeric
            if (trimmed.matches("^-?\\d{10}$")) {
                long epochSeconds = Long.parseLong(trimmed);
                return Instant.ofEpochSecond(epochSeconds);
            }
        }

        // Let Jackson produce a standard exception describing the problem
        return (Instant) ctxt.handleWeirdStringValue(Instant.class, p.getValueAsString(),
                "Unparseable date-time. Supported formats: ISO-8601 (with/without offset), yyyy-MM-dd, epoch millis/seconds");
    }
}
