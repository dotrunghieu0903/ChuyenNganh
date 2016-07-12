package io.selendroid.testapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import io.selendroid.testapp.server.HttpServer;

/**
 * Created by Minh on 04/06/2016.
 */
public class RecognitionActivity extends Activity {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    private EditText metTextHint;
    private TextView metTextURL;
    private Button mbtSpeak;
    private String text="";
    private String urlCode="code";
    private String urlText="url";
    private Button goback;
    private HttpServer server = null;
    private WebView mainWebsiteView = null;
    private Spinner testDataSpinner = null;
    private ArrayAdapter<SpinnerItem> arrayAdapter = null;
    private Boolean buttonState = false;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.voice_recognition);
        metTextHint = (EditText) findViewById(R.id.etTextHint);
        mbtSpeak = (Button) findViewById(R.id.btSpeak);
        metTextURL = (TextView) findViewById(R.id.websiteviewLocation);
        mainWebsiteView = (WebView) findViewById(io.selendroid.testapp.R.id.mainWebsiteView);
        mainWebsiteView.setWebViewClient(new WebViewClient());
        mainWebsiteView.getSettings().setJavaScriptEnabled(true);
//        mainWebsiteView.loadUrl("javascript:alert(\"ok\")");
        mainWebsiteView.getSettings().setDomStorageEnabled(true);
//        mainWebsiteView.loadUrl("file:///assets/js.html");

        goback = (Button) findViewById(R.id.goBack);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = mainWebsiteView.getUrl();
                url = url.concat("/?gws_rd=ssl#q=vietnam");
                mainWebsiteView.loadUrl(url);

            }
        });
        url = mainWebsiteView.getUrl();
        metTextURL.setText(url);
        server = HttpServer.getInstance();

        ////////////////////////////////////////////////////////////
        //////////////////   Recognition   /////////////////////////
        ////////////////////////////////////////////////////////////

//        testDataSpinner =
//                (Spinner) findViewById(io.selendroid.testapp.R.id.spinner_webdriver_data);
        arrayAdapter =
                new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item,
                        new ArrayList<SpinnerItem>());
        arrayAdapter.add(new SpinnerItem("GOOGLE.COM.VN", "http://google.com.vn/"));
        arrayAdapter.add(new SpinnerItem("VNEXPRESS.NET", "http://vnexpress.net/"));
        arrayAdapter.add(new SpinnerItem("DAA.UIT.EDU.VN", "http://daa.uit.edu.vn/"));
        arrayAdapter.add(new SpinnerItem("24H.COM.VN", "http://24h.com.vn/"));


//        testDataSpinner.setAdapter(arrayAdapter);
//        testDataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                SpinnerItem item = (SpinnerItem) testDataSpinner.getSelectedItem();
//                mainWebsiteView.loadUrl(item.url);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                SpinnerItem item = (SpinnerItem) testDataSpinner.getSelectedItem();
//                mainWebsiteView.loadUrl(item.url);
//            }
//        });
        mbtSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        mainWebsiteView.loadUrl("http://vnexpress.net");
        super.onCreate(savedInstanceState);
    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case VOICE_RECOGNITION_REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    metTextHint.setText(result.get(0));
                    text="ne";
                    if (result.get(0).equals(text)) {
                        urlText = "http://google.com.vn";
                    }
                    urlCode = result.toString();
                    int code = -1;
                    String [] arr = {"go back","back","call back","come back","hack","Come Back", "callback"};
                    String [] arr2 = {"next","forward","go forward","net","nghe"};
                    mainWebsiteView.loadData(urlCode,"text/html; charset=UTF-8", null);
                    for (String s : arr){
                        //go Back
                        code = urlCode.indexOf(s);
                        if (code != -1)
                            mainWebsiteView.goBack();
                    }
                    for (String s : arr2){
                        //go Forward
                        int code2 = urlCode.indexOf(s);
                        if (code2 != -1)
                            mainWebsiteView.goForward();
                    }
                    if (urlText == "http://google.com.vn") {
                        mainWebsiteView.loadUrl(urlText);
                    }
                }
                break;
            }
//        switch (requestCode) {
//            case VOICE_RECOGNITION_REQUEST_CODE: {
//                if (resultCode == RESULT_OK && null != data) {
//
//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    metTextHint.setText(result.get(0));
//                    text="Ok Google";
//                    String [][] arrayData = new String[2][5];
//                    arrayData[0][0]= "google";
//                    arrayData[0][1]= "facebook";
//                    arrayData[0][2]= "vnexpress";
//                    arrayData[0][3]= "thanhnien";
//                    arrayData[0][4]= "tuoitre";
//                    for (int j=0;j<5;j++){
//                        if (arrayData[0][j]== "google")
//                            arrayData[1][j] = "http://google.com.vn/";
//                        if (arrayData[0][j]== "facebook")
//                            arrayData[1][j] = "http://facebook.com/";
//                        if (arrayData[0][j]== "google")
//                            arrayData[1][j] = "http://vnexpress.net/";
//                        if (arrayData[0][j]== "google")
//                            arrayData[1][j] = "http://thanhnien.vn/";
//                        if (arrayData[0][j]== "tuoitre")
//                            arrayData[1][j] = "http://tuoitre.vn/";
//                    }
//                    String textGoogle = "Ok Google, ok google, ok";
//                    for (int i=0;i<2;i++){
//                        for (int j=0;j<5;j++){
//                            if (arrayData[i][j].indexOf(result.get(0)) != 0);
//                            text = arrayData[i][j];
//                        }
//                    }
//                    //text = result.toString();
//                    metTextOut.setText(text);
//                }
//                break;
//            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////      Recognition     ///////////////////////////////////
    private class TestAppWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            ((TextView)findViewById(R.id.webviewLocation)).setText(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    protected void onStart() {
        mainWebsiteView.setWebViewClient(new WebViewClient());
//        mainWebsiteView.loadUrl("about.bank");
        mainWebsiteView.loadUrl("http://google.com.vn");


        super.onStart();
    }

    public void showHomeScreenDialog(View view) {
//        Intent nextScreen = new Intent(getApplicationContext(), HomeScreenActivity.class);
//        startActivity(nextScreen);
    }

    public class SpinnerItem {
        private String text;
        public String url;

        SpinnerItem(String text, String url) {
            this.text = text;
            this.url = url;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
