package portalbeanz.com.doublefoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.model.ItemPackage;
import portalbeanz.com.doublefoot.model.ItemPackagePrice;

/**
 * Created by thangit14 on 7/14/16.
 */
public class AdapterListPackageChild extends ArrayAdapter<ItemPackage> {

    private LayoutInflater layoutInflater;
    private Context context;
    private MyOnClickListener myOnClickListener;

    public AdapterListPackageChild(Context context, ArrayList<ItemPackage> datas, MyOnClickListener myOnClickListener) {
        super(context, -1, datas);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.myOnClickListener = myOnClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ItemPackage itemPackage = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_package_child, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.btnBook = (Button) convertView.findViewById(R.id.btn_book);
            viewHolder.txtPackageName = (TextView) convertView.findViewById(R.id.txt_package_name);
            viewHolder.txtTime1 = (TextView) convertView.findViewById(R.id.txt_time_1);
            viewHolder.txtTime2 = (TextView) convertView.findViewById(R.id.txt_time_2);
            viewHolder.txtTime3 = (TextView) convertView.findViewById(R.id.txt_time_3);
            viewHolder.txtTime4 = (TextView) convertView.findViewById(R.id.txt_time_4);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtPackageName.setText(itemPackage.getTitle());

        ArrayList<ItemPackagePrice> itemPackagePrices = itemPackage.getItemPackagePrices();
        viewHolder.txtTime1.setText(itemPackagePrices.get(0).getTime() + " Mins (" +
                new DecimalFormat(context.getString(R.string.format_price)).
                        format(itemPackagePrices.get(0).getPrice())+ "$)");

        try {
            viewHolder.txtTime2.setText(itemPackagePrices.get(1).getTime() + " Mins (" +
                    new DecimalFormat(context.getString(R.string.format_price)).
                            format(itemPackagePrices.get(1).getPrice())+ "$)");
        } catch (IndexOutOfBoundsException ex) {
            viewHolder.txtTime2.setVisibility(View.GONE);
        }

        try {
            viewHolder.txtTime3.setText(itemPackagePrices.get(2).getTime() + " Mins (" +
                    new DecimalFormat(context.getString(R.string.format_price)).
                            format(itemPackagePrices.get(2).getPrice())+ "$)");
        } catch (IndexOutOfBoundsException ex) {
            viewHolder.txtTime3.setVisibility(View.GONE);
        }

        try {
            viewHolder.txtTime4.setText(itemPackagePrices.get(3).getTime() + " Mins (" +
                    new DecimalFormat(context.getString(R.string.format_price)).
                            format(itemPackagePrices.get(3).getPrice()) + "$)");
        } catch (IndexOutOfBoundsException ex) {
            viewHolder.txtTime4.setVisibility(View.GONE);
        }


        viewHolder.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClickListener.onBookClick(itemPackage.getId());
            }
        });
        return convertView;
    }

    private class ViewHolder {
        Button btnBook;
        TextView txtPackageName;
        TextView txtTime1;
        TextView txtTime2;
        TextView txtTime3;
        TextView txtTime4;
    }

    public interface MyOnClickListener{
        void onBookClick(int id);
    }

}
