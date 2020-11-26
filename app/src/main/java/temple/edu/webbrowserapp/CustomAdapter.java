package temple.edu.webbrowserapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    Context context;
    ArrayList<String> title;
    buttonclickinterface parentActivity;

    public CustomAdapter(Context context, ArrayList<String> title){
        super(context, 0, title);
        this.context = context;
        this.title = title;
    }
    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public String getItem(int i) {
        return title.get(i);
    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String item = getItem(i);

        parentActivity = (buttonclickinterface)context;

        if(view ==null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_button,viewGroup,false
            );
        }

         TextView list_Txt = (TextView)view.findViewById(R.id.bookmarktitle);
         ImageButton list_But = (ImageButton)view.findViewById(R.id.trash);

         list_Txt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Log.d("Click","The title being click is " + item);
                 parentActivity.textviewclick(i);
             }
         });
         list_But.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 parentActivity.deleteclick(i);
             }
         });

         list_Txt.setText(item);



        return view;
    }

    public interface buttonclickinterface{
        void textviewclick(int i);
        void deleteclick(int i);
    }



}