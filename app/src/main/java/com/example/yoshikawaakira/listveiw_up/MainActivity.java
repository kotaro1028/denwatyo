package com.example.yoshikawaakira.listveiw_up;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    private List<ContactDetail> list = new ArrayList<>();//<ContactDetail>を書いている理由はContactDetailのデータしかListに使用できなようにするため。
    //private List list  = new ArrayList();   //これは間違え
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        showContacts();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void showContacts() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            //携帯の中の電話帳にアクセスしていいかをきく
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            // スマホから取ってきたデータをcursorに入れる！
            while (phone.moveToNext()){
                String name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //持ってきたcursorから名前を取り出す。
                String phoneno = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //持ってきたcursorから電話を取り出す。

                ContactDetail detail = new ContactDetail();
                detail.setName(name);
                detail.setPhone_number(phoneno);
                list.add(detail);
                //持ってきたデータをlistに入れる

            }
            phone.close();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Contact detail");
            builder.setMessage(list.size()+" contact found");
            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            //何人電話帳に保存されているかを表示する。

            ContactAdapter adapter = new ContactAdapter(MainActivity.this,R.layout.activity_singleuser,list);
            listView.setAdapter(adapter);

            if (list.size() >0 && list != null){
                Collections.sort(list, new Comparator<ContactDetail>() {
                    @Override
                    public int compare(ContactDetail lhs, ContactDetail rhs) {

                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ContactDetail contactDetail = (ContactDetail) listView.getItemAtPosition(i);
        String name  = contactDetail.getName();
        final String Phone = contactDetail.getPhone_number();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Call ?");
        builder.setMessage("Do you want call " + name);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String p = "tel:"+Phone; // tel:でphoneの中に入ってる数字を電話番号として認識させる
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(p)); //電話をかけるアプリに飛ぶ
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
                }
                startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
