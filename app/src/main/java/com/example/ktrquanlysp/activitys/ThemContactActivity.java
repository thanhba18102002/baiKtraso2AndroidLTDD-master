package com.example.ktrquanlysp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ktrquanlysp.MainActivity;
import com.example.ktrquanlysp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ThemContactActivity extends AppCompatActivity {
    ImageButton btnCapture;
    ImageButton btnChoose;
    ImageView imgPicture;
    Bitmap selectedBitmap;
    EditText edtId,edtTen,edtPhone,edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sanpham);
        addControls();
        addEvents();
    }
    public void addControls()
    {
        btnCapture = findViewById(R.id.btnCapture);
        btnChoose= findViewById(R.id.btnChoose);
        imgPicture=findViewById(R.id.imgPicture);
        edtId=findViewById(R.id.edtContactId);
        edtTen=findViewById(R.id.edtTen);
        edtPhone=findViewById(R.id.edtPhone);
        edtEmail=findViewById(R.id.edtEmail);
    }
    public void addEvents() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePicture();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
    }
    //xử lý chọn hình
    private void choosePicture() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 200);//one can be replaced with any action code
    }
    //xử lý chụp hình
    private void capturePicture() {
        Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cInt,100);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
//xử lý lấy ảnh trực tiếp lúc chụp hình:
            selectedBitmap = (Bitmap) data.getExtras().get("data");
            imgPicture.setImageBitmap(selectedBitmap);
        } else if (requestCode == 200 && resultCode == RESULT_OK) {
            try {
//xử lý lấy ảnh chọn từ điện thoại:
                Uri imageUri = data.getData();
                selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPicture.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void xuLyThemMoi(View view) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
//Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
            DatabaseReference myRef = database.getReference("sanphams");
            String contactId=edtId.getText().toString();
            String ten = edtTen.getText().toString();
            String phone = edtPhone.getText().toString();
            String email = edtEmail.getText().toString();
            myRef.child(contactId).child("giasp").setValue(phone);
            myRef.child(contactId).child("loaisp").setValue(email);
            myRef.child(contactId).child("tensp").setValue(ten);

//đưa bitmap về base64string:
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String imgeEncoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            myRef.child(contactId).child("picture").setValue(imgeEncoded);

            Intent intent=new Intent(ThemContactActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"Thêm sản phẩm thành công",Toast.LENGTH_LONG).show();
            finish();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Error:"+ex.toString(),Toast.LENGTH_LONG).show();
        }
    }
}