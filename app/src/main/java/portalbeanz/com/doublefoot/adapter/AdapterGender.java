package portalbeanz.com.doublefoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.model.ItemGender;

/**
 * Created by datnx on 12/16/14.
 */
public class AdapterGender extends ArrayAdapter<ItemGender> {

    private final LayoutInflater mInflate;

    public AdapterGender(Context context, List<ItemGender> objects) {
        super(context, -1, objects);
        mInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemGender ItemGender = getItem(position);
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflate.inflate(R.layout.item_genre, null);
            viewHolder.mTextTitle = (TextView) convertView.findViewById(R.id.txt_title);
            viewHolder.mImageCheck = (ImageView) convertView.findViewById(R.id.img_check);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (ItemGender.isSelected()) {
            viewHolder.mImageCheck.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mImageCheck.setVisibility(View.GONE);
        }
        viewHolder.mTextTitle.setText(ItemGender.getTitle());
        return convertView;
    }

    public void updateListItems(int position) {
        for (int i = 0, n = getCount(); i < n; i++) {
            if (i != position) {
                if (getItem(i).isSelected()) {
                    getItem(i).updateSelected();
                }
            }
        }
        getItem(position).setSelected(true);
    }

    private class ViewHolder {
        TextView mTextTitle;
        ImageView mImageCheck;
    }
}
