package ir.teachfuture.netlab;
import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.webkit.*;
import android.view.*;
import android.widget.*;
import android.net.Uri;
import android.content.pm.ActivityInfo;

public class MainActivity extends Activity {
 WebView web;
 @Override public void onCreate(Bundle b){super.onCreate(b);
 web=new WebView(this); web.setBackgroundColor(Color.rgb(11,18,32)); setContentView(web);
 WebSettings s=web.getSettings(); s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setAllowFileAccess(true);s.setAllowFileAccessFromFileURLs(true);
 web.addJavascriptInterface(new Object(){@JavascriptInterface public void openPdf(int page){runOnUiThread(()->{Intent i=new Intent(MainActivity.this,PdfActivity.class);i.putExtra("page",page);startActivity(i);});}},"NetLabAndroid");
 web.setWebChromeClient(new WebChromeClient());
 web.setWebViewClient(new WebViewClient(){
 @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){String url=request.getUrl().toString();if(url.contains("book.pdf")){openPdf(1);return true;}if(url.startsWith("http")){startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));return true;}return false;}
 @Override public void onPageFinished(WebView v,String u){try(java.io.InputStream in=getAssets().open("android-reader.js")){byte[] bytes=new byte[in.available()];in.read(bytes);v.evaluateJavascript(new String(bytes,java.nio.charset.StandardCharsets.UTF_8),null);}catch(Exception ignored){}}

 });
 web.setDownloadListener((url,userAgent,contentDisposition,mimeType,contentLength)->{
 if(url.startsWith("data:image/png")){Toast.makeText(this,"برای ذخیره طرح درس، از قابلیت اشتراک‌گذاری یا خروجی مرورگر استفاده کنید.",Toast.LENGTH_LONG).show();}
 });
 web.loadUrl("file:///android_asset/index.html");
 }
 void openPdf(int p){Intent i=new Intent(this,PdfActivity.class);i.putExtra("page",p);startActivity(i);}
 @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
}
