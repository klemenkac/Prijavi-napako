package com.example.kac.prijavinapako;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

/**
 * Created by Klemen on 18. 12. 2017.
 */

public class ActivityNFCzapis extends AppCompatActivity {
    NfcAdapter nfcAdapter;

    Integer pisi=1;
    Integer beri=0;
    ToggleButton tglReadWrite;
    TextView txtTagContent;
    RadioButton rbPisanje;
    RadioButton rbBranje;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfczapis);

        rbPisanje = (RadioButton) findViewById(R.id.rbPisanje);
        rbBranje = (RadioButton) findViewById(R.id.rbBranje);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        txtTagContent = (EditText)findViewById(R.id.txtTagContent);

    }


    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundFispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbPisanje:
                if (checked)
                    pisi=1;
                beri=0;
                    break;
            case R.id.rbBranje:
                if (checked)
                    beri=1;
                pisi=0;
                    break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){

            if(beri==1){
                //Beri
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if(parcelables != null && parcelables.length > 0){
                    readTextFromMessage((NdefMessage)parcelables[0]);
                }else{
                    Toast.makeText(this,"No NDEF messages found!",Toast.LENGTH_LONG).show();

                }
            }else if(pisi==1){
                //Piši
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage(txtTagContent.getText()+"");

                writeNdefMessage(tag,ndefMessage);
            }


        }
    }

    public void pisanjeOnClick(View view){
        pisi=1;
        beri=0;
        Toast.makeText(this,"Status: pisanje",Toast.LENGTH_LONG).show();

    }

    public void branjeOnClick(View view){
        pisi=0;
        beri=1;
        txtTagContent.setText("");
        Toast.makeText(this,"Status: branje",Toast.LENGTH_LONG).show();
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){
            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            txtTagContent.setText(tagContent);
        }else{
            Toast.makeText(this,"No NDEF records found!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void enableForegroundFispatchSystem(){
        Intent intent = new Intent(this, ActivityNFCzapis.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this,pendingIntent, intentFilters,null);

    }

    private void disableForegroundDispatchSystem(){

        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage){
        try{
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if(ndefFormatable == null){
                Toast.makeText(this,"Tag is not ndef formatable!",Toast.LENGTH_LONG).show();
                return;
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this,"Tag writen!",Toast.LENGTH_LONG).show();
            txtTagContent.setText("");


        }catch (Exception e){
            Toast.makeText(this,"NAPAKA1",Toast.LENGTH_LONG).show();

        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){
        try{
            if(tag == null){
                Toast.makeText(this,"Tag je prazen!",Toast.LENGTH_LONG).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if(ndef==null){
                //format tag with ndef format and writes the message
                formatTag(tag,ndefMessage);
            }
            else{
                ndef.connect();

                if(!ndef.isWritable()){
                    Toast.makeText(this,"Tag is not writable!",Toast.LENGTH_LONG).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this,"Tag writen!",Toast.LENGTH_LONG).show();
                txtTagContent.setText("");
            }

        }catch (Exception e){
            Toast.makeText(this,"NAPAKA2",Toast.LENGTH_LONG).show();

        }
    }

    private NdefRecord createTextRecord(String content){
        try{
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes();

            final byte[] text = content.getBytes();
            final int languageSize=language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1+languageSize+textLength);

            payload.write((byte) (languageSize & 0x1F));
            payload.write(language,0,languageSize);
            payload.write(text,0,textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0],payload.toByteArray());

        }
        catch (Exception e){
            Toast.makeText(this,"NAPAKA3",Toast.LENGTH_LONG).show();

        }
        return null;
    }

    private NdefMessage createNdefMessage(String content){
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }

    public void tglReadWriteOnClick(View view){
        txtTagContent.setText("");
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord){
        String tagContent=null;
        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize +1, payload.length-languageSize-1, textEncoding);
        }catch (Exception e){

        }
        return tagContent;
    }
}
