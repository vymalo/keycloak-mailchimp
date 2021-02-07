package com.bayamsell.keycloak.jpa;

import org.keycloak.events.EventType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Converter
public class EventsConverter
        implements AttributeConverter<Collection<EventType>, String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(Collection<EventType> eventTypes) {
        if (eventTypes != null) {
            var strList = eventTypes.stream().map(EventType::name).collect(Collectors.toList());
            return String.join(SPLIT_CHAR, strList);
        }
        return "";
    }

    @Override
    public Collection<EventType> convertToEntityAttribute(String string) {
        if (string != null) {
            return Arrays.stream(string.split(SPLIT_CHAR)).map(EventType::valueOf).collect(Collectors.toList());
        }
        return emptyList();
    }

}
