package com.hui.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hui.tally.R;
import com.hui.tally.adapter.CalendarAdapter;
import com.hui.tally.db.DBManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {
    ImageView errorIv;
    GridView gv;
    LinearLayout hsvLayout;

    List<TextView>hsvViewList;
    List<Integer>yearList;

    int selectPos = -1;
    private CalendarAdapter adapter;
    int selectMonth = -1;

    public interface OnRefreshListener{
        public void onRefresh(int selPos,int year,int month);
    }
    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CalendarDialog(@NonNull Context context,int selectPos,int selectMonth) {
        super(context);
        this.selectPos = selectPos;
        this.selectMonth = selectMonth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        gv = findViewById(R.id.dialog_calendar_gv);
        errorIv = findViewById(R.id.dialog_calendar_iv);
        hsvLayout = findViewById(R.id.dialog_calendar_layout);
        errorIv.setOnClickListener(this);

        addViewToLayout();
        initGridView();

        setGVListener();
    }

    private void setGVListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selPos = position;
                adapter.notifyDataSetInvalidated();
                int month = position + 1;
                int year = adapter.year;

                onRefreshListener.onRefresh(selectPos,year,month);
                cancel();
            }
        });
    }

    private void initGridView() {
        int selYear = yearList.get(selectPos);
        adapter = new CalendarAdapter(getContext(), selYear);
        if (selectMonth == -1) {
            int month = Calendar.getInstance().get(Calendar.MONTH);
            adapter.selPos = month;
        }else {
            adapter.selPos = selectMonth-1;
        }
        gv.setAdapter(adapter);
    }

    private void addViewToLayout() {
        hsvViewList = new ArrayList<>();
        yearList = DBManager.getYearListFromAccounttb();

        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }

        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv, null);
            hsvLayout.addView(view);
            TextView hsvTv = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTv.setText(year+"");
            hsvViewList.add(hsvTv);
        }
        if (selectPos == -1) {
            selectPos = hsvViewList.size()-1;
        }
        changeTvbg(selectPos);
        setHSVClickListener();
    }

    private void setHSVClickListener() {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView view = hsvViewList.get(i);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeTvbg(pos);
                    selectPos = pos;

                    int year = yearList.get(selectPos);
                    adapter.setYear(year);
                }
            });
        }
    }

    private void changeTvbg(int selectPos) {
        for (int i = 0; i < hsvViewList.size(); i++) {
            TextView tv = hsvViewList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_bg);
            tv.setTextColor(Color.BLACK);
        }

        TextView selView = hsvViewList.get(selectPos);
        selView.setBackgroundResource(R.drawable.main_recordbtn_bg);
        selView.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_calendar_iv:
                cancel();
                break;
        }
    }

    public void setDialogSize(){
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());
        wlp.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}
