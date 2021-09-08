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
import com.example.doctor360.model.ViewDoctorScheduledAppointmentReceiveParams;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ViewDoctorAppointAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ViewDoctorScheduledAppointmentReceiveParams.DataBean> scheduledList;
    private static final String TAG = "ViewDoctotAppointAdapte";

    public ViewDoctorAppointAdapter (Context context, List<ViewDoctorScheduledAppointmentReceiveParams.DataBean> scheduledList) {
        this.context = context;
        this.scheduledList = scheduledList;
    }

    @NonNull
    @Override
    public ViewDoctorAppointAdapter.ScheduledViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_scheduled_appointment_single_item, parent, false);
        ViewDoctorAppointAdapter.ScheduledViewHolder viewHolder= new ViewDoctorAppointAdapter.ScheduledViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewDoctorScheduledAppointmentReceiveParams.DataBean receiveParams = scheduledList.get(position);

        final ViewDoctorAppointAdapter.ScheduledViewHolder viewHolder = (ViewDoctorAppointAdapter.ScheduledViewHolder) holder;
        viewHolder.txtPatientName.setText(receiveParams.getPatientId().getName());
        viewHolder.txtDate.setText(receiveParams.getDate());
        viewHolder.txtTime.setText(receiveParams.getTime());
        viewHolder.txtRemarks.setText(receiveParams.getDescription());

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
        return scheduledList.size();
    }

    public class ScheduledViewHolder extends RecyclerView.ViewHolder {

        TextView txtPatientName, txtDate, txtTime, txtRemarks, txtStatus;
        ImageView patientImage;

        public ScheduledViewHolder(@NonNull View itemView) {
            super(itemView);

            patientImage = itemView.findViewById(R.id.schPatientImage);
            txtPatientName = itemView.findViewById(R.id.schPatientName);
            txtDate = itemView.findViewById(R.id.schPatientDate);
            txtTime = itemView.findViewById(R.id.schPatientTime);
            txtRemarks = itemView.findViewById(R.id.schPatientRemarks);
            txtStatus = itemView.findViewById(R.id.schPatientStatus);
        }
    }
}
