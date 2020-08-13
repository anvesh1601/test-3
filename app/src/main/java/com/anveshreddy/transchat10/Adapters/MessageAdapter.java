package com.anveshreddy.transchat10.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anveshreddy.transchat10.Model.Chat;
import com.anveshreddy.transchat10.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String ImageURL;
    private int i;
    int n;
    String s3,s1;
    int languagecode,Translanguagecode;

    FirebaseUser fuser;
    public MessageAdapter(Context mContext, List<Chat> mChat, String ImageURL,int i){
        this.mChat = mChat;
        this.mContext = mContext;
        this.ImageURL = ImageURL;
        this.i=i;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        n=viewType;
        if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        s1=chat.getSmessage();

        if(n==MSG_TYPE_LEFT){
            holder.identifylanguage(chat.getSmessage(),i);
        }else{
            holder.show_message.setText(chat.getSmessage());
        }




        if (ImageURL.equals("Default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(ImageURL).into(holder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message,show_message_left;
        public ImageView profile_image;


        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
show_message_left=itemView.findViewById(R.id.show_message_left);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
        private void identifylanguage(final String ss, final int i) {

            FirebaseLanguageIdentification a= FirebaseNaturalLanguage.getInstance().getLanguageIdentification();


            a.identifyLanguage(ss).addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    if(s.equals("und")){


                    }else{
                        getLanguageCode(s,i,ss);

                    }

                }
            });
        }
        private void getLanguageCode(String s,int i,String ss) {
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

            translatecode(ss,languagecode,i);
        }
        private void translatecode(final String sss, int languagecode, int tralngcode) {

            FirebaseTranslatorOptions op =new FirebaseTranslatorOptions.Builder().setSourceLanguage(languagecode)
                    .setTargetLanguage(tralngcode).build();
            final FirebaseTranslator translator=FirebaseNaturalLanguage.getInstance().getTranslator(op);
            FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder().requireWifi().build();
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    translator.translate(sss).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
show_message_left.setText(s);
                        }
                    });
                }
            });
        }

    }


    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }


}
