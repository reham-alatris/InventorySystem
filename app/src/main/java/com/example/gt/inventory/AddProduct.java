package com.example.gt.inventory;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gt.inventory.data.ProductContract.ProductEntry;

import static com.example.gt.inventory.R.id.quantity;


public class AddProduct extends AppCompatActivity {
    String mImageURI;
    ImageView image;
    Context mContext;
    public static final int EXTERNAL_STORAGE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        final EditText name, price, quqntity;
        ImageView image;
        Button add, cancel;

        name = (EditText) findViewById(R.id.name);
        price = (EditText) findViewById(R.id.price);
        quqntity = (EditText) findViewById(quantity);
        image = (ImageView) findViewById(R.id.image);
        add = (Button) findViewById(R.id.add);
        cancel = (Button) findViewById(R.id.cancel);
        setTitle("Add Cupcake");


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString().trim();
                String Price = price.getText().toString().trim();
                String Quantity = quqntity.getText().toString().trim();
                if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(Price) || TextUtils.isEmpty(Quantity)) {
                    Toast.makeText(getBaseContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Integer quantity1 = Integer.parseInt(Quantity);
                    Integer price1 = Integer.parseInt(Price);
                    insertProduct(Name, quantity1, price1, mImageURI);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(AddProduct.this, MainActivity.class);
                AddProduct.this.startActivity(registerIntent);
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted(mContext)) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                            EXTERNAL_STORAGE);
                }
            }
        });
    }

    private void insertProduct(String name, Integer quantity, Integer price, String imageURI) {
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
        values.put(ProductEntry.COLUMN_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRICE, price);
        if (!"".equals(imageURI))
            values.put(ProductEntry.COLUMN_IMAGE, imageURI);
        getContentResolver().insert(ProductEntry.CONTENT_URI, values);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXTERNAL_STORAGE && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            mImageURI = selectedImage.toString();
        }
    }


    public boolean isStoragePermissionGranted(Context mContext) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions
                        (this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }
}