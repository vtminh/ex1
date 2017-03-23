package cse.vn.currency;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class MyListViewAdapter<T> extends ArrayAdapter {

    Context context;
    boolean isDarkBackground;

    public MyListViewAdapter(Context context, int resource, List<CurrencyInfo> objects) {
        super(context, resource, objects);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = super.getView(position, convertView, parent);

        View view = convertView;

        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.list_view_item, null);
        }

        TextView tvCode = (TextView) view.findViewById(R.id.tvCode);
        TextView tvName= (TextView) view.findViewById(R.id.tvName);
        TextView tvTransfer = (TextView) view.findViewById(R.id.tvTransfer);


        CurrencyInfo currencyInfo = (CurrencyInfo) getItem(position);

        tvCode.setText(currencyInfo.code);
        tvName.setText(currencyInfo.name);
        tvTransfer.setText(String.valueOf(currencyInfo.transfer));

        return view;
    }


}
