package portalbeanz.com.doublefoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.model.ItemPackage;
import portalbeanz.com.doublefoot.util.MySingleton;

/**
 * Created by thangit14 on 7/13/16.
 */
public class AdapterListPackages extends ArrayAdapter<ItemPackage>{
    private LayoutInflater layoutInflater;
    private Context context;
    private ImageLoader imageLoader;
    public AdapterListPackages(Context context, ArrayList<ItemPackage> datas) {
        super(context, -1, datas);
        imageLoader = MySingleton.getInstance(context).getImageLoader();
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemPackage itemPackage = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_packages, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgAvatar = (NetworkImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgAvatar.setImageUrl(itemPackage.getImage(),imageLoader);

        viewHolder.txtTitle.setText(itemPackage.getTitle());
        return convertView;
    }

    private class ViewHolder {
        TextView txtTitle;
        NetworkImageView imgAvatar;
    }

}
