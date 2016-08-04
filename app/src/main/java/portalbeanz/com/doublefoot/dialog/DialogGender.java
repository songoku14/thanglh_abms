package portalbeanz.com.doublefoot.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import portalbeanz.com.doublefoot.R;
import portalbeanz.com.doublefoot.adapter.AdapterGender;
import portalbeanz.com.doublefoot.model.ItemGender;

/**
 * Created by datnx on 12/16/14.
 */
public class DialogGender extends BaseDialog implements View.OnClickListener {
    public static final String ITEM_SELECTED="ITEM GENRE SELECTED";
    public static final String RETURN_FRAGMENT="RETURN FRAGMENT";

    private ArrayList<ItemGender> itemGenders;
    private ListView listView;
    private AdapterGender adapterGender;

    private OnCompleteListener mListener;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            adapterGender.getItem(position).setSelected(true);
            mItemSelected = adapterGender.getItem(position);
            adapterGender.updateListItems(position);
            adapterGender.notifyDataSetChanged();
        }
    };
    private boolean mReturnToFragment;
    private ItemGender mOldItem;
    private ItemGender mItemSelected;

    @Override
    protected void initViews(View rootView, Bundle savedInstanceState) {
        listView = (ListView) rootView.findViewById(R.id.list_view);
        itemGenders = new ArrayList<ItemGender>();
        itemGenders.add(new ItemGender(ItemGender.MALE,getString(R.string.male), true));
        itemGenders.add(new ItemGender(ItemGender.FEMALE,getString(R.string.female), false));
        adapterGender = new AdapterGender(getActivity().getApplicationContext(), itemGenders);
        listView.setAdapter(adapterGender);

        mReturnToFragment = getArguments().getBoolean(RETURN_FRAGMENT);

        mItemSelected =(ItemGender) getArguments().getSerializable(ITEM_SELECTED);
        mOldItem = mItemSelected;
        if (mItemSelected.getGender() == ItemGender.FEMALE) {
            adapterGender.updateListItems(1);
        }

        listView.setOnItemClickListener(onItemClickListener);
        setOnNegativeListener(this);
        setOnPositiveListener(this);

        setTitleDialog(getString( R.string.gender));
    }

    @Override
    protected int getLayoutDialog() {
        return R.layout.dialog_gender;
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonPositive && null != itemGenders) {
            if (mReturnToFragment == false) {
                if (mOldItem.getGender() != mItemSelected.getGender()) {
                    this.mListener.onComplete(mItemSelected);
                }
            } else {
                if (mOldItem.getGender() != mItemSelected.getGender()) {
                    Intent intent = getActivity().getIntent();
                    intent.putExtra(DialogGender.ITEM_SELECTED, mItemSelected);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                }
            }
        }
        dismiss();
    }

    public static interface OnCompleteListener {
        public abstract void onComplete(ItemGender ItemGender);
    }

    //    make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getTargetFragment()==null){
            try {
                this.mListener = (OnCompleteListener)activity;
            }
            catch (final ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
            }
        }
    }

}
