(function(){
 function fix(){
  document.querySelectorAll('iframe[src*="book.pdf"]').forEach(function(f){
   var m=(f.getAttribute('src')||'').match(/page=(\d+)/),p=m?+m[1]:1;
   var b=document.createElement('button');b.textContent='نمایش صفحه '+p+' کتاب';b.className='btn primary';b.style.cssText='width:100%;min-height:100px';b.onclick=function(){NetLabAndroid.openPdf(p)};f.replaceWith(b);
  });
  document.querySelectorAll('a[href*="book.pdf"]').forEach(function(a){a.onclick=function(e){e.preventDefault();NetLabAndroid.openPdf(1)}});
 }
 new MutationObserver(fix).observe(document.body,{childList:true,subtree:true});fix();
 var old=window.readerGoto;if(old){window.readerGoto=function(p){old(p);fix()}}
})();