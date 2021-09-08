package com.example.doctor360.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.model.ChatAcceptedPatientReceiveParams;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewPatientChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ChatAcceptedPatientReceiveParams.DataBean> acceptedList;
    private static final String TAG = "ViewPatientChatAdapter";

    public ViewPatientChatAdapter(Context context, List<ChatAcceptedPatientReceiveParams.DataBean> acceptedList) {
        this.context = context;
        this.acceptedList = acceptedList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_accepted_patient_single_item, parent, false);
        ViewPatientChatAdapter.ChatViewHolder viewHolder= new ViewPatientChatAdapter.ChatViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChatAcceptedPatientReceiveParams.DataBean receiveParams = acceptedList.get(position);

        final ViewPatientChatAdapter.ChatViewHolder viewHolder = (ViewPatientChatAdapter.ChatViewHolder) holder;
        viewHolder.ptxtDoctorName.setText("DR. "+receiveParams.getDoctorId().getName());

        int status = receiveParams.getRequestStatus();
        if(status == 1)
            viewHolder.ptxtStatus.setText("Accepted");
        else
            viewHolder.ptxtStatus.setText("Rejected");

        if(receiveParams.getDoctorId().getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getDoctorId().getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            viewHolder.pdoctorImage.setImageBitmap(decodedImage);
        } else {
            viewHolder.pdoctorImage.setImageResource(R.drawable.noimage);
        }
    }

    @Override
    public int getItemCount() {
        return acceptedList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView ptxtDoctorName, ptxtStatus;
        CircleImageView pdoctorImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            pdoctorImage = itemView.findViewById(R.id.chatDoctorImage);
            ptxtDoctorName = itemView.findViewById(R.id.chatDoctorName);
            ptxtStatus = itemView.findViewById(R.id.chatDoctorStatus);
        }
    }
}
