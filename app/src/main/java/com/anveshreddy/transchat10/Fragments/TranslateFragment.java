package com.anveshreddy.transchat10.Fragments;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anveshreddy.transchat10.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;


public class TranslateFragment extends Fragment {
    TextView tv1,tv2;
    EditText ed1;
    ImageButton b1;
    String s1,s2;
    Spinner sp1;
    String s4;
    int Translanguagecode,languagecode;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_translate, container, false);
        ed1=view.findViewById(R.id.editText);
        tv1=view.findViewById(R.id.textView6);
        b1=view.findViewById(R.id.btn_send);
        sp1=view.findViewById(R.id.spinner2);


        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getContext(),R.array.languages,android.R.layout.simple_spinner_item);


        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp1.setAdapter(arrayAdapter);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s4 = adapterView.getItemAtPosition(i).toString();

                switch (s4) {
                    case "English":
                        Translanguagecode = FirebaseTranslateLanguage.EN;

                        break;
                    case "Arabic":
                        Translanguagecode = FirebaseTranslateLanguage.AR;

                        break;
                    case "Urdu":
                        Translanguagecode = FirebaseTranslateLanguage.UR;

                        break;
                    case "Hindi":
                        Translanguagecode = FirebaseTranslateLanguage.HI;

                        break;
                    case "Telugu":
                        Translanguagecode = FirebaseTranslateLanguage.TE;

                        break;
                    case "Tamil":
                        Translanguagecode = FirebaseTranslateLanguage.TA;

                        break;
                    case "Bengali":
                        Translanguagecode = FirebaseTranslateLanguage.BN;

                        break;
                    case "Kannada":
                        Translanguagecode = FirebaseTranslateLanguage.KN;

                        break;
                    case "Marathi":
                        Translanguagecode = FirebaseTranslateLanguage.MR;

                        break;

                    default:
                        Translanguagecode = FirebaseTranslateLanguage.HI;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Translanguagecode = 0;
                Toast.makeText(getContext(), "Please Select a language", Toast.LENGTH_LONG).show();

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identifylanguage();
            }
        });

return view;
    }
    private void identifylanguage() {
        s1=ed1.getText().toString();
        FirebaseLanguageIdentification a= FirebaseNaturalLanguage.getInstance().getLanguageIdentification();


        a.identifyLanguage(s1).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(s.equals("und")){

                }else{
                    getLanguageCode(s);

                }

            }
        });
    }



    private void getLanguageCode(String s) {
        switch(s){
            case "en":
                languagecode= FirebaseTranslateLanguage.EN;

                break;
            case "ar":
                languagecode= FirebaseTranslateLanguage.AR;

                break;
            case "ur":
                languagecode=FirebaseTranslateLanguage.UR;

                break;
            case "hi":
                languagecode= FirebaseTranslateLanguage.HI;

                break;
            case "te":
                languagecode= FirebaseTranslateLanguage.TE;

                break;
            case "ta":
                languagecode= FirebaseTranslateLanguage.TA;

                break;
            case "bn":
                languagecode= FirebaseTranslateLanguage.BN;

                break;
            case "kn":
                languagecode= FirebaseTranslateLanguage.KN;

                break;
            case "mr":
                languagecode= FirebaseTranslateLanguage.MR;

                break;

            default:
                languagecode=0;
        }

        translatecode(languagecode,Translanguagecode);
    }


    private void translatecode(int languagecode,int tralngcode) {
        tv1.setText("translating...");
        FirebaseTranslatorOptions op =new FirebaseTranslatorOptions.Builder().setSourceLanguage(languagecode)
                .setTargetLanguage(tralngcode).build();
        final FirebaseTranslator translator=FirebaseNaturalLanguage.getInstance().getTranslator(op);
        FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder().requireWifi().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(s1).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        tv1.setText(s);
                    }
                });
            }
        });
    }
}