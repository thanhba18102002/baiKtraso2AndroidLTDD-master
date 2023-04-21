package com.example.ktrquanlysp.adapter;

import android.app.Activity;
import androidx.annotation.Nullable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ktrquanlysp.R;
import com.example.ktrquanlysp.model.SanPham;

public class SanPhamAdapter extends ArrayAdapter<SanPham> {
    Activity context;
    int resource;
    public SanPhamAdapter(Activity context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View custom=context.getLayoutInflater().inflate(resource,null);
//        TextView txtId=custom.findViewById(R.id.txtId);
        ImageView imgSp = custom.findViewById(R.id.ImageView);
        TextView txtName=custom.findViewById(R.id.txtName);
        TextView txtPhone=custom.findViewById(R.id.txtLoai);
        TextView txtEmail=custom.findViewById(R.id.txtgia);
        SanPham sanPham =getItem(position);
//        txtId.setText(sanPham.getContactId());
        if(sanPham.getPicture()!=null) {
            byte[] decodedString = Base64.decode(sanPham.getPicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgSp.setImageBitmap(decodedByte);
        }
        txtName.setText(sanPham.getTensp());
        txtPhone.setText(sanPham.getLoaisp());
        txtEmail.setText(sanPham.getGiasp());
        return custom;
    }
}
