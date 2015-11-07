package com.taru.taru;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by Shiran Maor on 11/7/15.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        try
        {
            ImageView img = (ImageView)findViewById(R.id.login_back);

            InputStream open = getAssets().open("loginscreen.png");
            Bitmap bitmap = BitmapFactory.decodeStream(open);
             img.setImageBitmap(bitmap);

            EditText viewById = (EditText) findViewById(R.id.userNameText);

            viewById.bringToFront();
            viewById.invalidate();

            viewById = (EditText) findViewById(R.id.password);
            viewById.bringToFront();
            viewById.invalidate();

            TextView button = (TextView) findViewById(R.id.sign_in_button);
            button.bringToFront();
            button.invalidate();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);

                }
            });
            img.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
