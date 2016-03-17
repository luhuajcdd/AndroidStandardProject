package com.jacoco;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;

import com.dehua.android.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class JacocoInstrumentation extends Instrumentation{
    public static String TAG = "JacocoInstrumentation:";
    public static String COVERAGE_FILE_PATH;
    private Intent mIntent;

    public JacocoInstrumentation() {

    }

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        mIntent = new Intent(getTargetContext(), LoginActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Looper.prepare();
        startActivitySync(mIntent);
    }

    public static void generateCoverageReport() {
        if (TextUtils.isEmpty(COVERAGE_FILE_PATH)){
            initCoverageFile();
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(COVERAGE_FILE_PATH, false);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);

            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void initCoverageFile(){
        boolean result = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (result) {
            COVERAGE_FILE_PATH = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + "coverage.ec";
            File file = new File(COVERAGE_FILE_PATH);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
