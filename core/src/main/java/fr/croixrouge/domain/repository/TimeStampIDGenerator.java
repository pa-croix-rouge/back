package fr.croixrouge.domain.repository;

import fr.croixrouge.domain.model.ID;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampIDGenerator implements IDGenerator<ID>
{
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    @Override
    public ID generate() {
        return new ID(Long.valueOf(LocalDateTime.now().format(formatter)));
    }
}
