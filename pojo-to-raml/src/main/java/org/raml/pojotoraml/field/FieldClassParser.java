package org.raml.pojotoraml.field;

import org.raml.pojotoraml.ClassParser;
import org.raml.pojotoraml.ClassParserFactory;
import org.raml.pojotoraml.Property;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created. There, you have it.
 */
public class FieldClassParser implements ClassParser {

    @Override
    public List<Property> properties(Type sourceType) {

        if ( ! (sourceType instanceof Class) ) {
            return Collections.emptyList();
        }

        Class<?> sourceClass = (Class<?>) sourceType;
        List<Property> props = new ArrayList<>();
        for (final Field field : sourceClass.getDeclaredFields()) {

            if (! Modifier.isTransient(field.getModifiers())) {
                Property prop = new FieldProperty(field);
                props.add(prop);
            }
        }
        return props;
    }

    @Override
    public Collection<Type> parentClasses(Type sourceType) {

        if ( ! (sourceType instanceof Class) ) {
            return Collections.emptyList();
        }

        Class<?> sourceClass = (Class<?>) sourceType;

        ArrayList<Type> type = new ArrayList<>();
        if ( sourceClass.getSuperclass() != Object.class && sourceClass.getSuperclass() != null ) {
            type.add(sourceClass.getSuperclass());
        }
        return type;
    }


    public static ClassParserFactory factory() {
        return clazz -> new FieldClassParser();
    }
}
