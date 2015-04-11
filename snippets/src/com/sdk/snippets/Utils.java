package com.sdk.snippets;

import android.content.Context;

import com.quickblox.core.helper.FileHelper;

import java.io.*;

/**
 * Created by vfite on 23.01.14.
 */
public class Utils {


    public static String getContentFromFile(InputStream is) {
        char[] buffer = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while (bufferedReader.read(buffer, 0, 1024) != -1) {
                stringBuilder.append(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static File getFileFromRawResource(int fileId, Context context) {
        InputStream is = context.getResources().openRawResource(fileId);
        File file = FileHelper.getFileInputStream(is, "sample" + fileId + ".txt", "qb_snippets12");
        return file;
    }
}
