package portalbeanz.com.doublefoot.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by datnx on 12/9/14.
 */
public class FontUtils {
    private static FontUtils instance;
    private Typeface typeface;
    private Typeface typefaceBold;
    private Typeface typefaceSemiBold;

    public final static FontUtils getInstance(){
        if(null == instance){
            instance = new FontUtils();
        }
        return instance;
    }

    public final void initFonts(Context context){
        typeface =  Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans_Regular.ttf");
        typefaceSemiBold =  Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans_Semibold.ttf");
        typefaceBold =  Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans_Bold.ttf");
    }

    public final Typeface getTypeface(){
        return typeface;
    }

    public final Typeface getTypefaceBold(){
        return typefaceBold;
    }

    public final Typeface getTypefaceSemiBold(){
        return typefaceSemiBold;
    }

}
