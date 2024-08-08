//package com.phat.api_flutter.converters;
//
//import com.phat.api_flutter.models.Category;
//import com.phat.api_flutter.repository.CategoryRepository;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ObjectIdToCategoryConverter implements Converter<ObjectId, Category> {
//
//    private final CategoryRepository categoryRepository;
//
//    @Autowired
//    public ObjectIdToCategoryConverter(CategoryRepository categoryRepository) {
//        this.categoryRepository = categoryRepository;
//    }
//
//    @Override
//    public Category convert(ObjectId source) {
//        return categoryRepository.findById(source.toHexString()).orElse(null);
//    }
//}
