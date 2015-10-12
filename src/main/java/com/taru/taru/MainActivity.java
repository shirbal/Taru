package com.taru.taru;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.taru.taru.citi.utils.RestApiCaller;
import com.taru.taru.models.TestDataCreator;
import com.taru.taru.pinki.TransactionsOrganizer;
import com.taru.taru.views.TransactionAdapter;
import com.taru.taru.views.TransactionViewModel;



public class MainActivity extends AppCompatActivity {
    private ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TransactionViewModel data[] = null;
//        TransactionAdapter adapter = new TransactionAdapter(this,
//                R.layout.transaction_template, data);
//        listView1 = (ListView)findViewById(R.id.listView1);
//        listView1.setAdapter(adapter);



        new Thread(new Runnable() {
            public void run() {


                //final TransactionViewModel[] data1 = createData();
                final String testData = RestApiCaller.getInstance().getTestData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TextView viewById = (TextView) findViewById(R.id.shiranTest);
                        viewById.setText(testData);
//                        TransactionAdapter adapter1 = (TransactionAdapter)listView1.getAdapter();
//                        for(int i = 0; i < data1.length; i++) {
//                            adapter1.add(data1[i]);
//                        }
                    }
                });
            }
        }).start();

    }

    private TransactionViewModel[] createData() {
        TestDataCreator creator = TestDataCreator.getInstance();
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
