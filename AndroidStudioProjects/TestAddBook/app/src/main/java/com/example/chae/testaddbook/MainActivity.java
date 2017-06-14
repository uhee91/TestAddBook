package com.example.chae.testaddbook;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_COME_PERMISSION = 0xFFFF01;

    private ListView addrList;
    private Button initButton;
    private AddrAdpater addAdpter;
    private ArrayList<SetAddrData> asList;
    private ListView addinital;
    private int statas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);

        addrList    =    (ListView)findViewById(R.id.addrList);
        initButton  =    (Button)findViewById(R.id.initButton);
        asList = new ArrayList<SetAddrData>();

        addAdpter  = new AddrAdpater(this,R.layout.addrview,asList);

        addrList.setAdapter(addAdpter);

        //\addrList.setOnItemClickListener(itemClickListener);
        initButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),Perinit.class);
                startActivityForResult(intent,0);
            }

        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_COME_PERMISSION);
            } else {
                refreshData();
            }
        }else {
            refreshData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String [] requestPermission= new String[2];
        int [] requestPerMissionResult = new int [2];
        if(requestCode == REQUEST_COME_PERMISSION){
            for(int i = 0; i< permissions.length;i++) {
                requestPermission[i] = permissions[i];
            }
            for(int i = 0; i<requestPermission.length;i++){
                if(Manifest.permission.READ_CONTACTS.equalsIgnoreCase(requestPermission[i])){
                     requestPerMissionResult[i] = grantResults[i] ;
                }else if(Manifest.permission.WRITE_CONTACTS.equalsIgnoreCase(requestPermission[i])){
                    requestPerMissionResult[i] = grantResults[i] ;
                }
            }

            for (int i = 0; i<requestPerMissionResult.length;i++){
                if (requestPerMissionResult[i] == PackageManager.PERMISSION_GRANTED){
                    refreshData();
                }else{
                    AlertDialog.Builder alert_confitm = new AlertDialog.Builder(this);
                    alert_confitm.setMessage("사용 불가");
                    alert_confitm.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    });

                    AlertDialog alert = alert_confitm.create();
                    alert.setTitle(("Contacts"));
                    alert.show();

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                refreshData();
                addAdpter.notifyDataSetChanged();
                break;
        }
    }

    //    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            SetAddrData temp= asList.get(position);
//        }
//    };

//    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            SetAddrData data = asList.get(position);
//
//        }
//    };

    private void refreshData() {
        // 항상 초기화, notifyDataSetChanged() 에서 중복으로 값을 가져 올수 있기 때문에 asList를 초기화 시켜야한다.
        asList.clear();

        //  asList = new ArrayList<SetAddrData>();
        Cursor c = null;
        char choName = '\u0000';

        try {
            c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" ASC");// " DESC"

            if (c != null) {
                if (c.getCount() > 0 && c.moveToFirst()) {
                    //SetAddrData Item = new SetAddrData(); //여기서 객체를 만들면 한 주소에 같은 내용을 넣기 때문에 이전 데이터 삭제됨
                    InitialClass setInit = new InitialClass(); // 글자 쪼개기
                    do {
                        SetAddrData Item = new SetAddrData(); // 여기서 객체를 새로 만들어 서로 다른 주소로 데이터를 받으면 데이터 삭제 안됨
                        //연락처 id 값
                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));//DB부분따로 빼기
                        Log.d("", "id: " + id);
                        //연락처 대표 이름
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
                        setInit.stream(name);

                        //** 수정된 부분, Layout 리스트뷰 하나.
                        if(setInit.getChoName() != choName) {
                            statas = 0;
                            Item.setStatus(statas);
                            Item.setInital(setInit.getChoName());
                            choName = setInit.getChoName();
                        }else {
                            statas = 1;
                            Item.setStatus(statas) ;
                        }

                        Character cr= new Character(Item.getInital());
                        String str = cr.toString();
                        Log.i("",str);
                        Log.d("", "name: " + name);
                        Item.setName(name);

                        Cursor phoneCursor = null;
                        phoneCursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                                null, null
                        );


                        if (phoneCursor.moveToNext()) {
                            String number = phoneCursor.getString(phoneCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                            ));
                            Item.setpNumber(number);
                            phoneCursor.close();
                        }

                        if (asList != null && Item.getName() != null)
                            asList.add(Item);
                    } while (c.moveToNext());

                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if(c == null)
             c.close();

        addAdpter.notifyDataSetChanged();
    }

//        asList = new ArrayList<SetAddrData>();
//       Cursor c = null;
//        c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
//                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
//        while(c.moveToNext()){
//            SetAddrData Item = new SetAddrData();
//
//            //연락처 id 값
//            String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
//            Log.d("","id: "+id);
//            //연락처 대표 이름
//            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
//            Log.d("","name: "+name);
//            Item.setName(name);
//
//            //ID로 전화 정보 조회
//            Cursor phoneCursor = getContentResolver().query(
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
//                    null,null
//            );
//
//
//            if(phoneCursor.moveToNext()){
//                String number = phoneCursor.getString(phoneCursor.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Phone.NUMBER
//                ));
//                Item.setpNumber(number);
//            }
//            phoneCursor.close();
//            if(asList != null)
//                asList.add(Item);
//
//        }// end while
//
//        c.close();
//
//        addAdpter.notifyDataSetChanged();
//    }// end function

///// ** cursor 코드 구조 및 기본 코드 구조
//    private void test() {
//
//        Cursor cursor = null;
//        try {
//            cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
//
//            if(cursor != null) {
//                if(cursor.getCount() > 0 && cursor.moveToFirst()) {
//
//                    do {
//                        .....
//                    } while( cursor.moveToNext() );
//
//                }
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//
//        }
//
//        if(cursor != null) {
//            cursor.close();
//            cursor = null;
//        }
//
//    }

}


