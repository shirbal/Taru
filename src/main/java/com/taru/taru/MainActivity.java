package com.taru.taru;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.taru.taru.citi.utils.APICreator;
import com.taru.taru.citi.utils.RestApiCaller;
import com.taru.taru.citi.utils.UrlCreator;
import com.taru.taru.models.Transaction;
import com.taru.taru.models.enums.TransactionType;
import com.taru.taru.utils.NumbersUtil;
import com.taru.taru.utils.TransactionUtil;
import com.taru.taru.views.TransactionAdapter;
import com.taru.taru.views.TransactionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    private ListView _transactionsViewList;
    private Context _cont;
    private CalendarView calendar;
    private LinearLayout _dayDetails;

    private TextView _totalExpensesView;
    private TextView _totalIncomeView;
    private TextView _totalBalanceView;

    private Double _totalExpenses;
    private Double _totalIncome;
    private Double _totalBalance;

    private Map<String, List<Transaction>> _daysToTransactions = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initializeViews();

        _cont = this;
        //initializeCalendar();
        getDataOnBackgroung();

    }

    private void initializeViews() {
        _dayDetails = (LinearLayout)findViewById(R.id.day_details_lay);
        _transactionsViewList = (ListView)findViewById(R.id.listView1);
        _totalExpensesView = (TextView)findViewById(R.id.total_expenses);
        _totalIncomeView = (TextView)findViewById(R.id.total_income);
        _totalBalanceView = (TextView)findViewById(R.id.total_balance);

    }

    private void getDataOnBackgroung() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    final TransactionViewModel[] data1 = createData();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _totalBalanceView.setText(_totalBalance.toString());
                            _totalExpensesView.setText(_totalExpenses.toString());
                            _totalIncomeView.setText(_totalIncome.toString());
                            TransactionAdapter adapter = new TransactionAdapter(_cont, R.layout.transaction_template, data1);
                            _transactionsViewList.setAdapter(adapter);
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TransactionViewModel[] createData() {
        List<Transaction> data = getDataFromRest();
        /**
         * update the total views
         */
        updateTotals(data);
        /**
         * Update the map before displaying it
         */
        TransactionUtil.updateDaysToTransactionMap(data, _daysToTransactions);

        /**
         * update the view models to display the list of transation
         */
        ArrayList<TransactionViewModel> transactionViewModels = new ArrayList<>();
        fillTransactionModelsList(transactionViewModels, data);
        TransactionViewModel[] res = createTransactionViewModels(transactionViewModels);
        return res;
    }

    private List<Transaction> getDataFromRest() {
        UrlCreator url = new UrlCreator();
        url.append(UrlCreator.TRANSACTINOS);
        return RestApiCaller.getInstance().getData(url, APICreator.GET_METHOD);
    }

    /**
     *
     * @param transactionViewModels
     * @return
     */
    private TransactionViewModel[] createTransactionViewModels(ArrayList<TransactionViewModel> transactionViewModels) {
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

    /**
     *
     * @param transactionViewModels
     * @param data
     */
    private void fillTransactionModelsList(ArrayList<TransactionViewModel> transactionViewModels, List<Transaction> data) {
        Iterator<Transaction> iterator = data.iterator();
        while(iterator.hasNext()) {
            Transaction next = iterator.next();
            String categoryName = next.getCategory();
            String date = next.getDate();
            double amount = next.getAmount();
            TransactionType type = next.getType();
            TransactionViewModel item = new TransactionViewModel(categoryName,date, amount,type);
            transactionViewModels.add(item);

        }
    }

    /**
     *
     * @param data
     */
    private void updateTotals(List<Transaction> data) {
        Pair<Double,Double> res = TransactionUtil.calcTotal(data);
        _totalExpenses = NumbersUtil.round(res.second,2);
        _totalIncome = NumbersUtil.round(res.first,2);
       _totalBalance = NumbersUtil.round(_totalIncome - _totalExpenses,2);
    }

    /**
     * Initialize the calendat and the its onClick
     */
    public void initializeCalendar() {

        //calendar = (CalendarView) findViewById(R.id.calendar);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,9);
        cal.set(Calendar.YEAR,2015);
        cal.set(Calendar.DATE,1);

        calendar.setMinDate(cal.getTimeInMillis());

        cal.set(Calendar.DATE, 31);
        calendar.setMaxDate(cal.getTimeInMillis());

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(1);

        //calendar.setDateTextAppearance(R.color.green);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        //calendar.setSelectedDateVerticalBar(R.color.darkgreen);

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                if(_dayDetails.getVisibility() == View.VISIBLE) {
                    _dayDetails.setVisibility(View.GONE);
                } else {
                    _dayDetails.setVisibility(View.VISIBLE);
                }

            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        //getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        return true;
//    }

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
