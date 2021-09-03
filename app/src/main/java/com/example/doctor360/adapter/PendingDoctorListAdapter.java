package com.example.doctor360.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.activity.PendingDoctorDescriptionActivity;
import com.example.doctor360.model.PendingDoctorReceiveParams;

import java.util.ArrayList;
import java.util.List;

public class PendingDoctorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<PendingDoctorReceiveParams.DataBean> pendingDoctorList;
    private static final String TAG = "DoctorListAdapter";

    public PendingDoctorListAdapter(List<PendingDoctorReceiveParams.DataBean> pendingList, Context _context){
        this.pendingDoctorList = pendingList;
        this.context = _context;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_list_single_item, parent, false);
        PendingDoctorListAdapter.DoctorViewHolder viewHolder= new PendingDoctorListAdapter.DoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final PendingDoctorReceiveParams.DataBean receiveParams = pendingDoctorList.get(position);

        final PendingDoctorListAdapter.DoctorViewHolder doctorViewHolder = (DoctorViewHolder) holder;
        doctorViewHolder.nameTxt.setText("DR. "+receiveParams.getName());
        doctorViewHolder.mobileTxt.setText(receiveParams.getMobile());
        doctorViewHolder.emailTxt.setText(receiveParams.getEmail());
        doctorViewHolder.genderTxt.setText(receiveParams.getGender());

        int doctorStatus = receiveParams.getStatus();
        if(doctorStatus == 0)
            doctorViewHolder.statusTxt.setText(R.string.unverified);
        else
            doctorViewHolder.statusTxt.setText(R.string.verified);

        doctorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Position " + position);
                Intent intent=new Intent(view.getContext(), PendingDoctorDescriptionActivity.class);
                intent.putExtra("doctor_id_adapter", receiveParams.get_id());
                intent.putExtra("doctor_name_adapter", receiveParams.getName());
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingDoctorList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt,mobileTxt, emailTxt, genderTxt, statusTxt;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.doctorNameTXT);
            mobileTxt= itemView.findViewById(R.id.doctorMobileTXT);
            emailTxt= itemView.findViewById(R.id.doctorEmailTXT);
            genderTxt= itemView.findViewById(R.id.doctorGenderTXT);
            statusTxt = itemView.findViewById(R.id.doctorStatusTXT);

        }

    }
}
