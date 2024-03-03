package com.devapi.util;

import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class DevApiUtilities {
    private DevApiUtilities(){
        
    }

    public static final String TIME_ZONE = "+05:30";
    public static Object mapObjectToObject(Object object, Class<?> clazz) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Field[] fields = object.getClass().getDeclaredFields();
        Object instance =  clazz.getDeclaredConstructor().newInstance();
        // For each field, set the value of the corresponding field in the item object
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(object) != null) {
                Field itemField;
                try {
                    itemField = instance.getClass().getDeclaredField(field.getName());
                } catch (NoSuchFieldException e) {
                    continue;
                }
                itemField.setAccessible(true);
                itemField.set(instance, field.get(object));
            }
        }
        return instance;
    }
    private static ModelMapper modelMapper = new ModelMapper();

    public static <T> T mapObjectToObjectWithModelMapper(Object object, Class<T> clazz) {
        return modelMapper.map(object, clazz);
    }

    public static Object mapObjectToObject(Object object, Object object1) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Field[] fields = object.getClass().getDeclaredFields();
        // For each field, set the value of the corresponding field in the item object
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(object) != null) {
                Field itemField;
                try {
                    itemField = object1.getClass().getDeclaredField(field.getName());
                } catch (NoSuchFieldException e) {
                    continue;
                }
                itemField.setAccessible(true);
                itemField.set(object1, field.get(object));
            }
        }
        return object1;
    }

    public static Timestamp subtractDays(Timestamp timestamp, int days, String timeZoneId) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.of(timeZoneId));
        ZonedDateTime adjustedDateTime = zonedDateTime.minusDays(days);

        return Timestamp.from(adjustedDateTime.toInstant());
    }

    public static Timestamp subtractDays(int days) {
        return subtractDays(new Timestamp(System.currentTimeMillis()),days,TIME_ZONE);
    }

    public static Timestamp getCurrentTimeStamp(Timestamp timestamp, String timeZoneId) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.of(timeZoneId));
        return Timestamp.from(zonedDateTime.toInstant());
    }

    public static Timestamp getCurrentTimeStamp() {
        return getCurrentTimeStamp(new Timestamp(System.currentTimeMillis()),TIME_ZONE);
    }

    public static <T> List<T> getRandomSubset(List<T> inputList, int n) {
        if (n < 0 || n > inputList.size()) {
            throw new IllegalArgumentException("Invalid subset size");
        }
        Collections.shuffle(inputList);
        return inputList.subList(inputList.size() - n, inputList.size());
    }



}
