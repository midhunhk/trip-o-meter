/*
 * MIT License
 * Copyright (c) 2016 Midhun Harikumar
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ae.apps.tripmeter.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        super(context, R.layout.contact_spinner_item, contacts);

        this.mContext = context;
        mValues = contacts;
    }

    @Override
    public int getCount() {
        return mValues.size();
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
        // RoundedImageView imageView = (RoundedImageView) convertView.findViewById(R.id.contactImage);
        //imageView.setVisibility(View.GONE);

        contactName.setText(mValues.get(position).getName());
        // imageView.setImageResource(R.drawable.ic_face_profile);
        // imageView.setImageResource(mValues.get(position).getMockProfileImageResource());

        return convertView;
    }
}
