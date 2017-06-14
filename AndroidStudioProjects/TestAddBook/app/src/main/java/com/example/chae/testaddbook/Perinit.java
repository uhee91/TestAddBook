package com.example.chae.testaddbook;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Text;

/**
 * Created by muhan_chae on 2017-06-08.
 */

public class Perinit extends Activity implements View.OnClickListener {
    private EditText edinput;
    private EditText edinput1;
    private Button initBtn;
    private Button canBtn;
    private String name;
    private String pNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrinfo1);
        edinput = (EditText) findViewById(R.id.edInput);
        edinput1 = (EditText) findViewById(R.id.edInput1);
        initBtn = (Button) findViewById(R.id.initBtn);
        canBtn = (Button) findViewById(R.id.canBtn);


        initBtn.setOnClickListener(this);
        canBtn.setOnClickListener(this);
        edinput1.addTextChangedListener(watcher);
        edinput.addTextChangedListener(watcher);

        initBtn.setEnabled(false);
        initBtn.setClickable(false);
    }

    TextWatcher watcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if(s.equals(null)) {
                Log.i("","1 "+s.toString());
                initBtn.setEnabled(false);
                initBtn.setClickable(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(count <=0 ) {
                Log.i("","2 "+s.toString());
                initBtn.setEnabled(false);
                initBtn.setClickable(false);
            }else{
                Log.i("","3 "+s.toString());
                initBtn.setEnabled(true);
                initBtn.setClickable(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };
    public void onClick(View v) {
        name = edinput.getText().toString();
        pNumber = edinput1.getText().toString();
        Log.i("",name);
        Log.i("",pNumber);
        if(v.getId() == R.id.initBtn) {
            ContentValues values = new ContentValues();
            //rawContact를 삽입한다.
            // ID 할당
            values.put(ContactsContract.RawContacts.CONTACT_ID,0);
            values.put(ContactsContract.RawContacts.AGGREGATION_MODE,ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED);
            Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
            long rawContactid = ContentUris.parseId(rawContactUri);

            //Data를 삽입한다.
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactid);
            values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,pNumber);
            Uri dataUri = this.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);
            Log.e("Data.phone put", dataUri.toString());

            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactid);
            values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
//            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,"광은");
//            values.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,"채");
            values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name);
            dataUri = this.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);

//            values.clear();
//            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
//            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
//            values.put(ContactsContract.CommonDataKinds.Email.TYPE,ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
//            values.put(ContactsContract.CommonDataKinds.Email.DATA1, "surprisen85@naver.com");
//            dataUri = this.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
//            Log.e("Data.StructuredName put", dataUri.toString()
            setResult(0);
            finish();
        }else if(v.getId() == R.id.canBtn){
            finish();
        }
    }

//    private View.OnClickListener insert_DB = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//        }
//    };

}

