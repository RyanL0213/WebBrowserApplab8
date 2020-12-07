package temple.edu.webbrowserapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BrowserActivity extends AppCompatActivity implements PageControlFragment.buttonClickInterface, PageViewerFragment.sentCurrentUrlInterface, BrowserControlFragment.imageButtonClickInterface,
PagerFragment.PagerInterface, PageListFragment.PageListInterface {


    PageControlFragment controlFragment;
    PagerFragment pagerFragment;
    BrowserControlFragment browserControlFragment;
    PageListFragment pageListFragment;
    ArrayList<PageViewerFragment> pages;
    ArrayList<String> bookmarktitle = new ArrayList<String>();
    ArrayList<String> bookmarkaddress = new ArrayList<String>();


    FragmentManager manager;
    private final String PAGE_KEY= "pages";
    int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager=getSupportFragmentManager();

        Intent getintent;




        Fragment temporary;

        if(savedInstanceState!=null)
            pages=(ArrayList) savedInstanceState.getSerializable(PAGE_KEY);
        else {
            pages = new ArrayList<>();
           // pages.add(new PageViewerFragment());
          //  pages.get(0).webView.loadUrl("temple.edu");
        }

        orientation=getResources().getConfiguration().orientation;
        //buttonClick("temple.edu");



        if((temporary = manager.findFragmentById(R.id.page_control_container)) instanceof PageControlFragment) {
            controlFragment=(PageControlFragment) temporary;
        } else {
            controlFragment=new PageControlFragment();
            manager.beginTransaction().add(R.id.page_control_container, controlFragment).commit();
        }


        if((temporary=manager.findFragmentById(R.id.browser_control_container)) instanceof BrowserControlFragment) {
            browserControlFragment=(BrowserControlFragment) temporary;
        } else {
            browserControlFragment=new BrowserControlFragment();
            manager.beginTransaction().add(R.id.browser_control_container, browserControlFragment).commit();
        }


        if((temporary=manager.findFragmentById(R.id.page_viewer_container)) instanceof PagerFragment) {
            pagerFragment=(PagerFragment) temporary;
        } else {
            pagerFragment=pagerFragment.newInstance(pages);
            manager.beginTransaction().add(R.id.page_viewer_container, pagerFragment).commit();
        }


        if(orientation== Configuration.ORIENTATION_LANDSCAPE) {
            if((temporary=manager.findFragmentById(R.id.page_list_container)) instanceof PageListFragment) {
                pageListFragment=(PageListFragment) temporary;
            } else {
                pageListFragment=PageListFragment.newInstance(pages);
                manager.beginTransaction().add(R.id.page_list_container, pageListFragment).commit();
            }
        }



       /* if(pages.size()>0){
            Log.d("null","page is null");
            pages.add(new PageViewerFragment());
           // notifyWebsitesChanged();
            pagerFragment.showTab(pages.size()-1);
            pagerFragment.go("temple.edu");
        }


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttonClick("temple.edu");
                }
            },1000);

        */




    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PAGE_KEY, pages);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent receiveintent = getIntent();
        String receivedAction = receiveintent.getAction();
        if(Intent.ACTION_VIEW.equals(receivedAction)){
            String templeedu = receiveintent.getStringExtra("templeedu");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttonClick(templeedu);
                }
            },1000);

        }

    }

    @Override
    public void buttonClick(String Url) {
        /*if(pages==null){
            pages.add(new PageViewerFragment());
            notifyWebsitesChanged();
            pagerFragment.showTab(pages.size()-1);
            pagerFragment.go(Url);
        }

         */
        if(pages.size()>0)
            pagerFragment.go(Url);
        else {
            pages.add(new PageViewerFragment());
            notifyWebsitesChanged();
            pagerFragment.showTab(pages.size()-1);
            pagerFragment.go(Url);
        }
    }



    @Override
    public void saveData() {
        //String address = PageControlFragment.url_text.getEditableText().toString();
        //String address = PageViewerFragment.currentTitle;
        // Log.d("bookmark",address);
        //PageControlFragment.setaddresslist();
        PagerFragment.setcurrenttile();
        String btitle = PagerFragment.currenttitle;
        String baddress = PageControlFragment.url_text.getEditableText().toString();
        Toast.makeText(this,"The book mark of " + btitle +" has been added",Toast.LENGTH_SHORT).show();
        bookmarktitle.add(btitle);
        bookmarkaddress.add(baddress);
        SharedPreferences share = getSharedPreferences("SaveData",MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        Gson gson = new Gson();
        String address_json = gson.toJson(bookmarkaddress);
        String title_json = gson.toJson(bookmarktitle);
        editor.putString("address_list",address_json);
        editor.putString("title_list",title_json);
        editor.apply();

    }

    @Override
    public void intentbookmark() {
        SharedPreferences share = getSharedPreferences("SaveData",MODE_PRIVATE);
        Gson gson = new Gson();
        String address_json = share.getString("address_list",null);
        String title_json = share.getString("title_list",null);
        Type title_type = new TypeToken<ArrayList<String>>(){}.getType();
        Type address_type = new TypeToken<ArrayList<String>>(){}.getType();
        bookmarktitle = gson.fromJson(title_json,title_type);
        bookmarkaddress = gson.fromJson(address_json,address_type);

        Intent bookmarkintent = new Intent(BrowserActivity.this,BookMarkActivity.class);
        bookmarkintent.putExtra("bookmarktitle",bookmarktitle);
        bookmarkintent.putExtra("bookmarkaddress",bookmarkaddress);
        //Log.d("At this point","Url is " + bookmarktitle.get(0));
        startActivityForResult(bookmarkintent,1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //buttonClick("temple.edu");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String btitle = pages.get(PagerFragment.viewPager.getCurrentItem()).currentTitle;
        String baddress = PageControlFragment.url_text.getEditableText().toString();
        int id = item.getItemId();
        if(id == R.id.sharebutton){
            Intent newintent = new Intent(Intent.ACTION_SEND);
            newintent.setType("text/plain");
            newintent.putExtra(Intent.EXTRA_TEXT,"Title: " + btitle + "\naddress is : " + baddress);
            startActivity(newintent);
        }
        else
            return false;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode==RESULT_OK){
                    String url=data.getExtras().getString("getbackaddress");
                    Log.d("url is","After intent the url is " + url);
                    buttonClick(url);
                }
        }
    }

    @Override
    public void buttonClickforward() {
        pagerFragment.forward();
    }

    @Override
    public void buttonClickback() {
        pagerFragment.back();
    }

    @Override
    public void sentlink(String s) {
        controlFragment.displayCurrentUrl(s);
    }

//    @Override
//    public void sentTitle(String s) {
//        this.setTitle(s);
//    }

    private void notifyWebsitesChanged() {
        pagerFragment.notifyWebsitesChanged();
        if(orientation==Configuration.ORIENTATION_LANDSCAPE)
            pageListFragment.notifyWebsitesChanged();
    }


    @Override
    public void imageButtonClick() {
        pages.add(new PageViewerFragment());
        notifyWebsitesChanged();
        pagerFragment.showTab(pages.size()-1);
    }

    @Override
    public void titleClickAction(int position) {
        pagerFragment.showTab(position);
    }


    @Override
    public void updateUrl(String url) {
        controlFragment.displayCurrentUrl(url);
    }

    @Override
    public void updateTitle(String title) {
        this.setTitle(title);
    }

    @Override
    public void setupviewpager() {
        PagerFragment.setup();
    }
}