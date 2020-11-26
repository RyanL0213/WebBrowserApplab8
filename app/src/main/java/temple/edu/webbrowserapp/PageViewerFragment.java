package temple.edu.webbrowserapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PageViewerFragment extends Fragment implements Parcelable {
    WebView webView;
    private String currentUrl;
    String currentTitle;
    sentCurrentUrlInterface parentActivity;
    private boolean avoidMultipleCall; //a lock to ensure onPageStart and onPageFinished does not get call when user make swipe across the viewpager
    public PageViewerFragment() {
        // Required empty public constructor
    }

    protected PageViewerFragment(Parcel in) {
        currentUrl = in.readString();
        currentTitle = in.readString();
        avoidMultipleCall = in.readByte() != 0;
    }

    public static final Creator<PageViewerFragment> CREATOR = new Creator<PageViewerFragment>() {
        @Override
        public PageViewerFragment createFromParcel(Parcel in) {
            return new PageViewerFragment(in);
        }

        @Override
        public PageViewerFragment[] newArray(int size) {
            return new PageViewerFragment[size];
        }
    };

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof sentCurrentUrlInterface) {
            parentActivity=(sentCurrentUrlInterface) context;
        } else {
            throw new RuntimeException("You must implement this fragment");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_page_viewer, container, false);
        webView= v.findViewById(R.id.webView);
        webView.canGoBack();
        webView.canGoForward();
        avoidMultipleCall=false;
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(avoidMultipleCall!=false) {
                    parentActivity.sentlink(url);
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if(avoidMultipleCall!=false) {
                    currentTitle=view.getTitle();
                    getActivity().setTitle(view.getTitle());
                    avoidMultipleCall=false;
                }
            }
        });
//        currentTitle=webView.getTitle();
//        currentUrl=webView.getUrl();
//        parentActivity.sentTitle(currentTitle);
//        parentActivity.sentlink(currentUrl);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true); //enable javascript
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if(savedInstanceState!=null) {
            webView.restoreState(savedInstanceState);
            savedInstanceState.getString("url", currentUrl);
            savedInstanceState.getString("title", currentTitle);
        }
        return v;
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
        outState.putString("url", currentUrl);
        outState.putString("title", currentTitle);
    }


    public void gettingUrl(String message) {
        currentUrl=message;
        webView.loadUrl(currentUrl);
    }

    public void forward() {
        webView.goForward();
    }

    public void back() {
        webView.goBack();
    }



    public String getCurrentTitle() {
        return webView.getTitle();
    }

    public String getCurrentUrl() {
        return webView.getUrl();
    }

    public void setAvoidMultipleCall(boolean newLock) {
        this.avoidMultipleCall=newLock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(currentUrl);
        parcel.writeString(currentTitle);
        parcel.writeByte((byte) (avoidMultipleCall ? 1 : 0));
    }

    interface sentCurrentUrlInterface {
        void sentlink(String s);
//        void sentTitle(String s);
    }

}