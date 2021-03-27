package com.faridtahmazov.instaclonefirebase;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userEmailFB;
    ArrayList<String> userCommentFB;
    ArrayList<String> userImageFB;
    FeedrecycleAdapter feedrecycleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        userEmailFB = new ArrayList<>();
        userCommentFB = new ArrayList<>();
        userImageFB = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFireStore();

        RecyclerView recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedrecycleAdapter = new FeedrecycleAdapter(userEmailFB, userCommentFB, userImageFB);
        recyclerView.setAdapter(feedrecycleAdapter);



    }

    public void getDataFromFireStore(){
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(FeedActivity.this, error.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
                if (value != null){
                    for (DocumentSnapshot snapshot: value.getDocuments()) {
                        Map<String,Object> data = snapshot.getData();

                        String comment = (String) data.get("Comment");
                        String userEmail = (String) data.get("User Email");
                        String downloadUrl = (String) data.get("Download Url");

                        userEmailFB.add(userEmail);
                        userCommentFB.add(comment);
                        userImageFB.add(downloadUrl);

                    feedrecycleAdapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.insta_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.sign_out){  //Sign_out
            firebaseAuth.signOut();

            Intent intentSignOut = new Intent(FeedActivity.this, SignInActivity.class);
            startActivity(intentSignOut);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void uploadButton(View view){
        Intent intentToUpload = new Intent(FeedActivity.this, UploadActivity.class);
        startActivity(intentToUpload);

    }
}