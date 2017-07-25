package com.example.pc.resttest1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class JoinActivity extends AppCompatActivity {

    private EditText edtJoinName, edtJoinId, edtJoinPw, edtJoinHp;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        edtJoinName = (EditText)findViewById(R.id.edtJoinName);
        edtJoinId = (EditText)findViewById(R.id.edtJoinId);
        edtJoinPw = (EditText)findViewById(R.id.edtJoinPw);
        edtJoinHp = (EditText)findViewById(R.id.edtJoinHp);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        findViewById(R.id.btnJoinOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JoinTask().execute();
            }
        });

    }//onCreate


    //회원가입 처리
    private class JoinTask extends AsyncTask<String, Void, String> {

        public static final String URL_LOGIN_PROC = "http://117.17.93.212:8080/rest/insertMember.do";
        private String userId, userPw, name, hp, addr;

        @Override
        protected void onPreExecute() {
            //프로그레스 다이얼로그 표시
            mProgressBar.setVisibility(View.VISIBLE);

            name = edtJoinName.getText().toString();
            userId = edtJoinId.getText().toString();
            userPw = edtJoinPw.getText().toString();
            hp = edtJoinHp.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                RestTemplate restTemplate = new RestTemplate();
                //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                map.add("userId", userId);
                map.add("userPw", userPw);
                map.add("name", name);
                map.add("hp", hp);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);

                HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_LOGIN_PROC, request, String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            mProgressBar.setVisibility(View.INVISIBLE);

            Gson gson = new Gson();
            try {
                MemberBean bean = gson.fromJson(s, MemberBean.class);
                if(bean != null) {
                    if( bean.getResult().equals("ok") ) {
                        //로그인 성공
                        Intent i = new Intent(JoinActivity.this, LoginSuccActivity.class);
                        startActivity(i);
                    } else {
                        //로그인 실패
                        Toast.makeText(JoinActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(JoinActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    };


}
