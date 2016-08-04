package portalbeanz.com.doublefoot.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import portalbeanz.com.doublefoot.util.FontUtils;

/**
 * Created by thangit14 on 6/13/16.
 */
public class OpenSanEditTextSemiBold extends EditText {
    public OpenSanEditTextSemiBold(Context context) {
        super(context);
        initFont();
    }



    public OpenSanEditTextSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public OpenSanEditTextSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFont();
    }

    private void initFont() {
        Typeface font = FontUtils.getInstance().getTypefaceSemiBold();
        this.setTypeface(font);
    }
}
