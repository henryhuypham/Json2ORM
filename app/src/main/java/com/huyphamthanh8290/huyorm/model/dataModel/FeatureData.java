package com.huyphamthanh8290.huyorm.model.dataModel;

import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonValue;

public class FeatureData {

    @JsonValue(name = "tip")
    private Boolean tip;

    @JsonValue(name = "video")
    private Boolean video;

    @JsonValue(name = "home_menu")
    private Boolean homeMenu;

    @JsonValue(name = "recipe")
    private Boolean recipe;

    @JsonValue(name = "event")
    private Boolean event;

    public Boolean isTipEnable() {
        return tip;
    }

    public Boolean isVideoEnable() {
        return video;
    }

    public Boolean isHomeMenuEnable() {
        return homeMenu;
    }

    public Boolean isRecipeEnable() {
        return recipe;
    }

    public Boolean isEventEnable() {
        return event;
    }

}
