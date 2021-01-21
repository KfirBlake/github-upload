package com.blake.blake49ersarticles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blake.blake49ersarticles.adapter.ArticlesRecViewAdapter;
import com.blake.blake49ersarticles.db.DBHelper;
import com.blake.blake49ersarticles.model.ArticleInfo;
import com.blake.blake49ersarticles.rest.RestManager;
import com.blake.blake49ersarticles.rest.model.Posts;
import com.blake.blake49ersarticles.rest.model.PostsBody;
import com.blake.blake49ersarticles.settings.AppSettingsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    private RecyclerView articleRecView;
    private ArticlesRecViewAdapter adapter;
    private ArrayList<ArticleInfo> articlesInfo = new ArrayList<>();
    public DBHelper dbHelper;
    //int index = 0;
    String linkToTwitter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_read:
                adapter.setArticlesInfo(getArticlesList(articlesInfo, true));
                item.setChecked(true);
                return true;

            case R.id.menu_not_read:
                adapter.setArticlesInfo(getArticlesList(articlesInfo, false));
                item.setChecked(true);
                return true;

            case R.id.menu_favorite:
                adapter.setArticlesInfo(getArticlesListFavorite(articlesInfo));
                item.setChecked(true);
                return true;

            case R.id.menu_all:
                adapter.setArticlesInfo(articlesInfo);
                item.setChecked(true);
                return true;

            case R.id.menu_load_twitter:
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkToTwitter)));
                return true;


            case R.id.menu_refresh_from_site:
                dbHelper.deleteAllArticles();
                dbHelper.insertAllArticles(articlesInfo);
                getArticelsCSBBayArea();
                return true;

            case R.id.menu_settings:
                Intent intentSettings = new Intent(this, AppSettingsActivity.class);
                startActivity(intentSettings);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SharedPreferences sharedPreferencesSettings = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferencesSettings.getBoolean("setting_pref_delete_flag", false))
        {
            dbHelper.deleteArticlesByDay(sharedPreferencesSettings.getString("setting_pref_delete_by_days", "7"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articleRecView = findViewById(R.id.ArticlesRecView);
        dbHelper = new DBHelper(this);

        // Setting Default values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPreferencesSettings = PreferenceManager.getDefaultSharedPreferences(this);
        linkToTwitter = sharedPreferencesSettings.getString(getResources().getString(R.string.pref_load_twitter_link_key), getResources().getString(R.string.pref_load_twitter_link_link));


        getArticelsCSBBayArea();
        ActionBar actionBar = getSupportActionBar();
        adapter = new ArticlesRecViewAdapter(this, actionBar);
        adapter.setArticlesInfo(articlesInfo);

        articleRecView.setAdapter(adapter);
        articleRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getArticelsCSBBayArea()
    {
        /*final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Getting articles. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();*/
        final ProgressBar articelProgressBar = (ProgressBar) findViewById(R.id.ArticelProgressBar);
        articelProgressBar.setVisibility(View.VISIBLE);

        final Call<PostsBody> articlesList = RestManager.getInstance().getPostsResult().getPosts();
        articlesList.enqueue(new Callback<PostsBody>()
        {
            @Override
            public void onResponse(Call<PostsBody> call, Response<PostsBody> response)
            {
                try
                {
                    if (null != response)
                    {
                        ArrayList<ArticleInfo> articlesInfoFromRest = new ArrayList<>();
                        PostsBody body = response.body();
                        List<Posts> postsList = body.getData();
                        if (null != postsList)
                        {
                            for (Posts posts : postsList)
                            {
                                ArticleInfo articleInfo = new ArticleInfo();
                                articleInfo.setId(posts.getNid());
                                articleInfo.setTitle(posts.getTitle());
                                articleInfo.setDescription(posts.getType());
                                articleInfo.setLinkArticle(posts.getPath());
                                articleInfo.setLinkImage(posts.getImage());
                                articleInfo.setTime(posts.getPublished());
                                articleInfo.setSite(getResources().getString(R.string.CSNBayAreaName));
                                articlesInfoFromRest.add(articleInfo);
                            }
                            dbHelper.insertAllArticles(articlesInfoFromRest);
                            articlesInfo = dbHelper.getAllArticles();
                            articelProgressBar.setVisibility(View.INVISIBLE);
                            //adapter.setArticlesInfo(articlesInfo);
                            adapter.setArticlesInfo(getArticlesList(articlesInfo, false));
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Getting empty result of Posts from website").setTitle("Error Getting Data");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Unexpected error occurred on function - onResponse:" + e.getMessage(), Toast.LENGTH_SHORT);
                }
                finally
                {
                    //dialog.dismiss();
                    articelProgressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PostsBody> call, Throwable t)
            {
                try
                {
                    Toast.makeText(MainActivity.this, "Unexpected error occurred:" + t.getMessage(), Toast.LENGTH_SHORT);
                }
                catch (Exception e)
                {
                }
                finally
                {
                    //dialog.dismiss();
                    articelProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private ArrayList<ArticleInfo> getArticlesList(ArrayList<ArticleInfo> articlesInfo, boolean read)
    {
        ArrayList<ArticleInfo> array = new ArrayList<>();
        for (ArticleInfo articleInfo : articlesInfo)
        {
            if (articleInfo.isRead() == read)
                array.add(articleInfo);
        }
        return array;
    }

    private ArrayList<ArticleInfo> getArticlesListFavorite(ArrayList<ArticleInfo> articlesInfo)
    {
        ArrayList<ArticleInfo> array = new ArrayList<>();
        for (ArticleInfo articleInfo : articlesInfo)
        {
            if (articleInfo.isFavorite())
                array.add(articleInfo);
        }
        return array;
    }

}