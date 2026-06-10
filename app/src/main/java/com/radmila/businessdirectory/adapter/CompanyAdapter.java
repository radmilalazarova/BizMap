package com.radmila.businessdirectory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.radmila.businessdirectory.R;
import com.radmila.businessdirectory.model.Company;

import java.util.List;

public class CompanyAdapter extends BaseAdapter {

    private final Context context;
    private List<Company> companies;

    public CompanyAdapter(Context context, List<Company> companies) {
        this.context   = context;
        this.companies = companies;
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Company getItem(int position) {
        return companies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return companies.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_company, parent, false);

            holder            = new ViewHolder();
            holder.imgLogo    = convertView.findViewById(R.id.imgLogo);
            holder.tvName     = convertView.findViewById(R.id.tvName);
            holder.tvAddress  = convertView.findViewById(R.id.tvAddress);
            holder.tvPhone    = convertView.findViewById(R.id.tvPhone);
            holder.tvWebsite  = convertView.findViewById(R.id.tvWebsite);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Company company = companies.get(position);

        holder.tvName.setText(company.getName());
        holder.tvAddress.setText(company.getAddress());
        holder.tvPhone.setText(company.getPhone());
        holder.tvWebsite.setText(company.getWebsite());


        String logoName = company.getLogoUrl();
        int resId = 0;

        if (logoName != null && !logoName.isEmpty()) {
            resId = context.getResources().getIdentifier(
                    logoName,
                    "drawable",
                    context.getPackageName()
            );
        }

        if (resId != 0) {
            holder.imgLogo.setImageResource(resId);
        } else {
            holder.imgLogo.setImageResource(R.drawable.ic_business_default);
        }

        return convertView;
    }


    public void updateList(List<Company> newList) {
        this.companies = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imgLogo;
        TextView  tvName;
        TextView  tvAddress;
        TextView  tvPhone;
        TextView  tvWebsite;
    }
}