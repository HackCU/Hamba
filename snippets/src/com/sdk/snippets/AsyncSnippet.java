package com.sdk.snippets;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by vfite on 16.01.14.
 */
public abstract class AsyncSnippet extends Snippet {
    private static final String TAG = AsyncSnippet.class.getSimpleName();
    Exception exception;
    private Context context;

    public AsyncSnippet(String title, Context context) {
        super(title);
        this.context = context;
    }

    public AsyncSnippet(String title, String subtitle, Context context) {
        super(title, subtitle);
        this.context = context;
    }

    public void setException(Exception exception){
         this.exception = exception;
    }
    public Exception getException(){
        return exception;
    }

    @Override
    public void execute() {
        exception = null;
        (new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... objects) {
                executeAsync();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
               postExecute();
            }
        }).execute();
    }

    protected void postExecute(){
        if(exception == null){
            Log.i(TAG, ">>> executed successful");
            Toast.makeText(context, " executed successful", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.i(TAG, ">>> errors:"+exception.getLocalizedMessage());
        }
    }

    public abstract void executeAsync();
}
