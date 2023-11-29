package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hui.tally.db.DBManager;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_iv_back:
                finish();
                break;
            case R.id.setting_tv_clear:
                showDeleteDialog();
                break;
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("刪除提示")
                .setMessage("確定要刪除所有紀錄嗎？\n注意：刪除後無法恢復！")
                .setPositiveButton("取消",null)
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteAllAccount();
                        Toast.makeText(SettingActivity.this,"刪除成功！",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}
