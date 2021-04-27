package com.example.exchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Listings> listings;
    private List<Listings> listingsfull;

    public ListAdapter(Context context, List<Listings> listings) {
        this.context = context;
        this.listings = listings;
        listingsfull = new ArrayList<>(listings);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listings list = listings.get(position);
        holder.bind(list);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvPrice;
        private ImageView ivImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            //tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }

        public void bind(Listings list) {
            String createdat = list.getCreatedKeyAt().toString();
            String timedifference = TimeFormatter.getTimeDifference(createdat, context);
            //tvCreatedAt.setText(timedifference);
            tvTitle.setText(list.getTitle());
            tvPrice.setText(String.format("$%s",list.getPrice() + " · "));

            ParseFile image = list.getImage();
            if (image != null) {
                Glide.with(context).load(list.getImage().getUrl()).into(ivImage);
            }
        }
    }

    @Override
    public Filter getFilter() {
        return listingFilter;
    }

    private Filter listingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Listings> filteredListings = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredListings.addAll(listingsfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Listings item : listingsfull) {
                    if (item.getDescription().toLowerCase().contains(filterPattern)) {
                        filteredListings.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredListings;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listings.clear();
            listings.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
