package com.huyphamthanh8290.huyorm.model.dataModel;

import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonObject;
import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonValue;

public class AppConfigData {

    @JsonValue(name = "name")
    private String				name;

    @JsonValue(name = "version")
    private String				version;

    @JsonValue(name = "lastUpdate")
    private String				lastUpdate;

    @JsonObject(name = "features")
    private FeatureData			feature;

    @JsonObject(name = "pages")
    private PageData			pages;

    @JsonObject(name = "splashScreen")
    private SplashScreenData	splashScreen;

    @JsonObject(name = "welcomePromo")
    private WelcomePromoData	welcomePromo;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public FeatureData getFeature() {
        return feature;
    }

    public PageData getPages() {
        return pages;
    }

    public SplashScreenData getSplashScreen() {
        return splashScreen;
    }

    public WelcomePromoData getWelcomePromo() {
        return welcomePromo;
    }

}