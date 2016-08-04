package portalbeanz.com.doublefoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.model.ItemUser;
import portalbeanz.com.doublefoot.view.CircleImageView;
import portalbeanz.com.doublefoot.view.OpenSanTextView;
import portalbeanz.com.doublefoot.view.OpenSanTextViewSemiBold;

/**
 * Created by thangit14 on 6/12/16.
 */
public class AdapterItemUser extends ArrayAdapter<ItemUser> {
    private LayoutInflater layoutInflater;
    private OnBookClickListener onBookClickListener;

    public AdapterItemUser(Context context, ArrayList<ItemUser> list) {
        super(context, -1, list);
//        this.onBookClickListener = onBookClickListener;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ItemUser itemUser = getItem(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_masseur_in_schedule, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtName.setText(itemUser.getDisplayName());
        viewHolder.txtWorkingExperience.setText(itemUser.getWorkingExperience() + " " + getContext().getString(R.string.year));
        Glide.with(getContext())
                .load(itemUser.getAvatarUrl())
//                .placeholder(R.drawable.avatar_default)
//                .crossFade()
                .into(viewHolder.imgAvatar);

//        viewHolder.btnBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBookClickListener.bookClick(position);
//            }
//        });

        return convertView;
    }

    public interface OnBookClickListener {
        void bookClick(int position);
    }

    static class ViewHolder {
        @Bind(R.id.img_avatar)
        CircleImageView imgAvatar;
        //        @Bind(R.id.btn_book)
//        OpenSanButton btnBook;
        @Bind(R.id.txt_name)
        OpenSanTextViewSemiBold txtName;
        @Bind(R.id.txt_working_experience)
        OpenSanTextView txtWorkingExperience;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
