package com.jar.jupo.randomchat;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;

/**
 * Created by KimYS on 2016-09-25.
 */
public class MainActivity extends Activity {


    private EditText mEditText;
    private TextView mTextView;
    private Button mButton;
    private Button mButton2;
    private LinearLayout mLinearLayout;
    private Context mContext;

    private OutputStream os = null;
    private ClientThread clientThread;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);

        mContext = this;

        mEditText = (EditText) findViewById(R.id.et_jar);
        mTextView = (TextView) findViewById(R.id.tv_jar2);
        mButton = (Button) findViewById(R.id.button);
        mButton2 = (Button) findViewById(R.id.button2);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_jar_adddddd);

        mTextView.setText("GOGO~");
        mButton.setOnClickListener(jarbtnListner);
        mButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = new TextView(mContext);
                clickcount++;
                tv.setText("aaaaa" + Integer.toString(clickcount));
                mLinearLayout.addView(tv);
            }
        });


        clientThread = new ClientThread();
        clientThread.start();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private boolean flag = false;
    private int clickcount = 0;

    private OnClickListener jarbtnListner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (os != null) {
                String s = mEditText.getText().toString();
                mEditText.setText("");
                byte b[] = s.getBytes();
                try {
                    os.write(b, 0, s.length());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class ClientThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                String serverIP = "172.30.1.45"; // 127.0.0.1 & localhost 본인
                Log.d("jar@", "서버에 연결중입니다. 서버 IP : " + serverIP);
                // 소켓을 생성하여 연결을 요청한다.
                Socket socket = new Socket(serverIP, 18576);
                // 소켓의 입력스트림을 얻는다.
                InputStream in = socket.getInputStream();
                os = socket.getOutputStream();
                Thread a = new RecvThread(in);
                a.start();
            } catch (ConnectException ce) {
                ce.printStackTrace();
            } catch (IOException ie) {
                ie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } // try - catch
        }
    }

    class RecvThread extends Thread {
        InputStream in;

        public RecvThread(InputStream a) {
            this.in = a;
        }

        public void run() {
            byte inb[] = new byte[512];

            try {
                while (true) {
                    Arrays.fill(inb, (byte) 0);
                    int a = in.read(inb, 0, 512);
                    if (a != 0) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = new String(inb);
                        mHandler.sendMessage(msg);
                    } else {
                        break;
                    }
                }
            } catch (ConnectException ce) {
                ce.printStackTrace();
            } catch (IOException ie) {
                ie.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } // try - catch
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String recvString = msg.obj.toString();
                    String s = mTextView.getText().toString();
                    s += "\n" + recvString;
                    mTextView.setText(s);
                    super.handleMessage(msg);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
