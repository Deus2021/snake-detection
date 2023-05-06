package com.udom.myapplication;

import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

public class RoundedBottomOutlineProvider extends ViewOutlineProvider {

    private final float radius;

    public RoundedBottomOutlineProvider(float radius) {
        this.radius = radius;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect(0, 0, view.getWidth(), view.getHeight());
        Path path = new Path();
        path.addRoundRect(new RectF(rect), radius, radius, Path.Direction.CW);
        outline.setPath(path);
    }
}
