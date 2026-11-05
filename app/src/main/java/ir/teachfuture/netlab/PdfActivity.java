package ir.teachfuture.netlab;
import android.app.*;import android.os.*;import android.graphics.*;import android.graphics.pdf.PdfRenderer;import android.view.*;import android.widget.*;import android.graphics.drawable.*;import android.content.*;import java.io.*;
public class PdfActivity extends Activity {
 PdfRenderer pdf;ParcelFileDescriptor fd;ImageView image;TextView status;int page=0;float zoom=1;
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.rgb(11,18,32));
 LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(Color.rgb(11,18,32));
 LinearLayout bar=new LinearLayout(this);bar.setGravity(Gravity.CENTER);bar.setPadding(5,10,5,10);
 Button prev=new Button(this);prev.setText("قبلی");Button next=new Button(this);next.setText("بعدی");Button close=new Button(this);close.setText("بازگشت");status=new TextView(this);status.setTextColor(-1);status.setGravity(Gravity.CENTER);
 bar.addView(prev,new LinearLayout.LayoutParams(0,-2,1));bar.addView(status,new LinearLayout.LayoutParams(0,-2,1));bar.addView(next,new LinearLayout.LayoutParams(0,-2,1));bar.addView(close,new LinearLayout.LayoutParams(0,-2,1));root.addView(bar);
 ScrollView sc=new ScrollView(this);image=new ImageView(this);image.setAdjustViewBounds(true);sc.addView(image);root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));setContentView(root);
 try {File f=new File(getCacheDir(),"netlab_book.pdf");if(!f.exists()||f.length()==0){try(InputStream in=getAssets().open("book.pdf");OutputStream out=new FileOutputStream(f)){byte[] buf=new byte[65536];int n;while((n=in.read(buf))!=-1)out.write(buf,0,n);}}fd=ParcelFileDescriptor.open(f,ParcelFileDescriptor.MODE_READ_ONLY);pdf=new PdfRenderer(fd);page=Math.max(0,Math.min(pdf.getPageCount()-1,getIntent().getIntExtra("page",1)-1));showPage();}
 catch(Exception e){status.setText("خطا در باز کردن کتاب");Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();}
 prev.setOnClickListener(v->{if(page>0){page--;showPage();}});next.setOnClickListener(v->{if(pdf!=null&&page<pdf.getPageCount()-1){page++;showPage();}});close.setOnClickListener(v->finish());
 image.setOnClickListener(v->{zoom=zoom==1?1.6f:1;showPage();});
 }
 void showPage(){if(pdf==null)return;try(PdfRenderer.Page p=pdf.openPage(page)){int w=(int)(getResources().getDisplayMetrics().widthPixels*zoom);w=Math.min(2600,Math.max(600,w));int h=(int)((double)w*p.getHeight()/p.getWidth());Bitmap bmp=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);bmp.eraseColor(Color.WHITE);p.render(bmp,null,null,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);image.setImageBitmap(bmp);status.setText((page+1)+" / "+pdf.getPageCount());}catch(Exception e){status.setText("خطای نمایش صفحه");}}
 @Override protected void onDestroy(){super.onDestroy();try{if(pdf!=null)pdf.close();if(fd!=null)fd.close();}catch(Exception ignored){}}
}
