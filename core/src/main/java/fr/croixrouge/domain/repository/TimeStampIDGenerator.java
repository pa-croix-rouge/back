package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampIDGenerator implements IDGenerator<ID>
{
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public ID generate() {
        return new ID(LocalDateTime.now().format(formatter));
    }
}
