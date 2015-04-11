package com.sdk.snippets.activities;

import android.app.Activity;
import android.os.Bundle;
import com.sdk.snippets.R;
import com.sdk.snippets.SnippetsList;
import com.sdk.snippets.modules.SnippetsChat;

public class ChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snippets_list);

        SnippetsChat snippets = new SnippetsChat(this);
        SnippetsList list = (SnippetsList) findViewById(R.id.list);
        list.initialize(snippets);
    }
}