package com.taru.taru;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.taru.taru.citi.utils.APICreator;
import com.taru.taru.citi.utils.RestApiCaller;
import com.taru.taru.citi.utils.UrlCreator;
import com.taru.taru.models.Transaction;
import com.taru.taru.views.TransactionAdapter;
import com.taru.taru.views.TransactionViewModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends Activity {
    private ListView listView1;
    private Context cont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView1 = (ListView)findViewById(R.id.listView1);
        cont = this;
        try {
            new Thread(new Runnable() {
                public void run() {
                    final TransactionViewModel[] data1 = createData();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TransactionAdapter adapter = new TransactionAdapter(cont, R.layout.transaction_template, data1);
                            listView1.setAdapter(adapter);
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TransactionViewModel[] createData() {
        UrlCreator url = new UrlCreator();
        url.append(UrlCreator.TRANSACTINOS);
        ArrayList<TransactionViewModel> transactionViewModels = new ArrayList<>();
        Map<String, List<Transaction>> data = RestApiCaller.getInstance().getData(url, APICreator.GET_METHOD);

        Set<Map.Entry<String, List<Transaction>>> entries = data.entrySet();

        Iterator<Map.Entry<String, List<Transaction>>> iterator = entries.iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, List<Transaction>> next = iterator.next();
            String categoryName = next.getKey();
            List<Transaction> transactions = next.getValue();
            Iterator<Transaction> iterator1 = transactions.iterator();
            while(iterator1.hasNext()) {
                Transaction next1 = iterator1.next();
                TransactionViewModel item = new TransactionViewModel(categoryName,
                                                                     next1.getDate(),
                                                                     next1.getAmount());
                transactionViewModels.add(item);
            }

        }

        TransactionViewModel[] res = new TransactionViewModel[transactionViewModels.size()];
        Iterator<TransactionViewModel> iterator1 = transactionViewModels.iterator();
        int i = 0;
        while(iterator1.hasNext()) {
            TransactionViewModel next = iterator1.next();
            res[i] = next;
            i++;
        }
        return res;
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
