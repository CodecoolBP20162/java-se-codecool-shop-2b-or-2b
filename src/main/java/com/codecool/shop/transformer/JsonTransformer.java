package com.codecool.shop.transformer;

import io.gsonfire.GsonFireBuilder;
import spark.ResponseTransformer;

/**
 * Created by eb on 2017.05.14..
 */

public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(Object model) {
        return new GsonFireBuilder()
                .enableExposeMethodResult()
                .createGsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .toJson(model);
    }
}