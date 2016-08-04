package portalbeanz.com.doublefoot.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import portalbeanz.com.doublefoot.util.FontUtils;


/**
 * Created by datnx on 12/27/14.
 */
public class OpenSanTextViewSemiBold extends TextView {

    public OpenSanTextViewSemiBold(Context context) {
        super(context);
        initFont();
    }

    public OpenSanTextViewSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public OpenSanTextViewSemiBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont();
    }

    private void initFont() {
        Typeface font = FontUtils.getInstance().getTypefaceSemiBold();
        this.setTypeface(font);
    }
}
