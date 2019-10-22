package com.soaic.libcommon.weight.recyclerview.holder;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class BasicItemHolder extends RecyclerView.ViewHolder {

    public BasicItemHolder(View itemView) {
        super(itemView);
    }

    public <T extends View> T findViewById(int viewId){
        return itemView.findViewById(viewId);
    }
}
