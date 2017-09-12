package com.example.gt.inventory;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gt.inventory.data.ProductContract.ProductEntry;
import static com.example.gt.inventory.R.id.price;
import static com.example.gt.inventory.R.id.quantity;


/**
 * Created by g.t on 23/07/2017.
 */

public class PCrusorAdapter extends CursorAdapter {


    public PCrusorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView Name = (TextView) view.findViewById(R.id.name);
        ImageView Image = (ImageView) view.findViewById(R.id.image);
        TextView Quantity = (TextView) view.findViewById(quantity);
        TextView Price = (TextView) view.findViewById(price);
        Button sell = (Button) view.findViewById(R.id.sell);


        final String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        final String image = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_IMAGE));
        final Integer quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_QUANTITY));
        final Integer price = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRICE));
        int idIndex = cursor.getColumnIndex(ProductEntry._ID);
        final long id = cursor.getLong(idIndex);


        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity >= 1) {
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_QUANTITY, quantity - 1);

                    Uri uri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                    context.getContentResolver().update(
                            uri,
                            values,
                            ProductEntry._ID + "=?",
                            new String[]{String.valueOf(ContentUris.parseId(uri))});
                    Log.v("values", values.toString());
                } else {
                    Toast.makeText(context, "no product to sell", Toast.LENGTH_SHORT).show();

                }
            }
        });
        Name.setText(name);
        Quantity.setText(String.valueOf(quantity));
        Price.setText(String.valueOf(price));
        if (image != null) {
            Image.setImageURI(Uri.parse(image));

        } else {
            Image.setImageResource(R.drawable.cupcake);
        }
    }

}