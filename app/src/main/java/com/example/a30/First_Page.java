package com.example.a30;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;


public class First_Page extends AppCompatActivity {

    String Storage_Path = "Images/";
    // Root Database Name for Firebase Database.
    String Database_Path = "Info/";

    // Creating button.
    Button ChooseButton, UploadButton;

    // Creating EditText.
    EditText Name ;
    EditText Phone ;
    EditText ReportingPerson;
    EditText Purpose;
    EditText Electronics;
    RadioGroup radioGroup;
    RadioButton yes;
    RadioButton no;



    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;
    File file;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 1;

    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference().child("/Info");

        //Assign ID'S to button.
        ChooseButton = (Button)findViewById(R.id.btnCamera);
        UploadButton = (Button)findViewById(R.id.idsubmit);

        // Assign ID's to EditText.
        Name = (EditText)findViewById(R.id.idname);
        Phone = (EditText)findViewById(R.id.idphone);
        ReportingPerson= (EditText)findViewById(R.id.idrp);
        Purpose = (EditText)findViewById(R.id.idpurpose);
        Electronics = (EditText)findViewById(R.id.idelectronic);
        Electronics.setVisibility(View.INVISIBLE);

        // Assign ID'S to image view.
        SelectImage = (ImageView)findViewById(R.id.ivCamera);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(First_Page.this);

        //Assigning ID's to RadioGroup and button
        radioGroup = (RadioGroup)findViewById(R.id.rg);
        yes = (RadioButton)findViewById(R.id.rbyes);
        no = (RadioButton)findViewById(R.id.rbno);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(yes.isChecked()== true)
                {
                    Electronics.setVisibility(View.VISIBLE);
                }
                if(no.isChecked()== true)
                {
                    Electronics.setVisibility(View.INVISIBLE);
                }

            }
        });




        // Adding click listener to Choose image button.
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating intent.
                //Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                  startActivityForResult(intent, Image_Request_Code);
                //}

                //Intent intent = new Intent();
                //intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent,Image_Request_Code);


            }
        });


        ActivityCompat.requestPermissions(First_Page.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();
                notificationCall();


            }
        });
    }

    public void myMethod()
    {
        Toast.makeText(First_Page.this,"Permission Granted",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK ) {


            Bitmap photo = (Bitmap) data.getExtras().get("data");
            SelectImage.setImageBitmap(photo);

            FilePathUri = getImageUri(getApplicationContext(),photo);

            file = new File(getRealPathFromUri(FilePathUri));

            //&& data != null && data.getData() != null

           /* FilePathUri = data.getData();



            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");


                    //Bundle extras = data.getExtras();
                    //Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //SelectImage.setImageBitmap(imageBitmap);


            }
            catch (IOException e) {

                e.printStackTrace();
            }*/

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted and now can proceed
                     myMethod();//a sample method called

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(First_Page.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // add other cases for more permissions
        }
    }


    public Uri getImageUri(Context inContext,Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),inImage,"Title",null);
        return Uri.parse(path);
    }

    public String getRealPathFromUri(Uri uri)
    {
        String path = "";
        if(getContentResolver()!=null)
        {
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            if(cursor!=null)
            {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }



    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }




    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            final String child = Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);
            StorageReference storageReference2nd = storageReference.child(child);

            String name = Name.getText().toString().trim();
            String phone = Phone.getText().toString().trim();
            String reportingperson = ReportingPerson.getText().toString().trim();
            String purpose = Purpose.getText().toString().trim();
            String electronics = Electronics.getText().toString().trim();
            //databaseReference.child(Database_Path + System.currentTimeMillis()).setValue(name);
            //databaseReference.child(Database_Path + System.currentTimeMillis()).setValue(phone);
            //databaseReference.child(Database_Path + System.currentTimeMillis()).setValue(reportingperson);

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = Name.getText().toString().trim();
                            String TempPhone = Phone.getText().toString().trim();
                            String TempReportingPerson = ReportingPerson.getText().toString().trim();
                            String TempPurpose = Purpose.getText().toString().trim();
                            String TempElectronics = Electronics.getText().toString().trim();
                            Date date=java.util.Calendar.getInstance().getTime();
                            String DateandTime = date.toString();
                            String ImageID = child ;
                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();


                            @SuppressWarnings("VisibleForTests")
                            Upload imageUploadInfo = new Upload(TempImageName,TempPhone,TempReportingPerson,TempPurpose,TempElectronics,DateandTime,ImageID, taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            // Getting image upload ID.
                            String ImageUploadId = databaseReference.push().getKey();

                            // Adding image upload id s child element into databaseReference.
                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);

                            Name.getText().clear();
                            Phone.getText().clear();
                            ReportingPerson.getText().clear();
                            Purpose.getText().clear();
                            Electronics.getText().clear();
                            SelectImage.setImageBitmap(null);
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(First_Page.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });
        }
        else {

            Toast.makeText(First_Page.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }


    public void notificationCall(){

        String notificationstring =  ReportingPerson.getText().toString().trim();

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.friends)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.friends))
                .setContentTitle("Notification")
                .setContentText("New Visitor For " + notificationstring);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());

    }


}





