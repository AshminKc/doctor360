package com.example.doctor360.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.doctor360.activity.DoctorChatActivity;
import com.example.doctor360.model.ChatAccptedDoctorReceiveParams;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ChatAccptedDoctorReceiveParams.DataBean> acceptedList;
    private static final String TAG = "DoctorChatListAdapter";

    public DoctorChatListAdapter(Context context, List<ChatAccptedDoctorReceiveParams.DataBean> acceptedList) {
        this.context = context;
        this.acceptedList = acceptedList;
    }

    @NonNull
    @Override
    public DoctorChatListAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_chat_list_single_item, parent, false);
        DoctorChatListAdapter.ChatViewHolder viewHolder= new DoctorChatListAdapter.ChatViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChatAccptedDoctorReceiveParams.DataBean receiveParams = acceptedList.get(position);

        final DoctorChatListAdapter.ChatViewHolder viewHolder = (DoctorChatListAdapter.ChatViewHolder) holder;
        viewHolder.ptxtPatientName.setText(receiveParams.getPatientId().getName());

        int status = receiveParams.getRequestStatus();
        if(status == 1)
            viewHolder.ptxtStatus.setText("Accepted");
        else
            viewHolder.ptxtStatus.setText("Rejected");

        if(receiveParams.getPatientId().getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getPatientId().getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            viewHolder.pPatientImage.setImageBitmap(decodedImage);
        } else {
            viewHolder.pPatientImage.setImageResource(R.drawable.noimage);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), DoctorChatActivity.class);
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return acceptedList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView ptxtPatientName, ptxtStatus;
        CircleImageView pPatientImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            pPatientImage = itemView.findViewById(R.id.patientChatImage);
            ptxtPatientName = itemView.findViewById(R.id.patientChatName);
            ptxtStatus = itemView.findViewById(R.id.patientChatStatus);
        }
    }
}
