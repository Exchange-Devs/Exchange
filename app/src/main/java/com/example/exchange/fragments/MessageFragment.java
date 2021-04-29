package com.example.exchange.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exchange.Message;
import com.example.exchange.MessageAdapter;
import com.example.exchange.R;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MessageFragment extends Fragment
{
    public static final String TAG = "MessageFragment";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(3);
    private RecyclerView rvChat;
    private ArrayList<Message> mMessages;
    private MessageAdapter mAdapter;
    private EditText etMessage;
    private ImageButton btSend;
    boolean mFirstLoad;
    private Handler myHandler = new android.os.Handler();
    private Runnable mRefreshMessagesRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        etMessage = view.findViewById(R.id.etMessage);
        btSend = view.findViewById(R.id.btSend);
        rvChat = view.findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String user = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new MessageAdapter(getActivity(), user, mMessages);
        rvChat.setAdapter(mAdapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvChat.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);


        btSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setBody(data);
                message.setUser(ParseUser.getCurrentUser());
                message.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(com.parse.ParseException e)
                    {
                        if(e != null)
                        {
                            Log.e(TAG, "Error", e);
                        }
                        if(getActivity() != null)
                        Toast.makeText(getActivity(), "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });
    }
    void refreshMessages()
    {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByDescending("createdAt");

        query.findInBackground((messages, e) -> {
            if (e == null) {
                mMessages.clear();
                mMessages.addAll(messages);
                mAdapter.notifyDataSetChanged(); // update adapter
                // Scroll to the bottom of the list on initial load
                if (mFirstLoad) {
                    rvChat.scrollToPosition(0);
                    mFirstLoad = false;
                }
            } else {
                Log.e("message", "Error Loading Messages" + e);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Only start checking for new messages when the app becomes active in foreground
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    @Override
    public void onPause()
    {
        // Stop background task from refreshing messages, to avoid unnecessary traffic & battery drain
        myHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}

