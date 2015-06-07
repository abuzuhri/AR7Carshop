package com.example.jma.ar7carshop.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Tareq on 03/13/2015.
 */
public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
    private long ID;
    public long getID(){ return  ID;}
    public void setID(long ID){ this.ID=ID;}
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }
}
