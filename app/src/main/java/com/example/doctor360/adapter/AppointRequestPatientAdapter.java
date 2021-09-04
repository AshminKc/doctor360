package com.example.doctor360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.MainModel;
import com.example.doctor360.R;
import com.example.doctor360.utils.SquareImageView;

import java.util.List;

public class AppointRequestPatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int limit = 3;
    Context context;
    List<MainModel> models;
    private static final String TAG = "AppointRequestPatientAd";

    public AppointRequestPatientAdapter(List<MainModel> mainModels, Context _context){
        this.models = mainModels;
        this.context = _context;
    }

    @NonNull
    @Override
    public AppointRequestPatientAdapter.DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_single_item, parent, false);
        AppointRequestPatientAdapter.DoctorViewHolder viewHolder= new AppointRequestPatientAdapter.DoctorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final AppointRequestPatientAdapter.DoctorViewHolder doctorViewHolder = (AppointRequestPatientAdapter.DoctorViewHolder) holder;
        doctorViewHolder.nameTxt.setText(models.get(position).getName());
        doctorViewHolder.mobileTxt.setText(models.get(position).getMobile());
        doctorViewHolder.imgProfile.setImageResource(models.get(position).getImage());

        /*if(receiveParams.getProfileImg()!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            String imageString = receiveParams.getProfileImg();
            imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            doctorViewHolder.imgProfile.setImageBitmap(decodedImage);
        } else {
            doctorViewHolder.imgProfile.setImageResource(R.drawable.noimage);
        }*/


        /*doctorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Position " + position);
                Intent intent=new Intent(view.getContext(), VerifiedDoctorDescriptionActivity.class);
                intent.putExtra("doctor_id_adapter", receiveParams.get_id());
                intent.putExtra("doctor_name_adapter", receiveParams.getName());
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });*/

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

        TextView nameTxt,mobileTxt;
        SquareImageView imgProfile;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.txtPatientProfileName);
            mobileTxt= itemView.findViewById(R.id.txtPatientProfileMobile);
            imgProfile = itemView.findViewById(R.id.imagePatientProfile);

        }

    }
}
