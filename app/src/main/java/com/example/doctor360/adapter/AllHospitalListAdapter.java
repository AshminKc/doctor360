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
import com.example.doctor360.activity.HospitalDetailsActivity;
import com.example.doctor360.model.HospitalListReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.utils.SquareImageView;

import java.util.List;

public class AllHospitalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<HospitalListReceiveParams.DataBean> allHospitalList;
    private static final String TAG = "AllHospitalListAdapter";

    public AllHospitalListAdapter(List<HospitalListReceiveParams.DataBean> hospitalList, Context _context){
        this.allHospitalList = hospitalList;
        this.context = _context;
    }

    @NonNull
    @Override
    public AllHospitalListAdapter.HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allhospital_list_single_item, parent, false);
        AllHospitalListAdapter.HospitalViewHolder viewHolder= new AllHospitalListAdapter.HospitalViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final HospitalListReceiveParams.DataBean receiveParams = allHospitalList.get(position);

        final AllHospitalListAdapter.HospitalViewHolder viewHolder = (AllHospitalListAdapter.HospitalViewHolder) holder;
        viewHolder.nameTxt.setText(receiveParams.getName());
        viewHolder.phoneTxt.setText(receiveParams.getPhone());
        viewHolder.emailTxt.setText(receiveParams.getEmail());
        viewHolder.websiteTxt.setText(receiveParams.getWebsite());
        viewHolder.contactNameTxt.setText(receiveParams.getContact_person());
        viewHolder.contactMobileTxt.setText(receiveParams.getContact_person_number());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Position " + position);
                Intent intent=new Intent(view.getContext(), HospitalDetailsActivity.class);
                intent.putExtra("hospital_name", receiveParams.getName());
                intent.putExtra("hospital_address", receiveParams.getAddress());
                intent.putExtra("hospital_phone", receiveParams.getPhone());
                intent.putExtra("hospital_email", receiveParams.getEmail());
                intent.putExtra("hospital_website", receiveParams.getWebsite());
                intent.putExtra("hospital_contact_name", receiveParams.getContact_person());
                intent.putExtra("hospital_contact_mobile", receiveParams.getContact_person_number());
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allHospitalList.size();
    }

    public class HospitalViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt,emailTxt, phoneTxt, websiteTxt, contactNameTxt, contactMobileTxt;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.hospitalNameTXT);
            emailTxt = itemView.findViewById(R.id.hospitalEmailTXT);
            phoneTxt = itemView.findViewById(R.id.hospitalPhoneTXT);
            websiteTxt = itemView.findViewById(R.id.hospitalWebsiteTXT);
            contactNameTxt = itemView.findViewById(R.id.hospitalContactNameTXT);
            contactMobileTxt = itemView.findViewById(R.id.hospitalContactMobileTXT);
        }

    }
}
