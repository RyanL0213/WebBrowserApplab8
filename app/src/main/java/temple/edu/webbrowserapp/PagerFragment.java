package temple.edu.webbrowserapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;


public class PagerFragment extends Fragment {

    public static ArrayList<PageViewerFragment> pages;
    public static ViewPager viewPager;
    private PagerInterface parentActivity;

    private static final String PAGES_KEY="pages";
    public static String currenttitle;

    public PagerFragment() {
        // Required empty public constructor
    }


    public static PagerFragment newInstance(ArrayList<PageViewerFragment> pages) {
        PagerFragment fragment=new PagerFragment();
        Bundle args=new Bundle();
        args.putSerializable(PAGES_KEY, pages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof PagerFragment.PagerInterface) {
            parentActivity= (PagerFragment.PagerInterface) context;
        } else {
            throw new RuntimeException("You must implement this fragment");
        }
    }
    public static void setcurrenttile(){
        currenttitle = pages.get(viewPager.getCurrentItem()).currentTitle;
        //webtitlestring.add(currenttitle);
        //Log.d("Current title is ",currenttitle);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            pages=(ArrayList) getArguments().getSerializable(PAGES_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_pager, container, false);
        viewPager = myView.findViewById(R.id.viewPager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return pages.get(position);
            }

            @Override
            public int getCount() {
                return pages.size();
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                if(pages.contains(object))
                    return pages.indexOf(object);
                else
                    return POSITION_NONE;
            }

        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                parentActivity.updateTitle(getCurrentTitle());
                parentActivity.updateUrl(getCurrentUrL());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return myView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void notifyWebsitesChanged() {
        viewPager.getAdapter().notifyDataSetChanged();
    }

    public void showTab(int index) {
        viewPager.setCurrentItem(index);
    }

    public void go(String url) {
        getPageViewer(viewPager.getCurrentItem()).setAvoidMultipleCall(true);
        getPageViewer(viewPager.getCurrentItem()).gettingUrl(url);
    }


    public void forward() {
        getPageViewer(viewPager.getCurrentItem()).setAvoidMultipleCall(true);
        getPageViewer(viewPager.getCurrentItem()).forward();
    }

    public void back() {
        getPageViewer(viewPager.getCurrentItem()).setAvoidMultipleCall(true);
        getPageViewer(viewPager.getCurrentItem()).back();;
    }

    public String getCurrentUrL() {
        return getPageViewer(viewPager.getCurrentItem()).getCurrentUrl();
    }

    public String getCurrentTitle() {
        return getPageViewer(viewPager.getCurrentItem()).getCurrentTitle();
    }

    public PageViewerFragment getPageViewer(int position) {
        return (PageViewerFragment) ((FragmentStatePagerAdapter) viewPager.getAdapter()).getItem(position);
    }


    interface PagerInterface {
        void updateUrl(String url);
        void updateTitle(String title);
    }
}