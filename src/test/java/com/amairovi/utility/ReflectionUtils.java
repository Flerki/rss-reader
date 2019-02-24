package com.amairovi.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

    public static void setFinalStatic(Object instance, String fieldName, Object newValue) throws Exception {
        Class<?> instanceClass = instance.getClass();
        for (Field field : instanceClass.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                setFinalStatic(instance, field, newValue);
            }
        }
    }

    private static void setFinalStatic(Object instance, Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(instance, newValue);
    }
}