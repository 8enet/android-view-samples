package com.zzzmode.android.views;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.zzzmode.android.library.LayoutSizeCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutSizeCompat.init(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initView();
    }

    private void initView(){
        int imgW=LayoutHelper.Layout1080x1920.w(150);
        int imgH=LayoutHelper.Layout1080x1920.h(150);

        RoundContentView rcView1= (RoundContentView) findViewById(R.id.id_rcv_1);
        rcView1.setText("Github");
        rcView1.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.test1));

        RoundContentView rcView2= (RoundContentView) findViewById(R.id.id_rcv_2);
        rcView2.setText("Github OpenSource ");
        rcView2.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.test2));

        RoundContentView rcView3= (RoundContentView) findViewById(R.id.id_rcv_3);
        rcView3.setText("Github OpenSource Android");
        rcView3.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.test2));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
