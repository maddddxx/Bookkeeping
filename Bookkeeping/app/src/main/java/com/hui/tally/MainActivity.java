package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hui.tally.adapter.AccountAdapter;
import com.hui.tally.db.AccountBean;
import com.hui.tally.db.DBManager;
import com.hui.tally.utils.BudgetDialog;
import com.hui.tally.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;

    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;

    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        addLVHeaderView();
        mDatas = new ArrayList<>();

        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                }
                int pos = position-1;
                AccountBean clickBean = mDatas.get(pos);


                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    private void showDeleteItemDialog(final  AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示訊息").setMessage("確定要刪除這條紀錄嗎？")
                .setNegativeButton("取消",null)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();

                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);
                        adapter.notifyDataSetChanged();
                        setTopTvShow();
                    }
                });
        builder.create().show();
    }

    private void addLVHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }
    private void setTopTvShow() {
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日支出 $"+outcomeOneDay+"  收入 $"+incomeOneDay;
        topConTv.setText(infoOneDay);
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("$"+incomeOneMonth);
        topOutTv.setText("$"+outcomeOneMonth);

        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            topbudgetTv.setText("$ 0");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            topbudgetTv.setText("$"+syMoney);
        }
    }

    private void loadDBData() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_search:
                Intent it = new Intent(this, SearchActivity.class);
                startActivity(it);
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:

                toggleShow();
                break;
        }
        if (v == headerView) {

            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money-outcomeOneMonth;
                topbudgetTv.setText("$"+syMoney);
            }
        });
    }

    boolean isShow = true;

    private void toggleShow() {
        if (isShow) {
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);
            topOutTv.setTransformationMethod(passwordMethod);
            topbudgetTv.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        }else{
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);
            topOutTv.setTransformationMethod(hideMethod);
            topbudgetTv.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }
}
