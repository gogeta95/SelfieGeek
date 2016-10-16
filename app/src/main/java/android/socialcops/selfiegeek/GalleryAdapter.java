package android.socialcops.selfiegeek;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ayush on 15-10-2016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private ArrayList<String> imageList;
    private final Context context;

    public GalleryAdapter(ArrayList<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView cameraImage;

        public ViewHolder(View itemView) {
            super(itemView);
            cameraImage = (ImageView) itemView.findViewById(R.id.image);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String address = imageList.get(position);
        File imageFile = new File(address);
        if(imageFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            holder.cameraImage.setImageBitmap(myBitmap);
        }
    }

    public void add(int position, String item) {
        imageList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = imageList.indexOf(item);
        imageList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void swapImages(ArrayList<String> data){
        imageList = data;
        notifyDataSetChanged();
    }
}
