package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kovospace.bandzoneplayerunofficial.MainActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.JsonRequest;
import com.kovospace.bandzoneplayerunofficial.objects.Band;
import com.kovospace.bandzoneplayerunofficial.objects.Page;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BandsWrapperNet extends BandsWrapper {
    private final int OPERATION_NEXTPAGE = 2;
    private final int OPERATION_SEARCH = 1;
    private final String QUERY_URL = "http://172.104.155.216:3030/search/bands?q=";
    private final int ITEMS_PER_PAGE = 20;
    private int operation;
    private BandsJsonRequest bandsJsonRequest;
    private String query;
    private Page page;

    public BandsWrapperNet(MainActivity mainActivity, Context context) {
        super(mainActivity, context);
        bandsJsonRequest = new BandsJsonRequest(activity);
    }

    public class BandsJsonRequest extends JsonRequest {
        private Gson gson = new Gson();
        private JSONArray bandsJsonArrray = new JSONArray();
        private List<Band> bandsList = new ArrayList<>();
        private Type bandsListType = new TypeToken<ArrayList<Band>>(){}.getType();
        private JSONObject pageData;

        public BandsJsonRequest(Activity activity) {
            super(activity);
        }

        @Override
        public void doStuff() {
            try {
                pageData = responseData.getJSONObject("table");
                bandsJsonArrray = pageData.getJSONArray("data");
                bandsList = gson.fromJson(String.valueOf(bandsJsonArrray), bandsListType);
                page = new Page(
                        pageData.getInt("current_page"),
                        ITEMS_PER_PAGE,
                        pageData.getInt("items_current_page"),
                        pageData.getInt("pages_count"),
                        pageData.getInt("items_total"),
                        bandsList
                );
                switch(operation) {
                    case OPERATION_NEXTPAGE:
                        add(page);
                        break;
                    case OPERATION_SEARCH:
                        update(page);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void search(String s) {
        operation = OPERATION_SEARCH;
        super.search(s);
        query = QUERY_URL + searchString;
        bandsJsonRequest.fetch(query);
    }

    @Override
    public void loadNextContent() {
        operation = OPERATION_NEXTPAGE;
        query = QUERY_URL + searchString + "&p=" + nextPageToLoad;
        bandsJsonRequest.fetch(query);
    }

    @Override
    public int setDataSourceType() {
        return DATA_SOURCE_INTERNET;
    }

}