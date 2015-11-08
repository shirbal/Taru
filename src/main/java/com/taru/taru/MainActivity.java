package com.taru.taru;

import android.app.Activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
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
import com.taru.taru.utils.CalendarHelper;
import com.taru.taru.utils.DateUtils;
import com.taru.taru.utils.NumbersUtil;
import com.taru.taru.utils.TransactionUtil;
import com.taru.taru.vdesmet.lib.calendar.OnDayClickListener;
import com.taru.taru.views.CustomDayAdapter;
import com.taru.taru.views.DailyTransactionAdapter;
import com.taru.taru.views.DailyTransactionViewModel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends Activity implements OnDayClickListener {

    public static final String OTHER_CATEGORY = "Other";
    private LinearLayout _mainLayout;
    private LinearLayout _progressBarLayout;
    private LinearLayout _enterAmountLayout;
    private LinearLayout _enterAmountDailyLayout;
    private LinearLayout _monthTotalsLayout;
    private LinearLayout _monthHeaderLayout;
    private LinearLayout _dayDetailsLayout;

    private ImageView _findADay;
    private ImageView _dailyExpenses;
    private ImageView _dailyIncome;

    private ListView _dailyTransactionViewList;

    private EditText _enterAmount;
    private EditText _enterAmountDaily;

    private Context _cont;

    private TextView _totalExpensesView;
    private TextView _totalIncomeView;
    private TextView _totalBalanceView;
    private TextView _dailyTotal;
    private TextView _selectedDate;
    private TextView _balanceUpToToday;

    private Double _totalExpenses;
    private Double _totalIncome;
    private Double _totalBalance;


    private Map<String, Pair<Double, List<DailyTransactionViewModel>>> _daysToTransactions = new HashMap<>();
    private Map<String, Double> _dateToBalance = new HashMap<>();
    private Map<String, Pair<Double, List<DailyTransactionViewModel>>> _addedTransactions = new HashMap<>();

    private Long lastDaySelected = null;
    private Typeface numbersTypeface;
    private Typeface lettersTypeface;
    private boolean _foundADay = false;
    private com.taru.taru.vdesmet.lib.calendar.CalendarView singleMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _cont = this;
        numbersTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Regular.ttf");
        lettersTypeface = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro/SourceSansPro-Regular.otf");
        initializeNewCalendar();
        initializeLayouts();
        initializeTextViews();
        initializeAdapters();
        initializeButtons();
        initializeEditText();
        initializeListeners();
        getDataOnBackground();
        initializeFonts();
    }

    private void initializeFonts() {

        TextView viewById = (TextView) findViewById(R.id.month_header_month);
        viewById.setTypeface(lettersTypeface);
        viewById = (TextView) findViewById(R.id.total_balance_header);
        viewById.setTypeface(lettersTypeface);
        viewById = (TextView) findViewById(R.id.amount_to_spend_header);
        viewById.setTypeface(lettersTypeface);
        viewById = (TextView) findViewById(R.id.total_expenses_header);
        viewById.setTypeface(lettersTypeface);
        viewById = (TextView) findViewById(R.id.total_income_header);
        viewById.setTypeface(lettersTypeface);
        viewById = (TextView) findViewById(R.id.enter_amount_daily);
        viewById.setTypeface(lettersTypeface);
        viewById = (TextView) findViewById(R.id.balance_today_header);
        viewById.setTypeface(lettersTypeface);
    }

    /**
     * Initialize the calendar view, clickListener and dayAdapter
     */
    private void initializeNewCalendar() {
        // Retrieve the CalendarView
        singleMonth =
                (com.taru.taru.vdesmet.lib.calendar.CalendarView) findViewById(R.id.single_calendar);
        // Set the first valid day
        final Calendar firstValidDay = Calendar.getInstance();
        firstValidDay.set(Calendar.YEAR, 2015);
        firstValidDay.set(Calendar.MONTH, 8);
        firstValidDay.set(Calendar.DATE, 1);
        singleMonth.setFirstValidDay(firstValidDay);
        singleMonth.setOnDayClickListener(this);
        CustomDayAdapter newAdapter = new CustomDayAdapter();
        singleMonth.setDayAdapter(newAdapter);
    }

    private void initializeEditText() {
        _enterAmount = (EditText) findViewById(R.id.enter_amount);
        _enterAmountDaily = (EditText) findViewById(R.id.enter_amount_daily);

    }

    private void initializeLayouts() {
        _dayDetailsLayout = (LinearLayout) findViewById(R.id.day_details_lay);
        _dailyTransactionViewList = (ListView) findViewById(R.id.daily_list);
        _mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        _progressBarLayout = (LinearLayout) findViewById(R.id.progress_bar_layout);
        _enterAmountLayout = (LinearLayout) findViewById(R.id.enter_amount_layout);
        _monthTotalsLayout = (LinearLayout) findViewById(R.id.month_header_layout);
        _monthHeaderLayout = (LinearLayout) findViewById(R.id.totals_layout);
        _enterAmountDailyLayout = (LinearLayout) findViewById(R.id.enter_amount_daily_layout);
    }

    private void initializeTextViews() {
        _totalExpensesView = (TextView) findViewById(R.id.total_expenses);
        _totalExpensesView.setTypeface(numbersTypeface);
        _totalIncomeView = (TextView) findViewById(R.id.total_income);
        _totalIncomeView.setTypeface(numbersTypeface);
        _totalBalanceView = (TextView) findViewById(R.id.total_balance);
        _totalBalanceView.setTypeface(numbersTypeface);
        _balanceUpToToday = (TextView) findViewById(R.id.balance_up_today);
        _balanceUpToToday.setTypeface(numbersTypeface);
        _dailyTotal = (TextView) findViewById(R.id.daily_total);
        _dailyTotal.setTypeface(numbersTypeface);
        _selectedDate = (TextView) findViewById(R.id.date_selected);
        _selectedDate.setTypeface(numbersTypeface);
    }

    private void initializeButtons() {
        _findADay = (ImageView) findViewById(R.id.amount_to_spend_button);

        _dailyExpenses = (ImageView) findViewById(R.id.daily_expenses);
        _dailyIncome = (ImageView) findViewById(R.id.daily_income);

    }

    private void initializeListeners() {
        initializeClickListenerForFindADay();

        _monthHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDailyTransactionDetails();
            }
        });
        _monthTotalsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDailyTransactionDetails();
            }
        });

        _dailyExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDailyTransactionClick(v, TransactionType.OUT);
            }
        });

        _dailyIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDailyTransactionClick(v, TransactionType.IN);
            }
        });

    }

    private void initializeAdapters() {
        List<DailyTransactionViewModel> data = new ArrayList<>();
        DailyTransactionAdapter dailyTransactionAdapter =
                new DailyTransactionAdapter(_cont, R.layout.day_details, data);
        _dailyTransactionViewList.setAdapter(dailyTransactionAdapter);
    }

    private void initializeClickListenerForFindADay() {

        _findADay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (_foundADay) { // Add to plan button appear
                    String dayWithMaxBalance = getDayWithMaxBalance();
                    createExternalTransaction(TransactionType.OUT, _enterAmount, dayWithMaxBalance);
                    // need to change back to find a day
                    _findADay.setImageResource(R.drawable.findaday);
                    long dateAsMilli = DateUtils.getDateAsMilli(dayWithMaxBalance);
                    unMarkDate(dateAsMilli);
                    markDateChanged(dateAsMilli);
                    _foundADay = false;

                } else { // Find a day button appear

                    if (_enterAmount.getText() == null || _enterAmount.getText().length() == 0) {
                        Toast.makeText(MainActivity.this, "Please enter amount first", Toast.LENGTH_SHORT).show();
                    } else {
                        String dayWithMaxBalance = getDayWithMaxBalance();
                        //TODO: need to display this date
                        // displaySelectedDay;
                        long dateAsMilli = DateUtils.getDateAsMilli(dayWithMaxBalance);
                        markDate(dateAsMilli);
                        // needs to change to addtoplan
                        _findADay.setImageResource(R.drawable.addtoplan);
                        _foundADay = true;

                    }
                }
            }
        });
    }

    private void markDateChanged(long dateAsMilli) {
        View lineForDate = singleMonth.getLineForDate(dateAsMilli);
        if(lineForDate != null) {
            lineForDate.setBackgroundColor(getColor(R.color.orange));
        }
    }

    private void createExternalTransaction(TransactionType type, EditText amountToReadFrom, String date) {
        DailyTransactionViewModel newViewModel = createDailyViewModel(amountToReadFrom, type);
        addToExternalTransaction(date, newViewModel);
        updateTotalsWithExternalExpenses(newViewModel, type);
        updateBalanceWithExternalTransaction(date, newViewModel, type);
        updateDayOnCalendar(date, newViewModel);
        amountToReadFrom.setText("");
    }

    private void updateDayOnCalendar(String date, DailyTransactionViewModel newViewModel) {

        long timeInMillis = DateUtils.getDateAsMilli(date);
        TextView textViewForDate = singleMonth.getTextViewForDate(timeInMillis);

        Pair<Double, List<DailyTransactionViewModel>> doubleListPair = _daysToTransactions.get(date);
        Pair<Double, List<DailyTransactionViewModel>> doubleListPair1 = _addedTransactions.get(date);

        double total = 0.0;
        if (doubleListPair != null) {
            total += doubleListPair.first;
        }
        if (doubleListPair1 != null) {
            total += doubleListPair1.first;
        }

        if (textViewForDate != null) {
            if (total < 0) {
                textViewForDate.setTextColor(getColor(R.color.red));
            } else {
                textViewForDate.setTextColor(getColor(R.color.green));
            }
            total = (total < 0) ? total * (-1.0) : total;
            setPrice(textViewForDate, total);
            //textViewForDate.setText(String.valueOf(total));
            textViewForDate.setTextSize(10);
            TextView dateNumber = singleMonth.getDateNumber(timeInMillis);
            if (dateNumber != null) {
                dateNumber.setTypeface(dateNumber.getTypeface(), Typeface.BOLD);
            }
        }

    }

    private void updateBalanceWithExternalTransaction(String date, DailyTransactionViewModel newViewModel, TransactionType type) {
        int current = DateUtils.getDay(date);
        int numberOfDays = DateUtils.numberOfDays(date);
        int year = DateUtils.getYear(date);
        int month = DateUtils.getMonth(date);
        for (int i = current; i <= numberOfDays; i++) {
            String currentDate = DateUtils.createDateAsString(year, month, i);
            Double currentBalance = _dateToBalance.get(currentDate);
            if (type == TransactionType.OUT) {
                currentBalance -= newViewModel.getAmount();
            } else {
                currentBalance += newViewModel.getAmount();
            }
            _dateToBalance.put(currentDate, currentBalance);
        }

    }

    private void updateTotalsWithExternalExpenses(DailyTransactionViewModel newViewModel,
                                                  TransactionType type) {
        if (type == TransactionType.OUT) {
            _totalExpenses += newViewModel.getAmount();
            _totalExpenses = NumbersUtil.round(_totalExpenses, 2);
            setPrice(_totalExpensesView, _totalExpenses);
           // _totalExpensesView.setText(_totalExpenses.toString());
        } else {
            _totalIncome += newViewModel.getAmount();
            _totalIncome = NumbersUtil.round(_totalIncome, 2);
            setPrice(_totalIncomeView, _totalIncome);
           // _totalIncomeView.setText(_totalIncome.toString());
        }
        // update total balance
        _totalBalance = NumbersUtil.round(_totalIncome - _totalExpenses, 2);
        setPrice(_totalBalanceView, _totalBalance);
       // _totalBalanceView.setText(_totalBalance.toString());

    }

    private DailyTransactionViewModel createDailyViewModel(EditText amountToReadFrom, TransactionType type) {
        String amountStr = amountToReadFrom.getText().toString();
        Double amount = Double.valueOf(amountStr);
        return new DailyTransactionViewModel(OTHER_CATEGORY, amount, type);
    }

    private void addToExternalTransaction(String date, DailyTransactionViewModel newViewModel) {

        Pair<Double, List<DailyTransactionViewModel>> pair = this._addedTransactions.get(date);

        List<DailyTransactionViewModel> list;
        Double total;
        if (pair == null) {
            total = 0.0;
            list = new ArrayList<>();
        } else {
            total = pair.first;
            list = pair.second;
        }
        list.add(newViewModel);

        if (newViewModel.getType() == TransactionType.OUT) {
            total -= newViewModel.getAmount();
        } else {
            total += newViewModel.getAmount();
        }

        total = NumbersUtil.round(total, 2);
        _addedTransactions.put(date, new Pair<>(total, list));
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

                    createData();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                               setPrice(_totalBalanceView,_totalBalance);
                                setPrice(_totalExpensesView,_totalExpenses);
                                setPrice(_totalIncomeView,_totalIncome);

//                                _totalBalanceView.setText(_totalBalance.toString());
//                                _totalExpensesView.setText(_totalExpenses.toString());
//                                _totalIncomeView.setText(_totalIncome.toString());

                                _progressBarLayout.setVisibility(View.GONE);
                                _mainLayout.setVisibility(View.VISIBLE);
                                CalendarHelper.fillCalendarWithData(getBaseContext(), singleMonth, _daysToTransactions);
                                _enterAmount.requestFocus();
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

    private void createData() {
        /**
         * update the view models to display the list of transaction
         */
        List<Transaction> data = getDataFromRest();

        if (data != null) {
            /**
             * update the total views
             */
            updateTotals(data);
            /**
             * Update the map before displaying it
             */
            TransactionUtil.updateDaysToTransactionMap(data, _daysToTransactions, _dateToBalance);
        }
    }

    private List<Transaction> getDataFromRest() {
        UrlCreator url = new UrlCreator();
        url.append(UrlCreator.TRANSACTINOS);
        return RestApiCaller.getInstance().getData(this);
        //return RestApiCaller.getInstance().getData(url, APICreator.GET_METHOD);
    }

    /**
     * @param data
     */
    private void updateTotals(List<Transaction> data) {
        Pair<Double, Double> res = TransactionUtil.calcTotal(data);
        _totalExpenses = NumbersUtil.round(res.second, 2);
        _totalIncome = NumbersUtil.round(res.first, 2);
        _totalBalance = NumbersUtil.round(_totalIncome - _totalExpenses, 2);
    }

    private void updateViewWithOnSelectedDate(Pair<Double, List<DailyTransactionViewModel>> doubleListPair,
                                              DailyTransactionAdapter adapter) {
        try {
            CharSequence text = _dailyTotal.getText();
            double total = 0.0;
            if (text != null && text.length() > 0) {
                total += Double.valueOf(getTextWIthDollar(text));
            }
            if (doubleListPair != null) {
                adapter.addAll(doubleListPair.second);
                double newTotal = doubleListPair.first + total;
                newTotal = NumbersUtil.round(newTotal, 2);
                setPrice(_dailyTotal, newTotal);
                //_dailyTotal.setText(String.valueOf(newTotal));
            } else {
                //_dailyTotal.setText(String.valueOf(total));
                setPrice(_dailyTotal,total);
            }
        } catch (NumberFormatException e) {
            Log.e("Taru.Exception", "on updateViewWithOnSelectedDate: " + e.getMessage());
        }
    }

    @NonNull
    private String getTextWIthDollar(CharSequence text) {
        char first = text.charAt(0);
        if(first == '$') {
            return text.toString().substring(1);
        }

        return "-" + text.toString().substring(2);

    }

    private String getDayWithMaxBalance() {
        String result = null;
        double max = 0;
        Set<Map.Entry<String, Double>> entries = _dateToBalance.entrySet();
        Iterator<Map.Entry<String, Double>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            if (max < next.getValue()) {
                result = next.getKey();
                max = next.getValue();
            }
        }
        return result;
    }

    private List<Transaction> getMockedTransactions() {
        List<Transaction> result = new ArrayList<>();
        result.add(new Transaction("Groceries", "9/1/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/3/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/4/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/7/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/8/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/10/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/14/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/15/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/16/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/21/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/23/2015", 100.0, TransactionType.OUT));
        result.add(new Transaction("Groceries", "9/25/2015", 100.0, TransactionType.OUT));


        return result;
    }

    private void markDate(long dateAsMilli) {
        //TextView textViewForDate = singleMonth.getTextViewForDate(dateAsMilli);
        View layoutForDate = singleMonth.getLayoutForDate(dateAsMilli);
        if (layoutForDate != null) {
            layoutForDate.setBackgroundResource(R.drawable.circ);
        }
    }

    private void unMarkDate(long dateAsMilli) {
        //TextView textViewForDate = singleMonth.getTextViewForDate(dateAsMilli);
        View layoutForDate = singleMonth.getLayoutForDate(dateAsMilli);
        if (layoutForDate != null) {
            layoutForDate.setBackgroundResource(R.color.transparent);
        }
    }

    @Override
    public void onDayClick(long dayInMillis) {
        if (lastDaySelected != null) {
            unMarkDate(lastDaySelected);
        }
        lastDaySelected = dayInMillis;
        markDate(dayInMillis);

        Date chosen = new Date(dayInMillis);
        int day = chosen.getDate();
        int month = chosen.getMonth();
        int year = chosen.getYear() + 1900;

        _enterAmount.setText("");
        _enterAmountDaily.setText("");
        _enterAmountDailyLayout.setVisibility(View.VISIBLE);
        _enterAmountLayout.setVisibility(View.GONE);
        String date = DateUtils.createDateAsString(year, month + 1, day);
        Pair<Double, List<DailyTransactionViewModel>> doubleListPair = _daysToTransactions.get(date);

        DailyTransactionAdapter adapter =
                (DailyTransactionAdapter) _dailyTransactionViewList.getAdapter();

        // add external transactions
        Pair<Double, List<DailyTransactionViewModel>> transactionViewModels = _addedTransactions.get(date);
        adapter.clear();
        _dailyTotal.setText("");
        updateViewWithOnSelectedDate(doubleListPair, adapter);
        updateViewWithOnSelectedDate(transactionViewModels, adapter);

        String dateToPresent = DateUtils.getDateForPresentation(year, month + 1, day);
        _selectedDate.setText(dateToPresent);
        setPrice(_balanceUpToToday,_dateToBalance.get(date));
        //_balanceUpToToday.setText(_dateToBalance.get(date).toString());
        _dayDetailsLayout.setVisibility(View.VISIBLE);
        _enterAmountDaily.requestFocus();

    }

    private void clearMarkFromPreviousDay() {
        unMarkDate(lastDaySelected);
        lastDaySelected = null;
    }

    private void onDailyTransactionClick(View v, TransactionType in) {
        String date = _selectedDate.getText().toString();
        date = DateUtils.getDateFromRep(date);
        createExternalTransaction(in, _enterAmountDaily, date);
        markDateChanged(lastDaySelected);
        clearDailyTransactionDetails();
    }

    private void clearDailyTransactionDetails() {
        _dayDetailsLayout.setVisibility(View.GONE);
        _enterAmountLayout.setVisibility(View.VISIBLE);
        _enterAmountDailyLayout.setVisibility(View.GONE);
        _enterAmount.requestFocus();
        _enterAmountDaily.setText("");
        _enterAmount.setText("");
        clearMarkFromPreviousDay();
    }

    private void setPrice(TextView text, Double amount) {
        int number = amount.intValue();
        StringBuilder str = new StringBuilder();
        if(number < 0) {
            str.append("-$");
        } else {
            str.append("$");
        }
        number = Math.abs(number);
        str.append(number);
        text.setText(str.toString());
    }


}
