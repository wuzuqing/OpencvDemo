package com.example.module_orc;

import android.graphics.Bitmap;

import org.opencv.core.Rect;
import org.opencv.core.Size;

public class IDCardDiscern extends AbsDiscern {

    public IDCardDiscern(Bitmap bitmap1, String langName, IDiscernCallback callback) {
        super(bitmap1, langName, callback);
        mSize = new Size(640, 400);
        thresh = 165;
    }

    @Override
    protected boolean ignoreRect(Rect rect) {
        return rect.width == 640 || rect.x < 110 || rect.x > 400 || rect.height > 45 || rect.height < 20;
    }
    private int idHeight=340;
    private int mingZuX = 190;
    @Override
    protected OrcModel createOrdModel(Rect rect, Bitmap bitmap) {
        if (rect.y>idHeight){
            return new OrcModel(rect, OrcHelper.getInstance().orcText(bitmap, "id"), bitmap);
        }else if (rect.x >mingZuX){
            return new OrcModel(rect, OrcHelper.getInstance().orcText(bitmap, langName), bitmap);
        }
        return super.createOrdModel(rect, bitmap);
    }
}
