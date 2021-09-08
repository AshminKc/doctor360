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
import com.example.doctor360.activity.PatientChatActivity;
import com.example.doctor360.model.ChatAcceptedPatientReceiveParams;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ChatAcceptedPatientReceiveParams.DataBean> acceptedList;
    private static final String TAG = "PatientChatListAdapter";

    public PatientChatListAdapter(Context context, List<ChatAcceptedPatientReceiveParams.DataBean> acceptedList) {
        this.context = context;
        this.acceptedList = acceptedList;
    }

    @NonNull
    @Override
    public PatientChatListAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_chat_list_single_item, parent, false);
        PatientChatListAdapter.ChatViewHolder viewHolder= new PatientChatListAdapter.ChatViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChatAcceptedPatientReceiveParams.DataBean receiveParams = acceptedList.get(position);

        final PatientChatListAdapter.ChatViewHolder viewHolder = (PatientChatListAdapter.ChatViewHolder) holder;
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

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), PatientChatActivity.class);
                intent.putExtra("doc_id",receiveParams.getDoctorId().get_id());
                intent.putExtra("doc_name", receiveParams.getDoctorId().getName());
                intent.putExtra("doc_photo", receiveParams.getDoctorId().getProfileImg());
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

        TextView ptxtDoctorName, ptxtStatus;
        CircleImageView pdoctorImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            pdoctorImage = itemView.findViewById(R.id.doctorChatImage);
            ptxtDoctorName = itemView.findViewById(R.id.doctorChatName);
            ptxtStatus = itemView.findViewById(R.id.doctorChatStatus);
        }
    }
}
