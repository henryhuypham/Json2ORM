package com.huyphamthanh8290.huyorm.model.dataModel;

import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonValue;
import com.huyphamthanh8290.huyorm.orm.annotation.serialize.ToColumn;
import com.huyphamthanh8290.huyorm.orm.annotation.serialize.ToTable;

@ToTable(name = "Recipes")
public class RecipeData {

    @JsonValue(name = "id")
    @ToColumn(name = "id")
    private String id;

    @JsonValue(name = "name")
    @ToColumn(name = "name")
    private String name;

    @JsonValue(name = "tags")
    @ToColumn(name = "tags")
    private String tags;

    @JsonValue(name = "imageURL")
    @ToColumn(name = "imageURL")
    private String imageUrl;

    @JsonValue(name = "videoURL")
    @ToColumn(name = "videoURL")
    private String videoUrl;

    @JsonValue(name = "ingredients")
    @ToColumn(name = "ingredients")
    private String ingredients;

    @JsonValue(name = "steps")
    @ToColumn(name = "steps")
    private String steps;

    @ToColumn(name = "category_id")
    private String parentId;

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
