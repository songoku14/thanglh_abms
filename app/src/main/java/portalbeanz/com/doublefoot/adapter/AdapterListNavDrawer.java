package portalbeanz.com.doublefoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.model.ItemNavDrawer;

/**
 * Created by thangit14 on 7/10/16.
 */
public class AdapterListNavDrawer extends ArrayAdapter<ItemNavDrawer> {
    private Context context;
    private LayoutInflater inflater;

   public AdapterListNavDrawer(Context context, ArrayList<ItemNavDrawer> datas) {
        super(context, -1,datas);
       inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemNavDrawer itemNavDrawer = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);

            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            viewHolder.container = (ViewGroup) convertView.findViewById(R.id.container);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTitle.setText(itemNavDrawer.getTitle());
        viewHolder.imgIcon.setImageResource(itemNavDrawer.getIcon());
        if (itemNavDrawer.isSelected()) {
            viewHolder.container.setBackgroundResource(R.color.highlight_gray_selected);
        } else {
            viewHolder.container.setBackgroundResource(R.color.highlight_gray);
        }
        return convertView;
    }

    private class ViewHolder{
        private ImageView imgIcon;
        private TextView txtTitle;
        private ViewGroup container;
    }
}
