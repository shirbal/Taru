package com.taru.taru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.taru.taru.citi.utils.RestApiCaller;
import com.taru.taru.models.TestDataCreator;
import com.taru.taru.pinki.TransactionsOrganizer;
import com.taru.taru.views.TransactionAdapter;
import com.taru.taru.views.TransactionViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TransactionViewModel data[] = createData();
        TransactionAdapter adapter = new TransactionAdapter(this,
                R.layout.transaction_template, data);
        listView1 = (ListView)findViewById(R.id.listView1);
        listView1.setAdapter(adapter);

    }

    private TransactionViewModel[] createData() {
        TestDataCreator creator = new TestDataCreator();
        TransactionsOrganizer org = new TransactionsOrganizer();
        TransactionViewModel[] transactionViewModels = org.create(creator.getData());
        return transactionViewModels;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
