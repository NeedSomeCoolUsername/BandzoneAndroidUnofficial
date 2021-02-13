package com.kovospace.bandzoneplayerunofficial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.BandsSearch;
import com.kovospace.bandzoneplayerunofficial.mainActivityClasses.PlayerWidget;

public class BandsActivity extends Activity {
    private EditText bandSearchField;
    private BandsSearch bandsSearch;
    private PlayerWidget playerWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bandSearchField = findViewById(R.id.bandInput);
        bandsSearch = new BandsSearch(BandsActivity.this, this);
        playerWidget = new PlayerWidget(this);

        bandSearchField.addTextChangedListener(
                bandsSearch.watchText()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connectionTest.isConnectionChanged()) {
            // refresh bands list if connection changed when going back
            System.out.println("activity should change");
            Intent intent = new Intent(this, BandsActivity.class);
            startActivity(intent);
            finish();
        }
        playerWidget.check();
    }

    @Override
    public void onUserInteraction() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}