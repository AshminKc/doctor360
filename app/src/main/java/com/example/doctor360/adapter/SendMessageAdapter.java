package com.example.doctor360.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctor360.ChatModel;
import com.example.doctor360.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SendMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ChatModel> stringList;

    public SendMessageAdapter(Context context, ArrayList<ChatModel> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public SendMessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_message_single_item, parent, false);
        SendMessageAdapter.MessageViewHolder viewHolder= new SendMessageAdapter.MessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ChatModel sendParams = stringList.get(position);

        final SendMessageAdapter.MessageViewHolder viewHolder = (SendMessageAdapter.MessageViewHolder) holder;
        viewHolder.txtMessage.setText(sendParams.getPatientMessage());
        viewHolder.txtDate.setText(sendParams.getChatDate());
        viewHolder.txtTime.setText(sendParams.getChatTime());
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime, txtMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtChatDate);
            txtTime = itemView.findViewById(R.id.txtChatTime);
            txtMessage = itemView.findViewById(R.id.txtChatMessage);
        }
    }

}

