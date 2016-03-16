package com.dg.app.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.dg.app.R;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.OnClick;

/**
 * Created by lenovo on 2015/9/28.
 */
public class DatePickerDialog extends AlertDialog implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private static final String START_YEAR="start_year";
    private static final String START_MONTH="start_month";
    private static final String START_DAY="start_day";

    private final DatePicker mDatePicker_start;
    private final OnDateSetListener mCallBack;

    Button cancel;
    Button enter;

    int year;
    int month;
    int day;



    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {


        void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth);
    }

    /**
     * @param context
     *            The context the dialog is to run in.
     * @param callBack
     *            How the parent is notified that the date is set.
     * @param year
     *            The initial year of the dialog.
     * @param monthOfYear
     *            The initial month of the dialog.
     * @param dayOfMonth
     *            The initial day of the dialog.
     */
    public DatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth);

        this.year=year;
        this.month=monthOfYear;
        this.day=dayOfMonth;
    }

    public DatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
                                  int dayOfMonth) {
        this(context, 0, callBack, year, monthOfYear, dayOfMonth, true);
        this.year=year;
        this.month=monthOfYear;
        this.day=dayOfMonth;
    }

    /**
     * @param context
     *            The context the dialog is to run in.
     * @param theme
     *            the theme to apply to this dialog
     * @param callBack
     *            How the parent is notified that the date is set.
     * @param year
     *            The initial year of the dialog.
     * @param monthOfYear
     *            The initial month of the dialog.
     * @param dayOfMonth
     *            The initial day of the dialog.
     */
    public DatePickerDialog(Context context, int theme, OnDateSetListener callBack, int year, int monthOfYear,
                                  int dayOfMonth, boolean isDayVisible) {
        super(context, theme);

        this.year=year;
        this.month=monthOfYear;
        this.day=dayOfMonth;
        mCallBack = callBack;

        Context themeContext = getContext();
        setIcon(0);

        LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.customdialog_jiyang_start, null);
        setView(view);
        try {
            cancel = (Button) view.findViewById(R.id.jiyang_start_cancel);
            enter = (Button) view.findViewById(R.id.jiyang_start_enter);
            cancel.setOnClickListener(this);
            enter.setOnClickListener(this);


        } catch (Exception ex)
        {
            ex.printStackTrace();
        }


        mDatePicker_start = (DatePicker) view.findViewById(R.id.jiyang_start_picker);
        mDatePicker_start.init(year, monthOfYear, dayOfMonth, this);
        // updateTitle(year, monthOfYear, dayOfMonth);

        // 如果要隐藏当前日期，则使用下面方法。
        if (!isDayVisible) {
            hidDay(mDatePicker_start);
        }
    }

    /**
     * 隐藏DatePicker中的日期显示
     *
     * @param mDatePicker
     */
    private void hidDay(DatePicker mDatePicker) {
    Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
        for (Field datePickerField : datePickerfFields) {
            if ("mDaySpinner".equals(datePickerField.getName())) {
                datePickerField.setAccessible(true);
                Object dayPicker = new Object();
                try {
                    dayPicker = datePickerField.get(mDatePicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                // datePicker.getCalendarView().setVisibility(View.GONE);
                ((View) dayPicker).setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        if (view.getId() == R.id.jiyang_start_picker)
            mDatePicker_start.init(year, month, day, this);


        // updateTitle(year, month, day);
    }

    /**
     * 获得开始日期的DatePicker
     *
     * @return The calendar view.
     */
    public DatePicker getDatePickerStart() {
        return mDatePicker_start;
    }


    /**
     * Sets the start date.
     *
     * @param year
     *            The date year.
     * @param monthOfYear
     *            The date month.
     * @param dayOfMonth
     *            The date day of month.
     */
    public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
    }



    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mDatePicker_start.clearFocus();

            mCallBack.onDateSet(mDatePicker_start, mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
                    mDatePicker_start.getDayOfMonth());
        }
    }

    @Override
    protected void onStop() {
        // tryNotifyDateSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(START_YEAR, mDatePicker_start.getYear());
        state.putInt(START_MONTH, mDatePicker_start.getMonth());
        state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());

        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int start_year = savedInstanceState.getInt(START_YEAR);
        int start_month = savedInstanceState.getInt(START_MONTH);
        int start_day = savedInstanceState.getInt(START_DAY);
        mDatePicker_start.init(start_year, start_month, start_day, this);

    }
    @Override
    public void onClick(View view)
    {
          if(view.getId()==R.id.jiyang_start_enter)
          {
              Calendar c1=Calendar.getInstance();
              c1.setTime(new Date(year,month,day));
              Calendar c2=Calendar.getInstance();
              c2.setTime(new Date(mDatePicker_start.getYear(),
                      mDatePicker_start.getMonth(),mDatePicker_start.getDayOfMonth()));
              if(c1.compareTo(c2)>0)
              {
                  Toast.makeText(this.getContext(),"时间小于当前时间或开始时间",Toast.LENGTH_LONG).show();
              }

              else {
                  tryNotifyDateSet();
                  dismiss();}
          }
        if(view.getId()==R.id.jiyang_start_cancel)
        {
            dismiss();
        }
    }




}
