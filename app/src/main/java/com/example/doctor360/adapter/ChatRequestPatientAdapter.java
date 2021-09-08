package com.example.doctor360.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.model.ViewChatRequestDoctorReceiveParams;
import com.example.doctor360.utils.SquareImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatRequestPatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int limit = 5;
    Context context;
    List<ViewChatRequestDoctorReceiveParams.DataBean> models;
    private static final String TAG = "ChatRequestPatientAdapt";

    public ChatRequestPatientAdapter(List<ViewChatRequestDoctorReceiveParams.DataBean> mainModels, Context _context){
        this.models = mainModels;
        this.context = _context;
    }

    @NonNull
    @Override
    public ChatRequestPatientAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_home_chat_request_single_item, parent, false);
        ChatRequestPatientAdapter.DoctorViewHolder viewHolder= new ChatRequestPatientAdapter.DoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewChatRequestDoctorReceiveParams.DataBean receiveParams = models.get(position);

        final ChatRequestPatientAdapter.DoctorViewHolder doctorViewHolder = (ChatRequestPatientAdapter.DoctorViewHolder) holder;
        doctorViewHolder.nameTxt.setText(receiveParams.getPatientId().getName());

        int status = receiveParams.getRequestStatus();
        if(status == 1)
            doctorViewHolder.statusTxt.setText("Accepted");
        else
            doctorViewHolder.statusTxt.setText("Pending");

        if(receiveParams.getPatientId().getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getPatientId().getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            doctorViewHolder.imgProfile.setImageBitmap(decodedImage);
        } else {
            doctorViewHolder.imgProfile.setImageResource(R.drawable.noimage);
        }

    }

    @Override
    public int getItemCount() {
        if(models.size() > limit){
            return limit;
        }
        else
        {
            return models.size();
        }
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt,statusTxt;
        SquareImageView imgProfile;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.txtDocHomeName);
            statusTxt= itemView.findViewById(R.id.txtDocHomeStatus);
            imgProfile = itemView.findViewById(R.id.imageDocHomeProfile);

        }

    }
}
