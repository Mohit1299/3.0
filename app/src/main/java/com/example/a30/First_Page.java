package com.example.a30;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


public class First_Page extends AppCompatActivity {



    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageView;
    private Button mSubmit;
    private EditText mName;
    private EditText mPhone;
    private EditText mReportingPerson;

    private Uri mUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        mSubmit = findViewById(R.id.idsubmit);
        mName = findViewById(R.id.idname);
        mPhone = findViewById(R.id.idphone);
        mReportingPerson = findViewById(R.id.idrp);
        Button btnCamera = findViewById(R.id.btnCamera);
        imageView = findViewById(R.id.ivCamera);


        mStorageRef = FirebaseStorage.getInstance().getReference("Photos");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Photos");

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        mUri =data.getData();
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
    }

    private String getFileExtension(Uri mUri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(mUri));
    }

    public void uploadFile()
    {
        StorageReference fileRefernce = mStorageRef.child(System.currentTimeMillis()
                + "." +getFileExtension(mUri));

        mUploadTask = fileRefernce.putFile(mUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                
                            }
                        },2000);

                        Toast.makeText(First_Page.this,"Uploaded Successfully",Toast.LENGTH_LONG).show();
                        Upload upload;
                        upload = new Upload(mName.getText().toString().trim(),
                                mPhone.getText().toString().trim(),
                                mReportingPerson.getText().toString().trim());
                        String uploadId = mDatabaseRef.push().getKey();
                        mDatabaseRef.child(uploadId).setValue(upload);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(First_Page.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100.0  * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        

                    }
                });

    }
    }



