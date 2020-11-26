package temple.edu.webbrowserapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class BookMarkActivity extends AppCompatActivity implements CustomAdapter.buttonclickinterface {
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    CustomAdapter arrayAdapter;
    ListView bookmark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        Intent getintent = getIntent();

        title = getIntent().getStringArrayListExtra("bookmarktitle");
        address = getIntent().getStringArrayListExtra("bookmarkaddress");
        /*if(title==null){
            Log.d("null exception","it is null");
        }
        else{
            Log.d("is not null","not null");
        }

         */
        bookmark = findViewById(R.id.bookmarklistview);
        arrayAdapter=new CustomAdapter(this,title);
        bookmark.setAdapter(arrayAdapter);



    }
    private void showDialog(){

    }

    @Override
    public void textviewclick(int position) {


        String url = address.get(position);
        if(!url.contains("https")){
            url = title.get(position);
        }
        Log.d("Click address","After Click the url is " + url);

        Intent i = new Intent();
        i.putExtra("getbackaddress",url);
        setResult(RESULT_OK,i);
        finish();
        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void deleteclick(int position) {
        Dialog determinedeletion = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure that you want to delete this bookmark?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        title.remove(position);
                        address.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        SharedPreferences share = getSharedPreferences("SaveData", MODE_PRIVATE);
                        SharedPreferences.Editor editor = share.edit();
                        Gson gson = new Gson();
                        String address_json = gson.toJson(title);
                        String title_json = gson.toJson(address);
                        editor.putString("address_list", address_json);
                        editor.putString("title_list", title_json);
                        editor.apply();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();



    }
}