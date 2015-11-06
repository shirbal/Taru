package com.taru.taru;

import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.taru.taru.citi.utils.APICreator;
import com.taru.taru.citi.utils.RestApiCaller;
import com.taru.taru.citi.utils.UrlCreator;
import com.taru.taru.models.Transaction;
import com.taru.taru.models.enums.TransactionType;
import com.taru.taru.utils.DateUtils;
import com.taru.taru.utils.NumbersUtil;
import com.taru.taru.utils.TransactionUtil;
import com.taru.taru.views.DailyTransactionAdapter;
import com.taru.taru.views.DailyTransactionViewModel;
import com.taru.taru.views.TextsClickListener;
import com.taru.taru.views.TransactionAdapter;
import com.taru.taru.views.TransactionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends Activity {

    public static final String OTHER_CATEGORY = "Other";
    private LinearLayout _mainLayout;
    private LinearLayout _progressBarLayout;
    private LinearLayout _enterAmountLayout;
    private LinearLayout _enterAmountDailyLayout;
    private LinearLayout _monthTotalsLayout;
    private LinearLayout _monthHeaderLayout;

    private ImageView _findADay;
    private ImageView _dailyExpenses;
    private ImageView _dailtIncome;


    private ListView _transactionsViewList;
    private ListView _dailyTransactionViewList;

    private EditText _enterAmount;
    private EditText _enterAmountDaily;


    private Context _cont;
    private CalendarView calendar;
    private LinearLayout _dayDetails;

    private TextView _totalExpensesView;
    private TextView _totalIncomeView;
    private TextView _totalBalanceView;
    private TextView _dailyTotal;
    private TextView _selectedDate;
    private TextView _balanceUpToToday;

    private Double _totalExpenses;
    private Double _totalIncome;
    private Double _totalBalance;

    private Map<String, Pair<Double,List<DailyTransactionViewModel>>> _daysToTransactions = new HashMap<>();
    private Map<String,Double> _dateToBalance = new HashMap<>();
    private Map<String, Pair<Double,List<DailyTransactionViewModel>>> _addedTransactions = new HashMap<>();

    private TextsClickListener hiderListener;

    private boolean _foundADay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        _cont = this;
        initializeLayouts();
        initializeTextViews();
        initializeAdapters();
        initializeButtons();
        initializeEditText();
        initializeListeners();

        initializeCalendar();
        getDataOnBackground();




    }


    private void initializeEditText() {
        _enterAmount = (EditText)findViewById(R.id.enter_amount);
        _enterAmountDaily = (EditText)findViewById(R.id.enter_amount_daily);

    }

    private void initializeLayouts() {
        _dayDetails = (LinearLayout)findViewById(R.id.day_details_lay);
       // _transactionsViewList = (ListView)findViewById(R.id.listView1);
        _dailyTransactionViewList = (ListView)findViewById(R.id.daily_list);
        _mainLayout = (LinearLayout)findViewById(R.id.main_layout);
        _progressBarLayout = (LinearLayout)findViewById(R.id.progress_bar_layout);
        _enterAmountLayout =  (LinearLayout)findViewById(R.id.enter_amount_layout);
       _monthTotalsLayout =  (LinearLayout)findViewById(R.id.month_header_layout);
        _monthHeaderLayout =  (LinearLayout)findViewById(R.id.totals_layout);
        _enterAmountDailyLayout = (LinearLayout)findViewById(R.id.enter_amount_daily_layout);
    }

    private void initializeTextViews() {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Regular.ttf");
        _totalExpensesView = (TextView)findViewById(R.id.total_expenses);
        _totalExpensesView.setTypeface(face);
        _totalIncomeView = (TextView)findViewById(R.id.total_income);
        _totalIncomeView.setTypeface(face);
        _totalBalanceView = (TextView)findViewById(R.id.total_balance);
        _totalBalanceView.setTypeface(face);
        _balanceUpToToday = (TextView)findViewById(R.id.balance_up_today);
        _balanceUpToToday.setTypeface(face);
        _dailyTotal = (TextView)findViewById(R.id.daily_total);
        _dailyTotal.setTypeface(face);
        _selectedDate = (TextView)findViewById(R.id.date_selected);
    }

    private void initializeButtons() {
        _findADay = (ImageView)findViewById(R.id.amount_to_spend_button);
        initializeClickListenerForFindADay();

        _dailyExpenses = (ImageView)findViewById(R.id.daily_expenses);
        _dailtIncome = (ImageView)findViewById(R.id.daily_income);

        _dailyExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = _selectedDate.getText().toString();
                date = DateUtils.getDateFromRep(date);
                createExternalTransaction(TransactionType.OUT, _enterAmountDaily, date);
                hiderListener.onClick(v);
            }
        });

        _dailtIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = _selectedDate.getText().toString();
                date = DateUtils.getDateFromRep(date);
                createExternalTransaction(TransactionType.IN, _enterAmountDaily, date);
                hiderListener.onClick(v);
            }
        });


    }

    private void initializeClickListenerForFindADay() {
        _findADay.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (_foundADay) { // Add to plan button appear
                    String dayWithMaxBalance = getDayWithMaxBalance();
                    createExternalTransaction(TransactionType.OUT,_enterAmount,dayWithMaxBalance);
                    // need to change back to find a day
                    _findADay.setImageResource(R.drawable.findaday);
                    _foundADay = false;

                } else { // Find a day button appear

                    if (_enterAmount.getText() == null || _enterAmount.getText().length() == 0) {
                        Toast.makeText(MainActivity.this, "Please enter amount first", Toast.LENGTH_SHORT).show();
                    } else {
                        String dayWithMaxBalance = getDayWithMaxBalance();
                        //TODO: need to display this date
                        // displaySelectedDay;
                        // needs to change to addtoplan
                        _findADay.setImageResource(R.drawable.addtoplan);
                        _foundADay = true;

                    }


                }

            }
        });
    }

    private void createExternalTransaction(TransactionType type,EditText amountToReadFrom, String date) {
        DailyTransactionViewModel newViewModel = createDailyViewModel(amountToReadFrom,type);
        addToExternalTransaction(date, newViewModel);
        updateTotalsWithExternalExpenses(newViewModel,type);
        updateBalanceWithExternalTransaction(date, newViewModel, type);
        amountToReadFrom.setText("");
    }

    private void updateBalanceWithExternalTransaction(String date, DailyTransactionViewModel newViewModel, TransactionType type) {
        int current = DateUtils.getDay(date);
        int numberOfDays = DateUtils.numberOfDays(date);
        int year = DateUtils.getYear(date);
        int month = DateUtils.getMonth(date);
        for(int i = current; i <= numberOfDays; i++) {
           String currentDate = DateUtils.createDateAsString(year,month,i);
            Double currentBalance = _dateToBalance.get(currentDate);
            if (type == TransactionType.OUT) {
                currentBalance -= newViewModel.getAmount();
            } else {
                currentBalance += newViewModel.getAmount();
            }
            _dateToBalance.put(currentDate,currentBalance);
        }

    }

    private void updateTotalsWithExternalExpenses(DailyTransactionViewModel newViewModel,
                                                  TransactionType type) {
        if(type == TransactionType.OUT) {
            _totalExpenses += newViewModel.getAmount();
            _totalExpenses = NumbersUtil.round(_totalExpenses, 2);
            _totalExpensesView.setText(_totalExpenses.toString());
        } else {
            _totalIncome += newViewModel.getAmount();
            _totalIncome = NumbersUtil.round(_totalIncome, 2);
            _totalIncomeView.setText(_totalIncome.toString());
        }
        // update total balance
        _totalBalance = NumbersUtil.round(_totalIncome - _totalExpenses,2);
        _totalBalanceView.setText(_totalBalance.toString());

    }

    private DailyTransactionViewModel createDailyViewModel(EditText amountToReadFrom, TransactionType type) {
        String amountStr = amountToReadFrom.getText().toString();
        Double amount = Double.valueOf(amountStr);
        return new DailyTransactionViewModel(OTHER_CATEGORY,amount,type);
    }

    private void addToExternalTransaction(String date, DailyTransactionViewModel newViewModel) {

        Pair<Double,List<DailyTransactionViewModel>> pair = this._addedTransactions.get(date);

        List<DailyTransactionViewModel> list;
        Double total;
        if(pair == null) {
            total = 0.0;
            list = new ArrayList<>();
        } else {
            total = pair.first;
            list = pair.second;
        }
        list.add(newViewModel);

        if(newViewModel.getType() == TransactionType.OUT) {
            total -= newViewModel.getAmount();
        } else {
            total += newViewModel.getAmount();
        }

        total = NumbersUtil.round(total, 2);
        _addedTransactions.put(date,new Pair<>(total,list));
    }

    private void initializeListeners() {
        hiderListener =
                new TextsClickListener(_dayDetails,_enterAmountLayout,_enterAmountDailyLayout,_enterAmountDaily);

        _monthHeaderLayout.setOnClickListener(hiderListener);
        _monthTotalsLayout.setOnClickListener(hiderListener);

    }

    private void initializeAdapters() {
        List<DailyTransactionViewModel> data = new ArrayList<>();
        DailyTransactionAdapter dailyTransactionAdapter =
                new DailyTransactionAdapter(_cont,R.layout.day_details,data);
        _dailyTransactionViewList.setAdapter(dailyTransactionAdapter);

        List<TransactionViewModel> mainData = new ArrayList<>();
        TransactionAdapter adapter =
                new TransactionAdapter(_cont, R.layout.transaction_template, mainData);
        //_transactionsViewList.setAdapter(adapter);


    }

    private void getDataOnBackground() {
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final List<TransactionViewModel> res = createData();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                _totalBalanceView.setText(_totalBalance.toString());
                                _totalExpensesView.setText(_totalExpenses.toString());
                                _totalIncomeView.setText(_totalIncome.toString());

                                _progressBarLayout.setVisibility(View.GONE);
                                _mainLayout.setVisibility(View.VISIBLE);

                                //TransactionAdapter adapter = (TransactionAdapter)_transactionsViewList.getAdapter();
                                //adapter.addAll(res);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<TransactionViewModel> createData() {
        /**
         * update the view models to display the list of transation
         */
        ArrayList<TransactionViewModel> transactionViewModels = new ArrayList<>();
        List<Transaction> data = getDataFromRest();

        if (data != null) {
            /**
             * update the total views
             */
            updateTotals(data);
            /**
             * Update the map before displaying it
             */
            TransactionUtil.updateDaysToTransactionMap(data, _daysToTransactions,_dateToBalance);
            fillTransactionModelsList(transactionViewModels, data);
        }
        return transactionViewModels;
    }

    private List<Transaction> getDataFromRest() {
        UrlCreator url = new UrlCreator();
        url.append(UrlCreator.TRANSACTINOS);
        return RestApiCaller.getInstance().getData(url, APICreator.GET_METHOD);
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

        calendar = (CalendarView) findViewById(R.id.calendar);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH,8);
        cal.set(Calendar.YEAR,2015);
        cal.set(Calendar.DATE,1);

        calendar.setMinDate(cal.getTimeInMillis());

        cal.set(Calendar.DATE, 30);
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
                _enterAmountDaily.setText("");
                _enterAmountDailyLayout.setVisibility(View.VISIBLE);
                _enterAmountLayout.setVisibility(View.GONE);
                String date = DateUtils.createDateAsString(year, month+1, day);
                Pair<Double, List<DailyTransactionViewModel>> doubleListPair = _daysToTransactions.get(date);

                DailyTransactionAdapter adapter =
                        (DailyTransactionAdapter) _dailyTransactionViewList.getAdapter();

                // add external transactions
                Pair<Double,List<DailyTransactionViewModel>> transactionViewModels = _addedTransactions.get(date);
                adapter.clear();
                updateViewWithOnSelectedDate(doubleListPair, adapter);
                updateViewWithOnSelectedDate(transactionViewModels, adapter);

                String dateToPresent = DateUtils.getDateForPresentation(year,month+1,day);
                _selectedDate.setText(dateToPresent);
                _balanceUpToToday.setText(_dateToBalance.get(date).toString());
                _dayDetails.setVisibility(View.VISIBLE);

            }
        });
    }

    private void updateViewWithOnSelectedDate(Pair<Double, List<DailyTransactionViewModel>> doubleListPair,
                                              DailyTransactionAdapter adapter) {
        try {
            CharSequence text = _dailyTotal.getText();
            double total = 0.0;
            if(text != null && text.length() > 0) {
                total += Double.valueOf(text.toString());
            }
            if (doubleListPair != null) {
                adapter.addAll(doubleListPair.second);
                double newTotal = doubleListPair.first + total;
                newTotal = NumbersUtil.round(newTotal,2);
                _dailyTotal.setText(String.valueOf(newTotal));
            } else {
                _dailyTotal.setText(String.valueOf(total));

            }
        } catch (NumberFormatException e) {
            Log.e("Taru.Exception","on updateViewWithOnSelectedDate: " + e.getMessage());
        }
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

    private String getDayWithMaxBalance() {
        String result = null;
        double max = 0;
        Set<Map.Entry<String, Double>> entries = _dateToBalance.entrySet();
        Iterator<Map.Entry<String, Double>> iterator = entries.iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            if(max < next.getValue()) {
                result = next.getKey();
            }
        }
        return result;
    }


    private List<Transaction> getMockedTransactions() {
         List<Transaction> result = new ArrayList<>();
         result.add(new Transaction("Groceries","9/1/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/3/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/4/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/7/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/8/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/10/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/14/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/15/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/16/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/21/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/23/2015",100.0,TransactionType.OUT));
         result.add(new Transaction("Groceries","9/25/2015",100.0,TransactionType.OUT));




        return result;
    }

}
