package com.example.exchange;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;
    private List<Message> mMessages;
    private Context mContext;
    private String User;


    public MessageAdapter(Context context, String userId, List<Message> messages)
    {
        this.mMessages = messages;
        this.User = userId;
        this.mContext = context;
    }

    @Override
    public int getItemCount()
    {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isMe(position))
        {
            return MESSAGE_OUTGOING;
        }
        else
            {
            return MESSAGE_INCOMING;
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position)
    {
        Message message = mMessages.get(position);
        try {
            holder.bindMessage(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean isMe(int position)
    {
        Message message = mMessages.get(position);
        return message.getUser() != null && message.getUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId());
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message) throws ParseException;
    }

        public class IncomingMessageViewHolder extends MessageViewHolder
        {
            ImageView imageOther;
            TextView body;
            TextView name;

            public IncomingMessageViewHolder(View itemView)
            {
                super(itemView);
                imageOther = itemView.findViewById(R.id.ivProfileOther);
                body = itemView.findViewById(R.id.tvBody);
                name = itemView.findViewById(R.id.tvName);
            }

            @Override
            public void bindMessage(Message message) throws ParseException
            {
                ParseFile file =  message.getUser().fetchIfNeeded().getParseFile("profileImage");
                if(file != null)
                {
                    Glide.with(mContext)
                          .load(file.getUrl())
                            .circleCrop() // create an effect of a round profile picture
                            .into(imageOther);
                }
                else
                {
                    Glide.with(mContext).load(R.drawable.profile_pic).circleCrop().into(imageOther);
                }

                body.setText(message.getBody());

                    name.setText(message.getUser().fetchIfNeeded().getUsername());
            }
        }

        public class OutgoingMessageViewHolder extends MessageViewHolder
        {
            ImageView imageMe;
            TextView body;

            public OutgoingMessageViewHolder(View itemView)
            {
                super(itemView);
                imageMe = itemView.findViewById(R.id.ivProfileMe);
                body = itemView.findViewById(R.id.tvBody);
            }

            @Override
            public void bindMessage(Message message) throws ParseException
            {
                ParseFile file =  message.getUser().fetchIfNeeded().getParseFile("profileImage");
                if(file != null)
                {
                    Glide.with(mContext)
                            .load(file.getUrl())
                            .circleCrop() // create an effect of a round profile picture
                            .into(imageMe);
                }
                else
                {
                    Glide.with(mContext).load(R.drawable.profile_pic).circleCrop().into(imageMe);
                }
                body.setText(message.getBody());
            }
        }
    private static String getProfileUrl(final String userId)
    {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
}

