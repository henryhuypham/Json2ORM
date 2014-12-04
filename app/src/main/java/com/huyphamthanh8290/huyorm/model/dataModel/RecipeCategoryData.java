package com.huyphamthanh8290.huyorm.model.dataModel;

import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonArray;
import com.huyphamthanh8290.huyorm.orm.annotation.json.JsonValue;
import com.huyphamthanh8290.huyorm.orm.annotation.serialize.ToChildTable;
import com.huyphamthanh8290.huyorm.orm.annotation.serialize.ToColumn;
import com.huyphamthanh8290.huyorm.orm.annotation.serialize.ToTable;
import com.huyphamthanh8290.huyorm.orm.postProcess.PostProcessJson;

import java.util.List;

@ToTable(name = "Recipe_category")
public class RecipeCategoryData implements PostProcessJson {

    @JsonValue(name = "id")
    @ToColumn(name = "id")
    private String id;

    @JsonValue(name = "name")
    @ToColumn(name = "name")
    private String name;

    @JsonValue(name = "iconURL")
    @ToColumn(name = "iconURL")
    private String iconUrl;

    @JsonArray(name = "recipes")
    @ToChildTable(name = "Recipes")
    private List<RecipeData> recipes;

    @Override
    public void postGetDataProcess() {
        for (RecipeData r : recipes) {
            r.setParentId(id);
        }
    }

}