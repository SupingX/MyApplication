package util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class XLoggerUtil {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault());

    public static XLoggerUtil newInstance() {
        return Holder.INSTANCE;
    }

    private XLoggerUtil() {

    }

    private File createDirectory(String directoryName) {
        if (TextUtils.isEmpty(directoryName)) {
            throw new NullPointerException("directoryName is empty");
        }
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        try {
            File file = new File(externalStorageDirectory.getAbsolutePath() + File.separator + directoryName);
            // File file = new File(externalStorageDirectory, directoryName);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();//注意mkdir与mkdirs的区别
                if (mkdirs) {
                    return file;
                }
            } else {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            File file = new File(path);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (newFile) {
                    return file;
                }
            } else {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveDefault(Context context,String log){
        return save(context,"XLogger","log_" + sdf.format(new Date()) + ".txt",log);
    }

    /**
     * 保存日志文件
     * @param context context
     * @param directoryName 文件目录
     * @param fileName 文件名
     * @param log 日志
     * @return boolean
     */
    public boolean save(Context context,String directoryName,String fileName,String log) {
        File directory = createDirectory(directoryName);
        if (directory != null) {
            //String path = directory.getAbsolutePath() + File.separator + "log_" + sdf.format(new Date()) + ".txt";
            String path = directory.getAbsolutePath() + File.separator + fileName;
            return saveFile(context, path, log);
        }
        return false;
    }

    private boolean saveFile(Context context, String path, String log) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        File file = createFile(path);
        if (file == null) {
            return false;
        }

        try {
            Sink sink = Okio.sink(file);
            BufferedSink buffer = Okio.buffer(sink);
            buffer.writeString(log, Charset.forName("utf-8"));
            buffer.flush();
            // 强刷 否则 看不到
            MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* 动态权限 否则创建文件失败 */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Holder {
        private static final XLoggerUtil INSTANCE = new XLoggerUtil();
    }
}
