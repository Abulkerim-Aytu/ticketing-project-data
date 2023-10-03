package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtil{
private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
//    Example 1
//    public <T> T convert(Object objectToBeConverted,Class<T> convertedObject){
//        return modelMapper.map(objectToBeConverted,convertedObject);
//    }

//    Example 2
    public <T> T convert(Object objectToBeConverted,T convertedObject ){
        return modelMapper.map(objectToBeConverted,(Type) convertedObject.getClass());
    }
}