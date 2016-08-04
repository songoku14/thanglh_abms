package portalbeanz.com.doublefoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hb.views.PinnedSectionListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.model.ItemAppointment;
import portalbeanz.com.doublefoot.util.Utils;
import portalbeanz.com.doublefoot.view.OpenSanTextView;

/**
 * Created by thangit14 on 6/10/16.
 */
public class AdapterListAppointment extends ArrayAdapter<ItemAppointment> implements PinnedSectionListView.PinnedSectionListAdapter {
    private LayoutInflater layoutInflater;
    private boolean showMasseur;

    public AdapterListAppointment(Context context, ArrayList<ItemAppointment> list, boolean showMasseur) {
        super(context, -1, list);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showMasseur = showMasseur;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemAppointment item = getItem(position);

        if (getItemViewType(position) == 0) {
            ViewHolderItem viewHolderItem;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_appointment, parent, false);
                viewHolderItem = new ViewHolderItem(convertView);

                convertView.setTag(viewHolderItem);

            } else {
                viewHolderItem = (ViewHolderItem) convertView.getTag();
            }

            if (showMasseur) {

                Glide.with(getContext()).load(item.getItemUser().getAvatarUrl()).
                        into(viewHolderItem.imgAvatar);

                Glide.with(getContext())
                        .load(item.getItemUser().getAvatarUrl())
//                        .placeholder(R.drawable.avatar_default)
//                        .crossFade()
                        .into(viewHolderItem.imgAvatar);

                viewHolderItem.txtName.setText(item.getItemUser().getDisplayName());
            } else {
                viewHolderItem.imgAvatar.setImageResource(R.drawable.avatar_default);
                viewHolderItem.txtName.setText(item.getItemUser().getDisplayName());
            }
            viewHolderItem.txtTime.setText(Utils.getDefaultFormatTimeString(item.getStartDate(), getContext()));
            viewHolderItem.txtAmPm.setText(Utils.getDefaultFormatAM_PMString(item.getStartDate(), getContext()));
        } else {
            ViewHolderSection viewHolderSection;
            if (convertView == null) {
                viewHolderSection = new ViewHolderSection();
                convertView = layoutInflater.inflate(R.layout.section_item_appointment, parent, false);

                viewHolderSection.txtSection = (TextView) convertView.findViewById(R.id.txt_section);

                convertView.setTag(viewHolderSection);
            } else {
                viewHolderSection = (ViewHolderSection) convertView.getTag();
            }
            viewHolderSection.txtSection.setText(
                    Utils.getDefaultFormatDateString(item.getStartDate(), getContext()));

        }
        return convertView;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == ItemAppointment.SECTION;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

    class ViewHolderItem {
        @Bind(R.id.txt_time)
        OpenSanTextView txtTime;
        @Bind(R.id.txt_am_pm)
        OpenSanTextView txtAmPm;
        @Bind(R.id.txt_name)
        OpenSanTextView txtName;
        @Bind(R.id.img_avatar)
        ImageView imgAvatar;

        ViewHolderItem(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class ViewHolderSection {
        private TextView txtSection;
    }

}
