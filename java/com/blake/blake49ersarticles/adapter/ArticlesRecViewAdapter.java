package com.blake.blake49ersarticles.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blake.blake49ersarticles.MainActivity;
import com.blake.blake49ersarticles.R;
import com.blake.blake49ersarticles.WebSiteActiviy;
import com.blake.blake49ersarticles.model.ArticleInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kfir Blake on 08/10/2020.
 */
public class ArticlesRecViewAdapter extends RecyclerView.Adapter<ArticlesRecViewAdapter.ViewHolder>
{

    private ArrayList<ArticleInfo> articlesInfo = new ArrayList<>();
    private Context context;
    ActionBar actionBar;

    public ArticlesRecViewAdapter(Context context, ActionBar actionBar)
    {
        this.context = context;
        this.actionBar = actionBar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_blake49_article_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.txtTitle.setText(articlesInfo.get(position).getTitle());
        holder.txtDesc.setText(articlesInfo.get(position).getDescription());
        holder.txtSite.setText(articlesInfo.get(position).getSite());
        holder.txtTime.setText(getPublishedTime(articlesInfo.get(position).getTime()));
        holder.checkBoxRead.setChecked(articlesInfo.get(position).isRead());
        holder.checkBoxFavorite.setChecked(articlesInfo.get(position).isFavorite());
        Picasso.with(context).load(articlesInfo.get(position).getLinkImage()).into(holder.image);

        final String articelFullPath = context.getResources().getString(R.string.CSNBayAreaHome) + articlesInfo.get(position).getLinkArticle();
        final String type = articlesInfo.get(position).getDescription();

        holder.parent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (type.equals("gallery"))
                {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(articelFullPath)));
                }
                else
                {
                    Intent intent = new Intent(context, WebSiteActiviy.class);
                    intent.putExtra("URL", articelFullPath);
                    context.startActivity(intent);
                }
            }
        });

        final int index = position;
        holder.checkBoxRead.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                articlesInfo.get(index).setRead(((CheckBox) v).isChecked());
                ((MainActivity) context).dbHelper.updateArticle(articlesInfo.get(index).getId(), articlesInfo.get(index).isRead() ? "1" : "0", articlesInfo.get(index).isFavorite() ? "1" : "0");
                setNumberOfArticlesNotRead();
            }
        });

        holder.checkBoxFavorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                articlesInfo.get(index).setFavorite(((CheckBox) v).isChecked());
                ((MainActivity) context).dbHelper.updateArticle(articlesInfo.get(index).getId(), articlesInfo.get(index).isRead() ? "1" : "0", articlesInfo.get(index).isFavorite() ? "1" : "0");
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return articlesInfo.size();
    }

    public void setArticlesInfo(ArrayList<ArticleInfo> articlesInfo)
    {
        this.articlesInfo = articlesInfo;
        notifyDataSetChanged();
        setNumberOfArticlesNotRead();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txtTitle, txtDesc, txtSite, txtTime;
        private ImageView image;
        private CheckBox checkBoxRead, checkBoxFavorite;
        private CardView parent;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.ArticleImage);
            txtTitle = itemView.findViewById(R.id.ArticleTitle);
            txtDesc = itemView.findViewById(R.id.ArticleDescription);
            txtSite = itemView.findViewById(R.id.ArticleSite);
            txtTime = itemView.findViewById(R.id.ArticleTime);
            checkBoxRead = itemView.findViewById(R.id.ArticelRead);
            checkBoxFavorite = itemView.findViewById(R.id.ArticleFavorite);
            parent = itemView.findViewById(R.id.ArticlesParent);
        }
    }

    public String getPublishedTime(Long published)
    {
        long time = published * 1000;
        Date currentDate = new Date();
        Date publishedDate = new Date(time);

        long difference_In_Time = currentDate.getTime() - publishedDate.getTime();
        long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 24;
        long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24;
        long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;

        //return "Days: " + difference_In_Days + " Hours: " + difference_In_Hours + " Minutes: " + difference_In_Minutes;
        return difference_In_Days + " Days. " + difference_In_Hours + " Hours. " + difference_In_Minutes + " Minutes.";
    }

    public void setNumberOfArticlesNotRead()
    {
        int numberOfNotRead = 0;
        for (ArticleInfo articleInfo : articlesInfo)
        {
            if (!articleInfo.isRead())
                numberOfNotRead++;
        }
        actionBar.setTitle(context.getResources().getString(R.string.app_name) + " - Unread: " + numberOfNotRead);
    }
}
