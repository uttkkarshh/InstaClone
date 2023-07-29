package com.example.instaclone;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    ImageView close,add;
    TextView post;
    EditText des;
    ActivityResultLauncher<Intent> photo;
    Uri uri;
    String downloadUrl;
    AlertDialog s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
         close=findViewById(R.id.close);
         add=findViewById(R.id.image_added);
         des=findViewById(R.id.description);
         post=findViewById(R.id.post);
         photo=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
             @Override
             public void onActivityResult(ActivityResult result) {
                 if(result.getResultCode()==RESULT_OK){
                      Intent u=result.getData();
                      uri=u.getData();
                      add.setImageURI(uri);
                 }
             }
         });

         add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i=new Intent(Intent.ACTION_PICK);
                 i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 photo.launch(i);
             }
         });
         AlertDialog.Builder b=new AlertDialog.Builder(this);
         b.setCancelable(false);
         b.setView(R.layout.layout_loading_dialog);
         post.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 s=b.create();
                 s.show();
                 if (uri != null){
                     final StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(uri));
                   StorageTask store=filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                   filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           downloadUrl=uri.toString();

                                           DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
                                           String postId = ref.push().getKey();
                                           Log.i("upload123",""+downloadUrl);
                                           HashMap<String , Object> map = new HashMap<>();
                                           map.put("postid" , postId);
                                           map.put("imageurl" , downloadUrl);
                                           map.put("description" , des.getText().toString());
                                           map.put("publisher" , FirebaseAuth.getInstance().getCurrentUser().getUid());

                                           ref.child(postId).setValue(map);
                                           DatabaseReference mHashTagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                                           String tag =des.getText().toString();
                                           map.clear();

                                           map.put("tag" , tag.toLowerCase());
                                           map.put("postid" , postId);

                                           mHashTagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                                           s.dismiss();
                                           startActivity(new Intent(PostActivity.this , MainActivity.class));
                                           finish();
                                       }
                                   });
                               }
                           })
                          .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
         }
             else{
                     Toast.makeText(PostActivity.this, "No image was selected!", Toast.LENGTH_SHORT).show();
             }
             }});
    }
    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

}