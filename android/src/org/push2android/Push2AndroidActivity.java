package org.push2android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Push2AndroidActivity extends Activity {

    private int mScreenId = -1;
    private int mAccountSelectedPosition = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenContent(R.layout.main);
    }

    private void setScreenContent(int screenId) {
        mScreenId = screenId;
        setContentView(screenId);
        switch (screenId) {
            // Screen shown if phone is registered/connected
            case R.layout.main: {
                setMainScreenContent();
                break;
            }
        }
    }

    private void setMainScreenContent() {

        // Display accounts
        String accounts[] = getGoogleAccounts();
        if (accounts.length == 0) {
            TextView promptText = (TextView) findViewById(R.id.select_text);
            promptText.setText(R.string.no_accounts);
            TextView nextText = (TextView) findViewById(R.id.click_next_text);
            nextText.setVisibility(TextView.INVISIBLE);
            //nextButton.setEnabled(false);
        } else {
            ListView listView = (ListView) findViewById(R.id.select_account);
            listView.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.account, accounts));
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setItemChecked(mAccountSelectedPosition, true);
        }
    }

     private String[] getGoogleAccounts() {
        ArrayList<String> accountNames = new ArrayList<String>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (account.type.equals("com.google")) {
                accountNames.add(account.name);
            }
        }

        String[] result = new String[accountNames.size()];
        accountNames.toArray(result);
        return result;
    }
}
