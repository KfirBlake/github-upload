package com.blake.blake49ersarticles.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.blake.blake49ersarticles.model.ArticleInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kfir Blake on 13/10/2020.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String TAG = "SQLiteOpenHelper";

    public static final String DATABASE_NAME = "Articles.db";
    public static final String ARTICLE_TABLE_NAME = "articles";
    public static final String ARTICLE_COLUMN_ID = "id";
    public static final String ARTICLE_COLUMN_TITLE = "title";
    public static final String ARTICLE_COLUMN_DESCRIPTION = "description";
    public static final String ARTICLE_COLUMN_TIME = "time";
    public static final String ARTICLE_COLUMN_LINK_IMAGE = "linkImage";
    public static final String ARTICLE_COLUMN_LINK_ARTICLE = "linkArticle";
    public static final String ARTICLE_COLUMN_SITE = "site";
    public static final String ARTICLE_COLUMN_READ = "read";
    public static final String ARTICLE_COLUMN_FAVORITE = "favorite";
    private static final String CREATE_TABLE = "CREATE TABLE " + ARTICLE_TABLE_NAME + " ( "
            + ARTICLE_COLUMN_ID + " STRING PRIMARY KEY, "
            + ARTICLE_COLUMN_TITLE + " VARCHAR(255), "
            + ARTICLE_COLUMN_DESCRIPTION + " VARCHAR(255), "
            + ARTICLE_COLUMN_TIME + " LONG, "
            + ARTICLE_COLUMN_LINK_IMAGE + " VARCHAR(255), "
            + ARTICLE_COLUMN_LINK_ARTICLE + " VARCHAR(255), "
            + ARTICLE_COLUMN_SITE + " VARCHAR(255), "
            + ARTICLE_COLUMN_READ + " VARCHAR(1), "
            + ARTICLE_COLUMN_FAVORITE + " VARCHAR(1));";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + ARTICLE_TABLE_NAME;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public ArrayList<ArticleInfo> getAllArticles()
    {
        ArrayList<ArticleInfo> array = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ARTICLE_TABLE_NAME + " order by " + ARTICLE_COLUMN_TIME + " desc", null);
        //cursor.moveToFirst();

        while (cursor.moveToNext())
        {
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setId(cursor.getString(0));
            articleInfo.setTitle(cursor.getString(1));
            articleInfo.setDescription(cursor.getString(2));
            articleInfo.setTime(cursor.getLong(3));
            articleInfo.setLinkImage(cursor.getString(4));
            articleInfo.setLinkArticle(cursor.getString(5));
            articleInfo.setSite(cursor.getString(6));
            articleInfo.setRead(!(cursor.getString(7)).equals("0"));
            articleInfo.setFavorite(!(cursor.getString(8)).equals("0"));
            array.add(articleInfo);
        }
        return array;
    }

    public boolean checkArticleExist(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ARTICLE_TABLE_NAME + " where " + ARTICLE_COLUMN_ID + "=" + id + "", null);
        return (res.getCount() == 0);
    }

    public void insertArticle(ArticleInfo articleInfo)
    {
        if (checkArticleExist(articleInfo.getId()))
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ARTICLE_COLUMN_ID, articleInfo.getId());
            contentValues.put(ARTICLE_COLUMN_TITLE, articleInfo.getTitle());
            contentValues.put(ARTICLE_COLUMN_DESCRIPTION, articleInfo.getDescription());
            contentValues.put(ARTICLE_COLUMN_TIME, articleInfo.getTime());
            contentValues.put(ARTICLE_COLUMN_LINK_IMAGE, articleInfo.getLinkImage());
            contentValues.put(ARTICLE_COLUMN_LINK_ARTICLE, articleInfo.getLinkArticle());
            contentValues.put(ARTICLE_COLUMN_SITE, articleInfo.getSite());
            contentValues.put(ARTICLE_COLUMN_READ, (articleInfo.isRead() ? "1" : "0"));
            contentValues.put(ARTICLE_COLUMN_FAVORITE, (articleInfo.isFavorite() ? "1" : "0"));
            db.insert(ARTICLE_TABLE_NAME, null, contentValues);
        }
    }

    public void insertAllArticles(ArrayList<ArticleInfo> articleInfos)
    {
        for (ArticleInfo articleInfo : articleInfos)
        {
            insertArticle(articleInfo);
        }
    }

    public void updateArticle(String id, String read, String favorite)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTICLE_COLUMN_READ, read);
        contentValues.put(ARTICLE_COLUMN_FAVORITE, favorite);
        db.update(ARTICLE_TABLE_NAME, contentValues, ARTICLE_COLUMN_ID + " = ? ", new String[]{id});
    }

    public Integer deleteArticle(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ARTICLE_TABLE_NAME, ARTICLE_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
    }

    public void deleteAllArticles()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ARTICLE_TABLE_NAME, null, null);
    }

    public void deleteArticlesByDay(String days)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ARTICLE_TABLE_NAME, ARTICLE_COLUMN_TIME + " < ? and " + ARTICLE_COLUMN_FAVORITE + " = 0",
                new String[]{String.valueOf(getTimeByNumberOfDays(days))});

    }

    public long getTimeByNumberOfDays(String days)
    {
        int daysToRemove = Integer.parseInt(days) * -1;
        Log.d(TAG, "Days To Remove: " + daysToRemove);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, daysToRemove);
        Log.d(TAG, "Date To Remove from: " + daysToRemove);

        Date deleteDate = calendar.getTime();
        Log.d(TAG, "Date To Remove from: " + deleteDate);

        long deleteTime = deleteDate.getTime() / 1000;
        Log.d(TAG, "Delete time - Published After: " + deleteTime);
        return deleteTime;
    }

}
