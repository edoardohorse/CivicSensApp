package com.civic.gercs.civicsense;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;


import static com.civic.gercs.civicsense.UserReportActivity.getResizedBitmap;

public class ImageFull extends Fragment {

    ViewGroup cont;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        cont = container;

        View view = inflater.inflate(R.layout.image_full, container, false);
        ImageView v = (ImageView) view.findViewById(R.id.image_full);
        v.setImageBitmap(null);
        String str = getTag();
        Uri uri = Uri.parse(str);
        try{
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            float widthScreen = size.x;

            float widthBitamp;
            float heigthBitamp;
            float ratio;

            InputStream inputStream = inflater.getContext().getContentResolver().openInputStream(uri);
            Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);


            ratio = (float) myBitmap.getHeight()/myBitmap.getWidth();
            heigthBitamp = widthScreen*ratio;


            myBitmap = getResizedBitmap(myBitmap, (int) widthScreen, (int) heigthBitamp);
            v.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            v.setImageBitmap(myBitmap);
            container.setVisibility(View.VISIBLE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onDestroy() {
        cont.setVisibility(View.GONE);
        super.onDestroy();

    }

}
