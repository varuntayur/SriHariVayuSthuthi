package com.vtayur.sriharivayusthuthi.detail;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.vtayur.sriharivayusthuthi.R;
import com.vtayur.sriharivayusthuthi.data.DataProvider;
import java.util.List;
import java.util.Random;

public class StaggeredGridAdapter extends ArrayAdapter<String> {
    private static final String TAG = "StaggeredGridAdapter";
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray();
    private final List<Integer> mBackgroundColors = DataProvider.getBackgroundColorList();
    private final LayoutInflater mLayoutInflater;
    private final Random mRandom = new Random();

    static class ViewHolder {
        Button btnGo;
        DynamicHeightTextView txtLineOne;

        ViewHolder() {
        }
    }

    public StaggeredGridAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        int backgroundIndex;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        double positionHeight = getPositionRatio(position);
        if (position >= this.mBackgroundColors.size()) {
            backgroundIndex = position % this.mBackgroundColors.size();
        } else {
            backgroundIndex = position;
        }
        convertView.setBackgroundResource(((Integer) this.mBackgroundColors.get(backgroundIndex)).intValue());
        vh.txtLineOne.setHeightRatio(1.5d);
        vh.txtLineOne.setText((CharSequence) getItem(position));
        return convertView;
    }

    private double getPositionRatio(int position) {
        double ratio = ((Double) sPositionHeightRatios.get(position, Double.valueOf(0.0d))).doubleValue();
        if (ratio != 0.0d) {
            return ratio;
        }
        ratio = getRandomHeightRatio();
        sPositionHeightRatios.append(position, Double.valueOf(ratio));
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (this.mRandom.nextDouble() / 2.0d) + 1.0d;
    }
}