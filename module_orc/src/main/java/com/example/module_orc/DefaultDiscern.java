package com.example.module_orc;

import android.graphics.Bitmap;

import org.opencv.core.Rect;
import org.opencv.core.Size;

public class DefaultDiscern extends AbsDiscern {

    public DefaultDiscern(Bitmap bitmap1, String langName, IDiscernCallback callback) {
        super(bitmap1, langName, callback);
        mSize = new Size(1080, 1920);
        thresh = 165;
    }

    @Override
    protected boolean ignoreRect(Rect rect) {
        return false;
    }
    @Override
    protected OrcModel createOrdModel(Rect rect, Bitmap bitmap) {
//        if (rect.y>idHeight){
//            return new OrcModel(rect, OrcHelper.getInstance().orcText(bitmap, "id"), bitmap);
//        }else if (rect.x >mingZuX){
//            return new OrcModel(rect, OrcHelper.getInstance().orcText(bitmap, langName), bitmap);
//        }
        return super.createOrdModel(rect, bitmap);
    }
}
