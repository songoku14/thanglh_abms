package portalbeanz.com.doublefoot.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import portalbeanz.com.doublefoot.util.FontUtils;

/**
 * Created by datnx on 12/9/14.
 */
public class OpenSanEditText extends EditText {
    public OpenSanEditText(Context context) {
        super(context);
        initFont();
    }



    public OpenSanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public OpenSanEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initFont();
    }

    private void initFont() {
        Typeface font = FontUtils.getInstance().getTypeface();
        this.setTypeface(font);
    }
}
