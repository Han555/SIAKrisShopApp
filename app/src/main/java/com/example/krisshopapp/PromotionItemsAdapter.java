package com.example.krisshopapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PromotionItemsAdapter extends RecyclerView.Adapter<PromotionItemsAdapter.ViewHolder> {

    private Context currentContext;
    private RelativeLayout nameInputLayout;
    private TextView itemName1,itemName2, itemName3;
    private ImageView itemImage1, itemImage2, itemImage3;
    List<String> placeholdervalues = new ArrayList<String>();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRowDetail;

        public ViewHolder(LinearLayout v) {
            super(v);
            mRowDetail = v;
        }

    }

    public PromotionItemsAdapter(Context context) {
        this.currentContext = context;
        placeholdervalues.add("");
        placeholdervalues.add("");
        placeholdervalues.add("");
    }

    @Override
    public PromotionItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout rowLayout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.promotion_item_row, null);
        ViewHolder promotionItemsViewHolder = new ViewHolder(rowLayout);
        itemImage1 = promotionItemsViewHolder.mRowDetail.findViewById(R.id.imageView1);
        itemImage2 = promotionItemsViewHolder.mRowDetail.findViewById(R.id.imageView2);
        itemImage3 = promotionItemsViewHolder.mRowDetail.findViewById(R.id.imageView3);
        itemName1 = promotionItemsViewHolder.mRowDetail.findViewById(R.id.textView1);
        itemName2 = promotionItemsViewHolder.mRowDetail.findViewById(R.id.textView2);
        itemName3 = promotionItemsViewHolder.mRowDetail.findViewById(R.id.textView3);
        return promotionItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(position == 0) {
            itemImage1.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.iphone));
            itemImage2.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.bag));
            itemImage3.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.cap));
            itemName1.setText("iphone");
            itemName2.setText("chanel bag");
            itemName3.setText("cap");
        } else if(position == 1) {
            itemImage1.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.shoe));
            itemImage2.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.toys));
            itemImage3.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.socks));
            itemName1.setText("jordan");
            itemName2.setText("disney");
            itemName3.setText("socks");
        } else if(position == 2) {
            itemImage1.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.vitamins));
            itemImage2.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.jeans));
            itemImage3.setImageDrawable(currentContext.getResources().getDrawable(R.drawable.shirt));
            itemName1.setText("vitamins");
            itemName2.setText("jeans");
            itemName3.setText("shirt");
        }

    }

    @Override
    public int getItemCount() {
        return placeholdervalues.size();
    }
}
