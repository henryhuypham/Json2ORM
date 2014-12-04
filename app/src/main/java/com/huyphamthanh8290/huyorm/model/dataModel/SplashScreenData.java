package com.huyphamthanh8290.huyorm.model.dataModel;

import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonValue;

public class SplashScreenData {

    @JsonValue(name="image")
    private String image;

    @JsonValue(name="text")
    private String text;

    public String getImage() {
        return image;
    }

    public String getText() {
        return text;
    }

}
