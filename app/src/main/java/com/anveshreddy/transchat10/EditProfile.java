package com.anveshreddy.transchat10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.anveshreddy.transchat10.Model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;



public class EditProfile extends AppCompatActivity {
    ImageView close, image_profile;
    TextView save, Prefered_Lang,lang;
    MaterialEditText Email, Username;
    Spinner sp1;
    Button b1;
    private Boolean spinnerTouched = false;
    int Translanguagecode;
    FirebaseUser firebaseUser;
    String s3,ss3,l,Langcode;
    private Uri mImageUri;
    private StorageTask uploadTask;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
image_profile=findViewById(R.id.image_profile);
             Email=findViewById(R.id.mail);
             Username=findViewById(R.id.username);
             Prefered_Lang=findViewById(R.id.Language_Preferred);
             sp1=findViewById(R.id.langSpinner);
             b1=findViewById(R.id.changePassword);

lang=findViewById(R.id.LanguagePreferred);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

storageRef= FirebaseStorage.getInstance().getReference("uploads");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Username.setText(user.getUsername());
                Email.setText(user.getEmail());
                s3=user.getPreferedlanguage();
                int i=Integer.parseInt(s3);
                Translanguagecode=i;
                l= findLanguage(i);
               lang.setText(l);
                if (user.getImageURL().equals("Default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(image_profile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(EditProfile.this,R.array.languages,android.R.layout.simple_spinner_item);


        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        int selectionPosition= arrayAdapter.getPosition(l);
        sp1.setSelection(selectionPosition);
        sp1.setAdapter(arrayAdapter);
        sp1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               Prefered_Lang.setText("");
                spinnerTouched = true;
                return false;
            }
        });
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s4 = adapterView.getItemAtPosition(i).toString();

                if (spinnerTouched){
                    switch (s4) {
                        case "English":
                            Translanguagecode = FirebaseTranslateLanguage.EN;
                            Prefered_Lang.setText("English");

                            break;
                        case "Arabic":
                            Translanguagecode = FirebaseTranslateLanguage.AR;
                            Prefered_Lang.setText("Arabic");
                            break;
                        case "Urdu":
                            Translanguagecode = FirebaseTranslateLanguage.UR;
                            Prefered_Lang.setText("Urdu");
                            break;
                        case "Hindi":
                            Translanguagecode = FirebaseTranslateLanguage.HI;
                            Prefered_Lang.setText("Hindi");
                            break;
                        case "Telugu":
                            Translanguagecode = FirebaseTranslateLanguage.TE;
                            Prefered_Lang.setText("Telugu");
                            break;
                        case "Tamil":
                            Translanguagecode = FirebaseTranslateLanguage.TA;
                            Prefered_Lang.setText("Tamil");
                            break;
                        case "Bengali":
                            Translanguagecode = FirebaseTranslateLanguage.BN;
                            Prefered_Lang.setText("Bengali");
                            break;
                        case "Kannada":
                            Translanguagecode = FirebaseTranslateLanguage.KN;
                            Prefered_Lang.setText("Kannada");
                            break;
                        case "Marathi":
                            Translanguagecode = FirebaseTranslateLanguage.MR;
                            Prefered_Lang.setText("Marathi");
                            break;

                        default:
                            Prefered_Lang.setText("hii");
                    }
                }
                Langcode=String.valueOf(Translanguagecode);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Prefered_Lang.setText(Translanguagecode+"");
                Toast.makeText(EditProfile.this, "Please Select a language", Toast.LENGTH_LONG).show();

            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(EditProfile.this);
            }
        });
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(Username.getText().toString(),
                        Email.getText().toString(),
                        Langcode);
            }
        });

        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void updateProfile(String username, String mail, String PreferedLanguage){
if(PreferedLanguage.equals("")){
    PreferedLanguage=lang.getText().toString();
}

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        HashMap<String, Object> map = new HashMap<>();

        map.put("Username", username);
        map.put("preferedlanguage", PreferedLanguage);
        map.put("Email",mail);

        reference.updateChildren(map);

        Toast.makeText(EditProfile.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (mImageUri != null){
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String miUrlOk = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("ImageURL", ""+miUrlOk);
                        reference.updateChildren(map1);

                        pd.dismiss();

                    } else {
                        Toast.makeText(EditProfile.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(EditProfile.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            uploadImage();

        } else {
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private  String findLanguage(int i){

        switch (i){
            case FirebaseTranslateLanguage.EN :
               ss3= "English";
                break;

            case FirebaseTranslateLanguage.HI :
                ss3="Hindi";
                break;
            case FirebaseTranslateLanguage.AR :
                ss3="Arabic";
                break;
            case FirebaseTranslateLanguage.UR :
                ss3="Urdu";
                break;
            case FirebaseTranslateLanguage.TE :
                ss3="Telugu";
                break;
            case FirebaseTranslateLanguage.TA :
                ss3="Tamil";
                break;
            case FirebaseTranslateLanguage.BN :
                ss3="Bengali";
                break;
            case FirebaseTranslateLanguage.KN :
                ss3="Kannada";
                break;
            case FirebaseTranslateLanguage.MR :
                ss3="Marathi";
                break;
            default:
                ss3="English";
        }
        return ss3;
    }

}