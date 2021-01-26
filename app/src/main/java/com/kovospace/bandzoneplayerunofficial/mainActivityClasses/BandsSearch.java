package com.kovospace.bandzoneplayerunofficial.mainActivityClasses;

import android.app.Activity;
import android.content.Context;
import com.kovospace.bandzoneplayerunofficial.MainActivity;
import com.kovospace.bandzoneplayerunofficial.helpers.*;

public class BandsSearch extends OnFinishTypingHelper {

    private TestConnection testConnection;
    private BandsWrapper bandsWrapper;
    private Context context;
    private Activity mainActivity;
    private SearchFieldProgress searchFieldProgress;

    public BandsSearch(MainActivity mainActivity, Context context) {
        super();
        this.mainActivity = mainActivity;
        this.context = context;
        this.testConnection = new TestConnection(this.context);
        this.searchFieldProgress = new SearchFieldProgress(context);

        if (testConnection.isActive()) {
            this.bandsWrapper = new BandsWrapperNet(mainActivity, context);
        } else {
            this.bandsWrapper = new BandsWrapperOffline();
        }

        search("");
    }

    private void search(String search) {
        bandsWrapper.search(search);
        searchFieldProgress.start();
    }

    @Override
    public void doStuffNotOften() {
        if (getText().length() > 0) {
            search(getText());
        }
    }

    @Override
    public void doStuffOnZero() {
        search("");
    }
}