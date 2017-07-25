package com.example.pc.resttest1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class MemUpdateActivity extends AppCompatActivity {

    private EditText edtJoinName, edtJoinId, edtJoinPw, edtJoinHp;
    private ImageView mImgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_update);

        MemberBean.MemberBeanSub memberBean = (MemberBean.MemberBeanSub)
                                                getIntent().getSerializableExtra("memberBean");

        edtJoinName = (EditText)findViewById(R.id.edtJoinName);
        edtJoinId = (EditText)findViewById(R.id.edtJoinId);
        edtJoinPw = (EditText)findViewById(R.id.edtJoinPw);
        edtJoinHp = (EditText)findViewById(R.id.edtJoinHp);
        mImgProfile = (ImageView)findViewById(R.id.imgProfile);

        edtJoinName.setText( memberBean.getName() );
        edtJoinId.setText( memberBean.getUserId() );
        edtJoinPw.setText( memberBean.getUserPw() );
        edtJoinHp.setText( memberBean.getHp() );

        new ImageLoaderTask(mImgProfile)
                .execute(Constants.BASE_URL + memberBean.getProfileImg());

    }//end onCreate

    //이미지 비동기 로딩 Task
    class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView dispImageView;

        public ImageLoaderTask(ImageView dispImgView) {
            this.dispImageView =dispImgView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String imgUrl = params[0];

            Bitmap bmp = null;

            try {
                bmp = BitmapFactory.decodeStream(  (InputStream)new URL(imgUrl).getContent()  );
            } catch (Exception e) {
                e.printStackTrace();
            }

            return bmp;
        }//end doInBackground()

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                //표시
                dispImageView.setImageBitmap(bitmap);
            }
        }

    };


}
