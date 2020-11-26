package temple.edu.webbrowserapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class BrowserControlFragment extends Fragment {

    imageButtonClickInterface parentActivity;
    ImageButton imageButton;
    ImageButton savebutton;
    ImageButton bookmarkintent;
    public BrowserControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof imageButtonClickInterface) {
            parentActivity=(imageButtonClickInterface) context;
        } else {
            throw new RuntimeException("You must implement this fragment");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView=inflater.inflate(R.layout.fragment_browser_control, container, false);
        imageButton= myView.findViewById(R.id.addTapButton);
        savebutton = myView.findViewById(R.id.savebutton);
        bookmarkintent = myView.findViewById(R.id.bookmark);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.imageButtonClick();
            }
        });
        bookmarkintent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent implicitIntent = new Intent(getActivity(),BookMarkActivity.class);
                startActivity(implicitIntent);

                 */
                parentActivity.intentbookmark();
            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.saveData();
            }
        });
        return myView;
    }

    interface imageButtonClickInterface {
        void imageButtonClick();
        void saveData();
        void intentbookmark();
    }


}