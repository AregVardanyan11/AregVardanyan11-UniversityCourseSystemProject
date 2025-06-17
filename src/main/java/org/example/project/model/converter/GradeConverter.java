package org.example.project.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.project.model.enums.Grade;

@Converter(autoApply = true)
public class GradeConverter implements AttributeConverter<Grade, String> {

    @Override
    public String convertToDatabaseColumn(Grade grade) {
        return grade != null ? grade.getLabel() : null;
    }

    @Override
    public Grade convertToEntityAttribute(String dbData) {
        return dbData != null ? Grade.fromString(dbData) : null;
    }
}
