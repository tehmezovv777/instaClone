package com.faridtahmazov.instaclonefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    TextView sharePostText;
    EditText commentText;
    Bitmap selectedImage;
    ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    Uri imageData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        sharePostText = findViewById(R.id.sharePostsText);
        commentText = findViewById(R.id.CommentText);
        imageView = findViewById(R.id.imageView);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void sharePost(View view){


        if (imageData != null){
            UUID uuid = UUID.randomUUID();  //Random id
            final String way = "images/" + uuid +".jpg";

            storageReference.child(way).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Download URl

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(way);

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {



                            String downloadUrl = uri.toString();

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userEmail = firebaseUser.getEmail();
                            String comment = commentText.getText().toString();

                            HashMap<String, Object> postData = new HashMap<>();
                            postData.put("User Email", userEmail);
                            postData.put("Download Url", downloadUrl);
                            postData.put("Comment", comment);
                            postData.put("Data", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Intent intentToFeed = new Intent(UploadActivity.this, FeedActivity.class);
                                    intentToFeed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intentToFeed);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();

                }
            });
        }

    }

    public void selectPicture(View view){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){ //Şəkil üçün icazə istəyirik
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else{
            Intent intentToGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGalery, 2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //Icazənin nəticələri
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGalery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGalery, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==2 && resultCode == RESULT_OK){
            imageData = data.getData();

            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                        selectedImage = ImageDecoder.decodeBitmap(source);
                        imageView.setImageBitmap(selectedImage);

                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    imageView.setImageBitmap(selectedImage);
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}