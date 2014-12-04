package com.huyphamthanh8290.huyorm;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.huyphamthanh8290.huyorm.model.dataModel.AppConfigData;
import com.huyphamthanh8290.huyorm.model.dataModel.RecipeCategoryData;
import com.huyphamthanh8290.huyorm.model.viewModel.BusinessCentreItem;
import com.huyphamthanh8290.huyorm.orm.parser.JsonParser;
import com.huyphamthanh8290.huyorm.orm.query.DatabaseQuery;
import com.huyphamthanh8290.huyorm.orm.query.JsonRemoteDataGetter;
import com.huyphamthanh8290.huyorm.orm.query.ToDbSerializer;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Return data from Json (AppConfigData is annotated by our json annotation)
        AppConfigData configData = JsonRemoteDataGetter.getDataUsingJsonObject(AppConfigData.class, "API URL");

        // Return data from Json & save to db (RecipeCategoryData is annotated by both json annotation & serialize annotation)
        List<RecipeCategoryData> recipes = JsonRemoteDataGetter.getDataUsingJsonArray(RecipeCategoryData.class, "API URL");
        ToDbSerializer.saveToDb(RecipeCategoryData.class, recipes);

        // Load data from db (SELECT business center with id = 12
        List<BusinessCentreItem> centers = DatabaseQuery.select(BusinessCentreItem.class, String.format("business_center.city_id=%d", 12), "business_center.name ASC");

        // Search data from db (SELECT business centers with name contain "HCM")
        List<BusinessCentreItem> centers2 = DatabaseQuery.select(BusinessCentreItem.class, DatabaseQuery.makeLikeClause("HCM", "name"), null);
    }

}
