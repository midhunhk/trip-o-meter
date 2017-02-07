package com.ae.apps.tripmeter.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ae.apps.common.views.RoundedImageView;
import com.ae.apps.common.vo.ContactVo;
import com.ae.apps.tripmeter.R;

import java.util.List;

/**
 * Custom spinner adapter
 */

public class ContactSpinnerAdapter extends ArrayAdapter<ContactVo> {

    private Context mContext;
    private List<ContactVo> mValues;

    public ContactSpinnerAdapter(Context context, List<ContactVo> contacts) {
        super(context, R.layout.contact_spinner_item);

        this.mContext = context;
        mValues = contacts;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView);
    }

    @NonNull
    private View getCustomView(int position, View convertView) {
        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_spinner_item, null);
        }

        TextView contactName = (TextView) convertView.findViewById(R.id.contactName);
        RoundedImageView imageView = (RoundedImageView) convertView.findViewById(R.id.contactImage);

        contactName.setText(mValues.get(position).getName());
        imageView.setImageResource(mValues.get(position).getMockProfileImageResource());

        return convertView;
    }
}
