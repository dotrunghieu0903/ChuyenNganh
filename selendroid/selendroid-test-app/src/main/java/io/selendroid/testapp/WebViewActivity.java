/*
 * Copyright 2012-2014 eBay Software Foundation and selendroid committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.selendroid.testapp;

import io.selendroid.testapp.server.HttpServer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class WebViewActivity extends Activity {
  private HttpServer server = null;
  private WebView mainWebView = null;
  private Spinner testDataSpinner = null;
  private ArrayAdapter<SpinnerItem> arrayAdapter = null;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    server = HttpServer.getInstance();

    setContentView(io.selendroid.testapp.R.layout.webview);

    mainWebView = (WebView) findViewById(io.selendroid.testapp.R.id.mainWebView);
    mainWebView.setWebViewClient(new TestAppWebViewClient());
    testDataSpinner =
        (Spinner) findViewById(io.selendroid.testapp.R.id.spinner_webdriver_test_data);
    arrayAdapter =
        new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item,
            new ArrayList<SpinnerItem>());
    arrayAdapter.add(new SpinnerItem("GOOGLE.COM.VN", "http://google.com.vn/"));
    arrayAdapter.add(new SpinnerItem("VNEXPRESS.NET", "http://vnexpress.net/"));
    arrayAdapter.add(new SpinnerItem("DAA.UIT.EDU.VN", "http://daa.uit.edu.vn/"));
    arrayAdapter.add(new SpinnerItem("24H.COM.VN", "http://24h.com.vn/"));


    testDataSpinner.setAdapter(arrayAdapter);
    testDataSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        SpinnerItem item = (SpinnerItem) testDataSpinner.getSelectedItem();
        mainWebView.loadUrl(item.url);
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0) {
        SpinnerItem item = (SpinnerItem) testDataSpinner.getSelectedItem();
        mainWebView.loadUrl(item.url);
      }
    });
    super.onCreate(savedInstanceState);
  }

  private class TestAppWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      ((TextView)findViewById(R.id.webviewLocation)).setText(url);
      return super.shouldOverrideUrlLoading(view, url);
    }
  }

  @Override
  protected void onStart() {
    mainWebView.loadUrl("about:blank");
    super.onStart();
  }

  public void showHomeScreenDialog(View view) {
    Intent nextScreen = new Intent(getApplicationContext(), HomeScreenActivity.class);
    startActivity(nextScreen);
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
