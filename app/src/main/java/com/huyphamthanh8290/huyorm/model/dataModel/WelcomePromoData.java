package com.huyphamthanh8290.huyorm.model.dataModel;

import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonValue;

public class WelcomePromoData {
    @JsonValue(name = "enable")
    private Boolean	enable;

    @JsonValue(name = "name")
    private String	name;

    @JsonValue(name = "image")
    private String	image;

    @JsonValue(name = "desc")
    private String	desc;

    @JsonValue(name = "promo_id")
    private String	promoId;

    public Boolean getEnable() {
        return enable;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return image;
    }

    public String getDescription() {
        return desc;
    }

    public String getPromoId(){
        return promoId;
    }

}
