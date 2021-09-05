package com.example.doctor360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.R;
import com.example.doctor360.model.HospitalListReceiveParams;
import com.example.doctor360.model.VerifiedDoctorReceiveParams;
import com.example.doctor360.utils.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HospitalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int limit = 5;
    Context context;
    List<HospitalListReceiveParams.DataBean> hospitalList;
    private static final String TAG = "DoctorListAdapter";

    public HospitalListAdapter(List<HospitalListReceiveParams.DataBean> verifiedList, Context _context){
        this.hospitalList = verifiedList;
        this.context = _context;
    }

    @NonNull
    @Override
    public HospitalListAdapter.HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_list_single_item, parent, false);
        HospitalListAdapter.HospitalViewHolder viewHolder= new HospitalListAdapter.HospitalViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final HospitalListReceiveParams.DataBean receiveParams = hospitalList.get(position);

        final HospitalListAdapter.HospitalViewHolder hospitalViewHolder = (HospitalListAdapter.HospitalViewHolder) holder;

        if(receiveParams.getName()!=null)
            hospitalViewHolder.nameTxt.setText(receiveParams.getName());
        else
            hospitalViewHolder.nameTxt.setText("NA");

        if(receiveParams.getAddress()!=null)
            hospitalViewHolder.addressTxt.setText(receiveParams.getAddress());
        else
            hospitalViewHolder.addressTxt.setText("NA");

        if(receiveParams.getPhone()!=null)
            hospitalViewHolder.phoneTxt.setText(receiveParams.getPhone());
        else
            hospitalViewHolder.phoneTxt.setText("NA");

        if(receiveParams.getWebsite()!=null)
            hospitalViewHolder.websiteTxt.setText(receiveParams.getWebsite());
        else
            hospitalViewHolder.websiteTxt.setText("NA");

            Picasso.with(context)
                    .load(R.drawable.hospital)
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .into(hospitalViewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        if(hospitalList.size() > limit){
            return limit;
        }
        else
        {
            return hospitalList.size();
        }
    }

    public class HospitalViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, phoneTxt, addressTxt, websiteTxt;
        SquareImageView imageView;

        public HospitalViewHolder (@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.txtHospitalName);
            phoneTxt= itemView.findViewById(R.id.txtHospitalPhone);
            addressTxt= itemView.findViewById(R.id.txtHospitalAddress);
            websiteTxt= itemView.findViewById(R.id.txtHospitalWebsite);
            imageView = itemView.findViewById(R.id.imageHospital);

        }

    }
}
