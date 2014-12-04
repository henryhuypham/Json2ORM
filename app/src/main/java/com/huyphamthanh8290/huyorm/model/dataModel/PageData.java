package com.huyphamthanh8290.huyorm.model.dataModel;

import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonArray;
import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonObject;
import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonValue;

import java.util.List;

public class PageData {
    public static class CommonData {
        @JsonValue(name = "imageURL")
        private String imageUrl;

        @JsonValue(name = "description")
        private String description;

        public String getImageUrl() {
            return imageUrl;
        }

        public String getDescription() {
            return description;
        }

    }

    public static class ContactUsData {
        public static class ContactUsTopic {
            @JsonValue(name = "id")
            private String id;

            @JsonValue(name = "name")
            private String name;

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        @JsonValue(name = "imageURL")
        private String imageUrl;

        @JsonValue(name = "description")
        private String description;

        @JsonArray(name = "topics")
        private List<ContactUsTopic> topicsList;

        public String getImageUrl() {
            return imageUrl;
        }

        public String getDescription() {
            return description;
        }

        public List<ContactUsTopic> getTopicsList() {
            return topicsList;
        }

    }

    @JsonObject(name = "tos")
    private CommonData tos;

    @JsonObject(name = "bookParty")
    private CommonData bookPartyData;

    @JsonObject(name = "register")
    private CommonData registerData;

    @JsonObject(name = "opportunity")
    private CommonData opportunityData;

    @JsonObject(name = "aboutUs")
    private CommonData aboutUsData;

    @JsonObject(name = "contactUs")
    private ContactUsData contactUsData;

    public CommonData getTos() {
        return tos;
    }

    public CommonData getBookPartyData() {
        return bookPartyData;
    }

    public CommonData getRegisterData() {
        return registerData;
    }

    public CommonData getOpportunityData() {
        return opportunityData;
    }

    public CommonData getAboutUsData() {
        return aboutUsData;
    }

    public ContactUsData getContactUsData() {
        return contactUsData;
    }

}