package com.example.exchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
{
    private Context context;
    private List<Listings> listings;

    public ListAdapter(Context context, List<Listings> listings)
    {
        this.context = context;
        this.listings = listings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Listings list = listings.get(position);
        holder.bind(list);
    }

    @Override
    public int getItemCount()
    {
        return listings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvTitle, tvPrice;
        private ImageView ivImage;


        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTitle= itemView.findViewById(R.id.tvTitle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            //tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }

        public void bind(Listings list)
        {
            String createdat = list.getCreatedKeyAt().toString();
            String timedifference = TimeFormatter.getTimeDifference(createdat, context);
            //tvCreatedAt.setText(timedifference);
            tvTitle.setText(list.getTitle());
            tvPrice.setText(String.format("$%s",list.getPrice() + " · "));

            ParseFile image = list.getImage();
            if (image != null)
            {
                Glide.with(context).load(list.getImage().getUrl()).into(ivImage);
            }
        }
    }
}
