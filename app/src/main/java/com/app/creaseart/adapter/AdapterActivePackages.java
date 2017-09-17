package com.app.creaseart.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.creaseart.R;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.ModelPackage;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterActivePackages extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ModelPackage> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterActivePackages(Context context, OnCustomItemClicListener lis, ArrayList<ModelPackage> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_active_package, parent, false);

            vh = new CustomViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof CustomViewHolder) {

            ModelPackage m1 = (ModelPackage) detail.get(i);

            ((CustomViewHolder) holder).text_name.setText(m1.getPackageName());
            ((CustomViewHolder) holder).text_date.setText(m1.getPackagePrice());

            ((CustomViewHolder) holder).card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickListener(i, 1);
                }
            });

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_name, text_message, text_date;
        ImageView image_viewers;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.image_viewers = (ImageView) view.findViewById(R.id.image_viewers);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.text_message = (TextView) view.findViewById(R.id.text_message);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(getPosition(), 1);
        }

    }

    @Override
    public int getItemViewType(int position) {
        ModelPackage m1 = (ModelPackage) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }
}