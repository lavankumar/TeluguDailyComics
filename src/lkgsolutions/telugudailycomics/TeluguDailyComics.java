package lkgsolutions.telugudailycomics;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.imagezoom.ImageAttacher;
import com.imagezoom.ImageAttacher.OnMatrixChangedListener;
import com.imagezoom.ImageAttacher.OnPhotoTapListener;



import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
//import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
//import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class TeluguDailyComics extends Activity  implements OnItemSelectedListener{

	// flag for Internet connection status
    Boolean isInternetPresent = false;
    
    String URL=null;
    String RealURL =null;
    // Connection detector class 
    ConnectionDetector cd;
   
    ImageLinkGrabberAndDownloader imageProcess;
   
	ProgressDialog myDialog;
 	String paper,rlink,slink; 
	URL url;
	ImageView image1,image2;
	WebView wb1;
	String[] papers = {"Select","Eenadu","Sakshi","Namaste Telangana","Vartha","Surya","Andhra Bhoomi","dc","Hindu","iexpress"};
	int arr_images[] = { R.drawable.allpapers1,R.drawable.eenadu,
            R.drawable.sakshi, R.drawable.namasthe,
            R.drawable.vartha, R.drawable.surya,R.drawable.andhraboomi,R.drawable.dc,R.drawable.hindu,R.drawable.indianexpress};;
            
             Calendar cal = Calendar.getInstance();
    	    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
    	    @SuppressLint("SimpleDateFormat") SimpleDateFormat d = new SimpleDateFormat("MM-dd-yyyy");
    	    String date, edate;
            
   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telugu_daily_comics);
		
		
		date = dateFormat.format(cal.getTime());
	    cal.add(Calendar.DATE, -1);
	    edate = d.format(cal.getTime());
	    System.out.printf("today date is :\t"+date+"\n\n");

	    Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	    spinner.setAdapter(new MyAdapter(TeluguDailyComics.this, R.layout.row, papers));
		
	    spinner.setOnItemSelectedListener(this);
		image1 = (ImageView) findViewById(R.id.imageView1);
		image2 = (ImageView) findViewById(R.id.imageView2);
		
		// creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
 
		// get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // instantiate it within the onCreate method
        myDialog = new ProgressDialog(TeluguDailyComics.this);
        myDialog.setMessage("Loading Cartoon of the Day");
        myDialog.setIndeterminate(true);
        myDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        myDialog.setCancelable(true);

       
      
    }
	
	@Override
	public void onDestroy(){
	  
		
	    if(myDialog != null)
	    	myDialog.dismiss();
	    myDialog = null;
	    super.onDestroy();
	}
	
	 @Override
	    public void onPause()
	    {
	        super.onPause();
	        if(myDialog != null)
	        	myDialog.dismiss();
	        myDialog = null;
	        
	    }
 

	
		

	 public class MyAdapter extends ArrayAdapter<String>{
		 
	        public MyAdapter(Context context, int textViewResourceId,   String[] objects) {
	            super(context, textViewResourceId, objects);
	        }
	 
	        @Override
	        public View getDropDownView(int position, View convertView,ViewGroup parent) {
	            return getCustomView(position, convertView, parent);
	        }
	 
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            return getCustomView(position, convertView, parent);
	        }
	 
	        public View getCustomView(int position, View convertView, ViewGroup parent) {
	 
	            LayoutInflater inflater=getLayoutInflater();
	            View row=inflater.inflate(R.layout.row, parent, false);
	            
	            ImageView icon=(ImageView)row.findViewById(R.id.rowimageView);
	            icon.setImageResource(arr_images[position]);
	 
	            return row;
	            }
	        }


	@SuppressLint("NewApi") @Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		
		 // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is  Present
            // make HTTP requests
        	if(parent.getItemAtPosition(pos).equals("Select"))
   		 	{
   			 Log.d("TDC : onItemSelected","No Paper Selected");
   			
   			 }
   		 if(parent.getItemAtPosition(pos).equals("Eenadu"))
   		 {
   			 Log.d("TDC : onItemSelected","Enadu Paper Selected");
   			 DisplayImage("Eenadu");
   			 usingSimpleImage(image2);

   	      }
   		  else if(parent.getItemAtPosition(pos).equals("Sakshi")) 
   		  {
   			  Log.d("TDC : onItemSelected","Sakshi Paper Selected");
   			  DisplayImage("Sakshi");
   			  usingSimpleImage(image2);
   		  }
   		  
   		  else if(parent.getItemAtPosition(pos).equals("Namaste Telangana")) 
   		  { 
   			  Log.d("TDC : onItemSelected","Namaste Telangana Paper Selected");
   			  DisplayImage("Namaste Telangana");
   			  usingSimpleImage(image2);
   		  }
   		  else if(parent.getItemAtPosition(pos).equals("Vartha")) 
   		  { 
   			  Log.d("TDC : onItemSelected","Vartha Paper Selected");
   			  DisplayImage("Vartha");
   			  usingSimpleImage(image2);
   		  }
   		  else if(parent.getItemAtPosition(pos).equals("Surya")) 
   		  { 
   			  Log.d("TDC : onItemSelected","Surya Paper Selected");
   			  DisplayImage("Surya");
   			  usingSimpleImage(image2);
   		  }
   		  else if(parent.getItemAtPosition(pos).equals("Andhra Jyothi")) 
   		  { 
   			  Log.d("TDC : onItemSelected","Andhra Jyothi Paper Selected");
   			  DisplayImage("Andhra Jyothi");
   			  usingSimpleImage(image2);
   		  }
   		  else if(parent.getItemAtPosition(pos).equals("Andhra Bhoomi")) 
 		  { 
 			  Log.d("TDC : onItemSelected","Andhra Bhoomi Paper Selected");
 			  DisplayImage("Andhra Bhoomi");
 			  usingSimpleImage(image2);
 		  }
   		  else if(parent.getItemAtPosition(pos).equals("dc")) 
		  { 
			  Log.d("TDC : onItemSelected","Deccan Chronicle Paper Selected");
			  DisplayImage("dc");
			  usingSimpleImage(image2);
		  }
   		  else if(parent.getItemAtPosition(pos).equals("Hindu")) 
		  { 
			  Log.d("TDC : onItemSelected","Hindu Paper Selected");
			  DisplayImage("Hindu");
			  usingSimpleImage(image2);
		  }
   		else if(parent.getItemAtPosition(pos).equals("iexpress")) 
		  { 
			  Log.d("TDC : onItemSelected","Indian Express Paper Selected");
			  DisplayImage("iexpress");
			  usingSimpleImage(image2);
		  }

        	
        }
        else
        {
        	// Internet Connection is not Present
            // Don't make HTTP requests
        	Context context = getApplicationContext();
        	CharSequence text = "Please Check your Internet Connection";
        	int duration = Toast.LENGTH_SHORT;

        	Toast toast = Toast.makeText(context, text, duration);
        	toast.show();
        	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }
        
				     
		
	}

	private void usingSimpleImage(ImageView image22) {
		ImageAttacher mAttacher = new ImageAttacher(image22);
        ImageAttacher.MAX_ZOOM = 3.0f; // Double the current Size
        ImageAttacher.MIN_ZOOM = 0.75f; // Half the current Size
        MatrixChangeListener mMaListener = new MatrixChangeListener();
        mAttacher.setOnMatrixChangeListener(mMaListener);
        PhotoTapListener mPhotoTap = new PhotoTapListener();
        mAttacher.setOnPhotoTapListener(mPhotoTap);
	}
	
	
	 private class PhotoTapListener implements OnPhotoTapListener {

	        @Override
	        public void onPhotoTap(View view, float x, float y) {
	        }
	    }

	    private class MatrixChangeListener implements OnMatrixChangedListener {

	        @Override
	        public void onMatrixChanged(RectF rect) {

	        }
	    }
	
	

 private void DisplayImage(String string) {
	
		paper = string;
		
		
		if(paper.contentEquals("Eenadu"))
		{
			Log.d("TDC : Display Image : Downloading ",paper);
			
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });

			
			//new ImageLinkGrabber().execute(paper);
			// Log.d("Link of the paper"+paper,slink);
			
			
		}
		else if(paper.contentEquals("Sakshi"))
		{
			Log.d("TDC : Display Image : Downloading ",paper);
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//new ImageDownloader().execute(RealURL);
			//slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/sakshi"+date+".jpg";
			//new ImageDownloader().execute(slink);
			//Drawable drawable = LoadImageFromWeb(slink);
	        //image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
			
		}
		else if(paper.contentEquals("Namaste Telangana"))
		{
			Log.d("TDC : Display Image : Downloading ",paper);

			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//new ImageDownloader().execute(RealURL);
			//slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/nTelangana"+date+".jpg";
			//new ImageDownloader().execute(slink);
			//Drawable drawable = LoadImageFromWeb(slink);
			//image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
			
		} 
		else if(paper.contentEquals("Vartha"))
		{
			Log.d("TDC : Display Image : Downloading ",paper);
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//new ImageDownloader().execute(RealURL);
			//slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/vartha"+date+".jpg";
			//new ImageDownloader().execute(slink);
	        //Drawable drawable = LoadImageFromWeb(slink);
	        //image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
			
		} 
		else if(paper.contentEquals("Surya"))
		{
			Log.d("TDC : Display Image : Downloading : ",paper);
			
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//new ImageDownloader().execute(RealURL);
			//slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/surya"+date+".jpg";
			//new ImageDownloader().execute(slink);
			//Drawable drawable = LoadImageFromWeb(slink);
	        //image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
				
		}
		else if(paper.contentEquals("Andhra Jyothi"))
		{
			Log.d("TDC : Display Image : Downloading : ",paper);
			
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//new ImageDownloader().execute(RealURL);
			//	slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/andJyothi"+date+".jpg";
			//	new ImageDownloader().execute(slink);
			//Drawable drawable = LoadImageFromWeb(slink);
	        //image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
			
		}
		else if(paper.contentEquals("Andhra Bhoomi"))
		{
			Log.d("TDC : Display Image : Downloading : ",paper);
			
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//	new ImageDownloader().execute(RealURL);
			//slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/andBhoomi"+date+".jpg";
			//new ImageDownloader().execute(slink);
			//Drawable drawable = LoadImageFromWeb(slink);
	        //image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
			
		}  
		else if(paper.contentEquals("dc"))
		{
			Log.d("TDC : Display Image : Downloading : ",paper);
			
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//new ImageDownloader().execute(RealURL);
			//slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/dc"+date+".jpg";
			//new ImageDownloader().execute(slink);
			//Drawable drawable = LoadImageFromWeb(slink);
	        //image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
			
		} 
		else if(paper.contentEquals("Hindu"))
		{
			Log.d("TDC : Display Image : Downloading : ",paper);
			
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//new ImageDownloader().execute(RealURL);
			//slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/hindu"+date+".jpg";
			//new ImageDownloader().execute(slink);
			//Drawable drawable = LoadImageFromWeb(slink);
	        //image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
			
		} 
		else if(paper.contentEquals("iexpress"))
		{
			Log.d("TDC : Display Image : Downloading : ",paper);
			
			imageProcess = new ImageLinkGrabberAndDownloader(TeluguDailyComics.this);
			imageProcess.execute(paper);
			
			myDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	                imageProcess.cancel(true);
	            }
	        });
			
			//new ImageDownloader().execute(RealURL);
			//slink = "http://i1356.photobucket.com/albums/q721/lavankumar33/VIJAYA-PC/PhotoBucket/TeluguComics/indiaexp"+date+".jpg";
			//new ImageDownloader().execute(slink);
			//Drawable drawable = LoadImageFromWeb(slink);
	        //image2.setImageDrawable(drawable);
			//Log.d("Link of the paper"+paper,slink);
			
		}
		
	}

 
 private class ImageLinkGrabberAndDownloader extends AsyncTask<String, Integer, Bitmap> {
	 
	 private Context context;

	    public ImageLinkGrabberAndDownloader(Context context) {
	        this.context = context;
	    }
	 
	 
		@Override
		protected Bitmap doInBackground(String... param) {
			// TODO Auto-generated method stub
			//Grabbing the Link
			RealURL = imagelinkGrabber(param[0]);
			// if (isCancelled()) return null;
			if(RealURL!=null && !RealURL.isEmpty())
			{
				return downloadBitmap(RealURL);
			}
			else
			{
				RealURL = "http://i1356.photobucket.com/albums/q721/lavankumar33/New-Logo.png";
				return downloadBitmap(RealURL);
			}
			
		}

		@Override
		protected void onPreExecute() {
			Log.d("TDC : Async-Task :ImageLinkGrabberAndDownloader", "onPreExecute Called");
			myDialog.show();
			  switch ( context.getResources().getConfiguration().orientation)
			  { 
			  case Configuration.ORIENTATION_PORTRAIT:     ((TeluguDailyComics) context).setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
			  											break;   
			  case Configuration.ORIENTATION_LANDSCAPE:     ((TeluguDailyComics) context).setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			  											break;     
			  } 
			  

		}
		
		@Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        // if we get here, length is known, now set indeterminate to false
	        myDialog.setIndeterminate(false);
	        myDialog.setMax(100);
	        myDialog.setProgress(progress[0]);
	    }

		
		
		@Override
		protected void onPostExecute(Bitmap result) {
			Log.d("TDC : Async-Task :ImageLinkGrabberAndDownloader", "onPostExecute Called");
			myDialog.dismiss();
			if(result!=null)
			image2.setImageBitmap(result);


		}
		
		
		private Bitmap downloadBitmap(String url) {
			
			// initialize the default HTTP client object
			final DefaultHttpClient client = new DefaultHttpClient();
			
			//forming a HttoGet request 
			final HttpGet getRequest = new HttpGet(url);
			try {
				
				HttpResponse response = client.execute(getRequest);

				//check 200 OK for success
				final int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.d("TDC : DownloadBitmap :", "Error " + statusCode + " while retrieving bitmap from " + url);
					return null;

				}
				
				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					
					try {
						// getting contents from the stream 
						inputStream = entity.getContent();
						
						// decoding stream data back into image Bitmap that android understands
						final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						
						
						
						Log.d("TDC : DownloadBitmap :","ok");
						return bitmap;
						
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
			 		}
				}
			} catch (Exception e) {
				// You Could provide a more explicit error message for IOException
				getRequest.abort();
				Log.e("TDC : DownloadBitmap :", "Something went wrong while retrieving bitmap from " + url + e.toString());
				Context context = getApplicationContext();
	        	CharSequence text = "No Image Available Sorry";
	        	int duration = Toast.LENGTH_SHORT;

	        	Toast toast = Toast.makeText(context, text, duration);
	        	toast.show();
	        	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				
			} 

			return null;
		}
	}
 
 
 
 
 

 

 /*	 private Drawable LoadImageFromWeb(String url)
	   {
	  try
	  {
	   InputStream is = (InputStream) new URL(url).getContent();
	   Drawable d = Drawable.createFromStream(is, "src name");
	   return d;
	  }catch (Exception e) {
	   System.out.println("Exc="+e);
	   return null;
	  }
	 }

*/
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}
    
    
    public String imagelinkGrabber(String templink) {
		try{
		if(templink.equals("Eenadu"))
		{	URL = null;
			Document doc = Jsoup.connect("http://www.eenadu.net/").timeout(10*1000).get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				if( s.contains("cart")) 
		        {
				  String li ="http://eenadu.net/"+s;
		          URL = li.replace("small", "big");
		         // System.out.println ("\nEnadu link :"+URL + "\n");	
		        }
				
			}
			
			}	//Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("Sakshi"))
		{
			URL = null;
			Document doc = Jsoup.connect("http://www.sakshi.com/").get();
			Elements links = doc.getElementsByTag("a");
			
			for (Element link : links) {
			  String linkHref = link.attr("href");
			  
			if(linkHref.contains("cartoon"))
			{
			  Element img = link.firstElementSibling();
			 if(img!=null)
			 {
				for(Node e : img.childNodes())
				{
				if(e.hasAttr("src"))
				{
					URL = e.attr("src");
					//System.out.println("\nSakshi Link :"+URL);
				}
				}
										
			 }
			 }
								
			}
			
			
			
		}  //Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("Surya"))
		{
			URL=null;
			Document doc = Jsoup.connect("http://www.suryaa.com/").timeout(10*1000).get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				if( s.contains("c1")) 
		        {
				  URL = s;
		          URL = URL.replace("c1", "c1b");
		         // System.out.println ("\nSuryaa link :"+URL);	
		        }
				
			}
			
		
		 }   //Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("Vartha"))
		{
			URL = null;
			Document doc = Jsoup.connect("http://www.vaartha.com/").timeout(10*000).get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				if( s.contains("CartoonImages")) 
		        {
				  URL = "http://www.vaartha.com/"+s;
		         // System.out.println ("\nVartha link :"+URL);	
		        }
				
			}
			
		
		}  //Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("Namaste Telangana"))
		{
			URL = null; //2013/Oct/08  09102013
			DateFormat d1 = new SimpleDateFormat("yyyy/MMM/dd");
			DateFormat d2 = new SimpleDateFormat("ddMMyyyy");
			
			
			//cal.add(Calendar.DATE, 1);
			
			String ndate = d1.format(cal.getTime());
			String ndate2 = d2.format(cal.getTime());
			//System.out.println("N Telanaga Date"+ndate);
			
			Document doc = Jsoup.connect("http://namasthetelangaana.com/").timeout(10*1000).get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				if( s.contains(ndate)&& s.contains(ndate2)) 
		        {
				  URL = s;
				  String oldchar=ndate2+"s";
				  URL = URL.replace(oldchar, ndate2+"b");
		        //  System.out.println ("\nNamaste Telangana link :"+URL);	
		        }
				
			}
			
		} //Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("Andhra Bhoomi"))
		{	URL = null;
			Document doc = Jsoup.connect("http://www.andhrabhoomi.net/").get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				
				if( s.contains("funimages")) 
		        {
				  s = s.replace("styles/fungama/public/funimages", "funimages");
				  URL = s;
		         // System.out.println ("\nAndhraBhoomi link :"+URL);	
		        }
				
			}
			
		}  //Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("Andhra Jyothi"))
		{	URL = null;
			Document doc = Jsoup.connect("http://www.andhrabhoomi.net/").get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				
				if( s.contains("funimages")) 
		        {
				  s = s.replace("styles/fungama/public/funimages", "funimages");
				  URL = s;
		         // System.out.println ("\nAndhraBhoomi link :"+URL);	
		        }
				
			}
			
		}

		
		 //Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("dc"))
		{	URL = null;
			Document doc = Jsoup.connect("http://www.deccanchronicle.com/").timeout(10*1000).get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				
				if( (s.contains("cloudfront.net/sites/default/files/")&& s.contains("SUB")) || (s.contains("cloudfront.net/sites/default/files/")&& s.contains("counter"))) 
		        {
				  //s = s.replace("styles/fungama/public/funimages", "funimages");
				  URL = s;
		          //System.out.println ("\nDeccan Chronicle link :"+URL);	
		        }
				
			}
			
		}
		 //Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("Hindu"))
		{	URL = null;
			DateFormat dhindu = new SimpleDateFormat("dd");	
			String todaydate = dhindu.format(cal.getTime())+"TH";
			System.out.println("\n \t date ra"+todaydate);
			Document doc = Jsoup.connect("http://www.thehindu.com/opinion/cartoon/").timeout(10*1000).get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				
				if( s.contains("CARTOON") && s.contains(todaydate)) 
		        {
				  s = s.replace("d.", "f.");
				  URL = s;
		         // System.out.println ("\n HINDU Link :"+URL);	
		        }
				
			}
			
			
		}
		 //Sakshi Namaste Telangana  Vartha Surya Andhra Jyothi Andhra Bhoomi dc  Hindu iexpress
		else if(templink.contentEquals("iexpress"))
		{	URL = null;
			Document doc = Jsoup.connect("http://www.indianexpress.com/picture-gallery/cartoon-gallery-by-ep-unny/262-1.html").timeout(10*1000).get();
			
			Elements imagelinks = doc.getElementsByTag("img");
			
			for(Element ilink : imagelinks)
			{
				String s= ilink.attr("src");
				
				if( s.contains("Cartoon") ) 
		        {
				   URL = s;
		         // System.out.println ("\n IExpress Link :"+URL);	
		        }
				
			}
			
		}

		

		}
		catch(Exception e)
		{
			
			URL ="http://i1356.photobucket.com/albums/q721/lavankumar33/New-Logo.png";
			e.printStackTrace();
			return URL;
		}
	
	

		if(URL!=null && !URL.isEmpty())
		{	
			Log.d("TDC : ImageLinkGrabber :",URL);
			return URL;
		}
			else
			{
				URL ="http://i1356.photobucket.com/albums/q721/lavankumar33/New-Logo.png";
				return URL;
			}
	
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.telugu_daily_comics, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        
        case R.id.help:
        startActivity(new Intent(this, Help.class));
        return true;
        default:
        return super.onOptionsItemSelected(item);
    }
    }
    
}
