package com.hui.tally.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hui.tally.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context,"tally.db" , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,sImageId integer,kind integer)";
        db.execSQL(sql);
        insertType(db);

        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,beizhu varchar(80),money float," +
                "time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {

        String sql = "insert into typetb (typename,imageId,sImageId,kind) values (?,?,?,?)";
        db.execSQL(sql,new Object[]{"其他", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,0});
        db.execSQL(sql,new Object[]{"餐飲", R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,0});
        db.execSQL(sql,new Object[]{"交通", R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,0});
        db.execSQL(sql,new Object[]{"購物", R.mipmap.ic_gouwu,R.mipmap.ic_gouwu_fs,0});
        db.execSQL(sql,new Object[]{"服飾", R.mipmap.ic_fushi,R.mipmap.ic_fushi_fs,0});
        db.execSQL(sql,new Object[]{"日用品", R.mipmap.ic_riyongpin,R.mipmap.ic_riyongpin_fs,0});
        db.execSQL(sql,new Object[]{"娛樂", R.mipmap.ic_yule,R.mipmap.ic_yule_fs,0});
        db.execSQL(sql,new Object[]{"零食", R.mipmap.ic_lingshi,R.mipmap.ic_lingshi_fs,0});
        db.execSQL(sql,new Object[]{"飲料", R.mipmap.ic_yanjiu,R.mipmap.ic_yanjiu_fs,0});
        db.execSQL(sql,new Object[]{"學習", R.mipmap.ic_xuexi,R.mipmap.ic_xuexi_fs,0});
        db.execSQL(sql,new Object[]{"醫療", R.mipmap.ic_yiliao,R.mipmap.ic_yiliao_fs,0});
        db.execSQL(sql,new Object[]{"住宅", R.mipmap.ic_zhufang,R.mipmap.ic_zhufang_fs,0});
        db.execSQL(sql,new Object[]{"水電費", R.mipmap.ic_shuidianfei,R.mipmap.ic_shuidianfei_fs,0});
        db.execSQL(sql,new Object[]{"通訊", R.mipmap.ic_tongxun,R.mipmap.ic_tongxun_fs,0});
        db.execSQL(sql,new Object[]{"社交", R.mipmap.ic_renqingwanglai,R.mipmap.ic_renqingwanglai_fs,0});

        db.execSQL(sql,new Object[]{"其他", R.mipmap.in_qt,R.mipmap.in_qt_fs,1});
        db.execSQL(sql,new Object[]{"薪資", R.mipmap.in_xinzi,R.mipmap.in_xinzi_fs,1});
        db.execSQL(sql,new Object[]{"獎金", R.mipmap.in_jiangjin,R.mipmap.in_jiangjin_fs,1});
        db.execSQL(sql,new Object[]{"借入", R.mipmap.in_jieru,R.mipmap.in_jieru_fs,1});
        db.execSQL(sql,new Object[]{"收債", R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,1});
        db.execSQL(sql,new Object[]{"利息收入", R.mipmap.in_lixifuji,R.mipmap.in_lixifuji_fs,1});
        db.execSQL(sql,new Object[]{"投資回報", R.mipmap.in_touzi,R.mipmap.in_touzi_fs,1});
        db.execSQL(sql,new Object[]{"二手交易", R.mipmap.in_ershoushebei,R.mipmap.in_ershoushebei_fs,1});
        db.execSQL(sql,new Object[]{"意外之財", R.mipmap.in_yiwai,R.mipmap.in_yiwai_fs,1});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
