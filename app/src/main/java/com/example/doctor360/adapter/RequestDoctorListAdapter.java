package com.example.doctor360.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.activity.RequestDoctorDetailsActivity;
import com.example.doctor360.activity.VerifiedDoctorDescriptionActivity;
import com.example.doctor360.model.PendingDoctorReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.utils.SquareImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class RequestDoctorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<VerifiedDoctorReceiveParams.DataBean> verifiedDoctorList;
    private static final String TAG = "RequestDoctorListAdapte";

    public RequestDoctorListAdapter(List<VerifiedDoctorReceiveParams.DataBean> verifiedList, Context _context){
        this.verifiedDoctorList = verifiedList;
        this.context = _context;
    }

    @NonNull
    @Override
    public RequestDoctorListAdapter.RequestDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_doctor_single_item, parent, false);
        RequestDoctorListAdapter.RequestDoctorViewHolder viewHolder= new RequestDoctorListAdapter.RequestDoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final VerifiedDoctorReceiveParams.DataBean receiveParams = verifiedDoctorList.get(position);

        final RequestDoctorListAdapter.RequestDoctorViewHolder viewHolder = (RequestDoctorListAdapter.RequestDoctorViewHolder) holder;
        viewHolder.nameTxt.setText("DR. "+receiveParams.getName());
        viewHolder.mobileTxt.setText(receiveParams.getMobile());
        viewHolder.emailTxt.setText(receiveParams.getEmail());
        viewHolder.specTxt.setText(receiveParams.getSpecialization());

        int status = receiveParams.getStatus();
        if(status == 0)
            viewHolder.statusTxt.setText(R.string.unverified);
        else
            viewHolder.statusTxt.setText(R.string.verified);

        if(receiveParams.getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            viewHolder.imgProfile.setImageBitmap(decodedImage);
        } else {
            viewHolder.imgProfile.setImageResource(R.drawable.noimage);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Position " + position);
                Intent intent=new Intent(view.getContext(), RequestDoctorDetailsActivity.class);
                intent.putExtra("obj1", receiveParams);
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return verifiedDoctorList.size();
    }

    public class RequestDoctorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt,mobileTxt, emailTxt, specTxt, statusTxt;
        SquareImageView imgProfile;

        public RequestDoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.requestDoctorProfilePic);
            nameTxt = itemView.findViewById(R.id.requestDoctorNameTXT);
            mobileTxt= itemView.findViewById(R.id.requestDoctorMobileTXT);
            emailTxt= itemView.findViewById(R.id.requestDoctorEmailTXT);
            specTxt = itemView.findViewById(R.id.requestDoctorSpecTXT);
            statusTxt = itemView.findViewById(R.id.requestdoctorStatusTXT);

        }

    }
}
