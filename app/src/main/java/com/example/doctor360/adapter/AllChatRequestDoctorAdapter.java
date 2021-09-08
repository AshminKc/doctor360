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
import com.example.doctor360.activity.ChatRequestPatientDetailsActivity;
import com.example.doctor360.model.ViewChatRequestDoctorReceiveParams;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllChatRequestDoctorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context _context;
    List<ViewChatRequestDoctorReceiveParams.DataBean> dataBeanList;
    private static final String TAG = "AllChatRequestDoctorAda";

    public AllChatRequestDoctorAdapter(Context _context, List<ViewChatRequestDoctorReceiveParams.DataBean> dataBeanList) {
        this._context = _context;
        this.dataBeanList = dataBeanList;
    }

    @NonNull
    @Override
    public AllChatRequestDoctorAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_request_doctor_single_item, parent, false);
        AllChatRequestDoctorAdapter.ChatViewHolder viewHolder= new AllChatRequestDoctorAdapter.ChatViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        final ViewChatRequestDoctorReceiveParams.DataBean receiveParams = dataBeanList.get(position);

        final AllChatRequestDoctorAdapter.ChatViewHolder doctorViewHolder = (AllChatRequestDoctorAdapter.ChatViewHolder) holder;
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
            doctorViewHolder.imageView.setImageBitmap(decodedImage);
        } else {
            doctorViewHolder.imageView.setImageResource(R.drawable.noimage);
        }

        doctorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = receiveParams.getRequestStatus();
                Log.d(TAG, "onClick: Position " + position);
                Intent intent=new Intent(view.getContext(), ChatRequestPatientDetailsActivity.class);
                intent.putExtra("chat_patient_id", receiveParams.getPatientId().get_id());
                intent.putExtra("chat_patient_name", receiveParams.getPatientId().getName());
                intent.putExtra("chat_id", receiveParams.get_id());
                intent.putExtra("chat_status", status);
                Activity activity = (Activity) _context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, statusTxt;
        CircleImageView imageView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.fromPatientName);
            statusTxt = itemView.findViewById(R.id.fromPatientStatus);
            imageView = itemView.findViewById(R.id.fromPatientImage);
        }

    }

}
