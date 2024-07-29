package com.tatianaars.kanban.util;

import java.time.LocalDateTime;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(dtf));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        String localDateTime = jsonReader.nextString();
        if (localDateTime != null && !localDateTime.isEmpty()) {
            return LocalDateTime.parse(localDateTime, dtf);
        } else {
            return null;
        }
    }
}
