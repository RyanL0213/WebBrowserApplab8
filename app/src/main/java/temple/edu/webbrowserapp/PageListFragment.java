package temple.edu.webbrowserapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PageListFragment extends Fragment {

    private ArrayList<PageViewerFragment> pages;
    private ListView listView;
    PageListInterface parentActivity;

    private static final String PAGES_KEY="pages";

    public PageListFragment() {
        // Required empty public constructor
    }

    public static PageListFragment newInstance(ArrayList<PageViewerFragment> webTitles) {
        PageListFragment fragment=new PageListFragment();
        Bundle args=new Bundle();
        args.putSerializable(PAGES_KEY, webTitles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
            pages=(ArrayList) getArguments().getSerializable(PAGES_KEY);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PageListInterface) {
            parentActivity = (PageListInterface) context;
        } else {
            throw new RuntimeException("You must implement ButtonClickInterface to attach this fragment");
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView=inflater.inflate(R.layout.fragment_page_list, container, false);
        listView= myView.findViewById(R.id.webTitlesList);
        ArrayList<String> allWebTitles = new ArrayList<String>();
        for(int i=0; i<pages.size(); i++) {
            allWebTitles.add(pages.get(i).getCurrentTitle());
        }
        listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_textview, allWebTitles));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parentActivity.titleClickAction(position);
            }
        });
        return myView;
    }

    public void notifyWebsitesChanged() {
        if(listView!=null)
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    interface PageListInterface {
        void titleClickAction(int position);
    }
}