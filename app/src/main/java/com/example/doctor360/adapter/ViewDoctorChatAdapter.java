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
import com.example.doctor360.model.ChatAccptedDoctorReceiveParams;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ViewDoctorChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ChatAccptedDoctorReceiveParams.DataBean> acceptedList;
    private static final String TAG = "ViewDoctorChatAdapter";

    public ViewDoctorChatAdapter(Context context, List<ChatAccptedDoctorReceiveParams.DataBean> acceptedList) {
        this.context = context;
        this.acceptedList = acceptedList;
    }


    @NonNull
    @Override
    public ViewDoctorChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_accepted_doctor_single_item, parent, false);
        ViewDoctorChatAdapter.ChatViewHolder viewHolder= new ViewDoctorChatAdapter.ChatViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChatAccptedDoctorReceiveParams.DataBean receiveParams = acceptedList.get(position);

        final ViewDoctorChatAdapter.ChatViewHolder viewHolder = (ViewDoctorChatAdapter.ChatViewHolder) holder;
        viewHolder.txtPatientName.setText(receiveParams.getPatientId().getName());

        int status = receiveParams.getRequestStatus();
        if(status == 1)
            viewHolder.txtStatus.setText("Accepted");
        else
            viewHolder.txtStatus.setText("Rejected");

        if(receiveParams.getPatientId().getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getPatientId().getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            viewHolder.patientImage.setImageBitmap(decodedImage);
        } else {
            viewHolder.patientImage.setImageResource(R.drawable.noimage);
        }
    }

    @Override
    public int getItemCount() {
        return acceptedList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView txtPatientName, txtStatus;
        ImageView patientImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            patientImage = itemView.findViewById(R.id.chatPatientImage);
            txtPatientName = itemView.findViewById(R.id.chatPatientName);
            txtStatus = itemView.findViewById(R.id.chatPatientStatus);
        }
    }
}
