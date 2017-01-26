package com.unimate;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.unimate.model.Event;
import com.unimate.model.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.R.attr.bitmap;

public class NewGroupActivity extends BaseActivity {

    MessageAdapter adapter;
    ListView listView;
    EditText messageEditText;
    FloatingActionButton fab;
    ImageView resultImage;

    // [START declare_database_ref]
    private DatabaseReference mDatabaseRef;
    // [END declare_database_ref]

    private FirebaseDatabase mDatabase;

    private FirebaseAuth mAuth;

    private boolean textEntered;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private FirebaseStorage storage;

    private String groupNameString;

    private StorageReference storageRef;

    private ListView messageListView;

    private String startTimeString;
    private String endTimeString;
    private String groupDescriptionString;
    private String groupLocationString;
    private String getGroupNameString;
    private String cameFromActivityString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        /*if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }*/

        storage = FirebaseStorage.getInstance();

        // [START initialize_database_ref]
        mDatabaseRef = mDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

         storageRef = storage.getReferenceFromUrl("gs://unimate-93283.appspot.com");


        fab = (FloatingActionButton) findViewById(R.id.fab);
        resultImage = (ImageView)findViewById(R.id.result_view);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);

        Intent intent = getIntent();
        String memberCountString = intent.getStringExtra("memberCount");
        startTimeString = intent.getStringExtra("startTime");
        endTimeString = intent.getStringExtra("endTime");
        groupDescriptionString = intent.getStringExtra("groupDescription");
        groupNameString = intent.getStringExtra("groupName");
        groupLocationString = intent.getStringExtra("groupLocation");
        cameFromActivityString = intent.getStringExtra("cameFromActivity");

        toolbar.setTitle(groupNameString);

        final ArrayList<Message> messageList = new ArrayList<>();

        messageEditText = (EditText)findViewById(R.id.messageEditText);

        messageListView = (ListView)findViewById(R.id.message_list);


        showProgressDialog();

        // pull messages from the server
        mDatabaseRef.child("messages").child(groupNameString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                messageList.clear();


                for(DataSnapshot d: dataSnapshot.getChildren()){
                    final Message m = d.getValue(Message.class);

                    /*check if message was image*/
                    if(m.getImage() == 1) {
                        StorageReference islandRef = storageRef.child(m.getMessageText()+ ".jpg");

                        final long ONE_MEGABYTE = 1024 * 1024;
                        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                //Toast.makeText(NewGroupActivity.this, "new image has been loaded: " + m.getMessageText(), Toast.LENGTH_SHORT).show();

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                m.setBmp(bitmap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //Toast.makeText(NewGroupActivity.this, "new image has NOT been loaded: " + m.getMessageText(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    messageList.add(m);
                }
                // setup adapter
                adapter=new MessageAdapter(NewGroupActivity.this, messageList);
                messageListView.setAdapter(adapter);

                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //change icon of floating button when text is entered
                if(messageEditText.getText().toString().length() > 0){
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_send_black_24dp));
                    textEntered = true;
                }
                else{
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo_black_24dp));
                    textEntered = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textEntered){
                    DatabaseReference messagesRef = mDatabaseRef.child("messages").child(groupNameString).push();

                    Message m = new Message();
                    m.setImage(0);
                    m.setMessageText(messageEditText.getText().toString());
                    java.util.Date date= new java.util.Date();
                    // m.setTimestamp(new Timestamp(date.getTime()));
                    try{
                        m.setSender(mAuth.getCurrentUser().getEmail());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    messagesRef.setValue(m);

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    messageEditText.setText("");

                }
                else{
                    dispatchTakePictureIntent();
                }
            }
        });




    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            resultImage.setImageBitmap(imageBitmap);


            final String nameOfImage = String.valueOf(System.nanoTime());

            // Create a reference to "mountains.jpg"
            StorageReference mountainsRef = storageRef.child(nameOfImage+".jpg");

            // Create a reference to 'images/mountains.jpg'
            StorageReference mountainImagesRef = storageRef.child("images/" + nameOfImage + ".jpg");

            // While the file names are the same, the references point to different files
            mountainsRef.getName().equals(mountainImagesRef.getName());    // true
            mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false


            // Get the data from an ImageView as bytes
            resultImage.setDrawingCacheEnabled(true);
            resultImage.buildDrawingCache();
            Bitmap bitmap = resultImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataArray = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(dataArray);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                      /* +++++++++++++++++++++++++++++++++ CERATE MESSAGE FOR IMAGE ++++++++++++++++++++++ */
                    DatabaseReference messagesRef = mDatabaseRef.child("messages").child(groupNameString).push();

                    Message m = new Message();
                    m.setImage(1);
                    m.setMessageText(nameOfImage);
                    java.util.Date date= new java.util.Date();
                    // m.setTimestamp(new Timestamp(date.getTime()));
                    try{
                        m.setSender(mAuth.getCurrentUser().getEmail());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    messagesRef.setValue(m);

                    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ //
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        // add one to the membercounter:
        mDatabaseRef.child("events").child(groupNameString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Event e = dataSnapshot.getValue(Event.class);
                int countBefore = e.getMemberCount();
                e.setMemberCount(countBefore-1);
                mDatabaseRef.child("events").child(groupNameString).setValue(e);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(cameFromActivityString.equals("create")) {
            Intent intent = new Intent(NewGroupActivity.this, DescriptionActivity.class);

            intent.putExtra("startTime", startTimeString);
            intent.putExtra("endTime", endTimeString);
            intent.putExtra("groupName", groupNameString);
            intent.putExtra("groupLocation", groupLocationString);
            intent.putExtra("groupDescription", groupDescriptionString);
            intent.putExtra("cameFromActivity","newGroup");

            startActivity(intent);
        }
        else{
            super.onBackPressed();
        }
    }
}
