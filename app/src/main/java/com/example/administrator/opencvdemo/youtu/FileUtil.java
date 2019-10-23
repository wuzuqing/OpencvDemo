package com.example.administrator.opencvdemo.youtu;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;


/**
 * 文件处理util
 */
public class FileUtil {
    static final String FILES_PATH = "Compressor";
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    public static final long ONE_DAY_TIME = 86400000;
    public static final int DEFAULT_CACHE_DAY = 2;

    private FileUtil() {

    }

    public static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    /**
     * 获取文件名
     *
     * @param fileName
     * @return
     */
    static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    /**
     * 根据uri获取文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * 根据uri获取真文件路径
     *
     * @param context
     * @param contentUri
     * @return
     */
    static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String realPath = cursor.getString(index);
            cursor.close();
            return realPath;
        }
    }

    static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists()) {
                if (newFile.delete()) {
                    Log.d("FileUtil", "Delete old " + newName + " file");
                }
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    public static boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 如果存在外部SD卡则返回外部缓存路径
     *
     * @param context
     * @return
     */
    public static String getAppCacheDir(Context context) {
        if (context.getExternalCacheDir() != null && isExistSDCard()) {
            return context.getExternalCacheDir().toString();
        } else {
            return context.getCacheDir().toString();
        }
    }

    /**
     * 如果存在外部SD卡则返回外部缓存路径
     *
     * @return
     */
    public static String getAppCacheDir() {
        return getAppCacheDir(BaseApplication.getAppContext());
    }

    /**
     * 获取内部存储缓存目录
     *
     * @param context 上下文对象
     * @return 当前缓存目录
     */
    public static String getInternalCacheDir(Context context) {
        return context.getCacheDir().toString();
    }

    /**
     * 获取SD卡缓存目录
     *
     * @param context 上下文对象
     * @return 当前缓存目录
     */
    public static String getExternalCacheDir(Context context) {
        if (context.getExternalCacheDir() != null && isExistSDCard()) {
            return context.getExternalCacheDir().toString();
        } else return null;
    }

    /**
     * 根据文件路径删除文件
     *
     * @param path 文件路径
     * @return 是否删除成功
     */
    public static boolean delete(String path, boolean deleteSelf) {
        return delete(new File(path), deleteSelf);
    }

    /**
     * 根据文件路径删除文件
     *
     * @param path 文件路径
     * @return 是否删除成功
     */
    public static boolean delete(String path) {
        return delete(path, false);
    }

    /**
     * 根据File对象删除文件
     *
     * @param file File对象
     * @return 是否删除成功
     */
    public static boolean delete(File file, boolean deleteSelf) {
        if (file.isFile()) {
            return file.delete();
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }
            for (File childFile : childFiles) {
                delete(childFile, true);
            }
            if (deleteSelf) {
                return file.delete();
            }
        }
        return false;
    }

    /**
     * 根据文件路径返回文件夹名称
     *
     * @param filePath 文件路径
     * @return 该文件所在的文件夹
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 创建文件夹
     *
     * @param filePath
     * @return
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.write("\r\n");
            fileWriter.write("\r\n");
            fileWriter.close();
            return true;
        } catch (IOException e) {
            Log.e("IOException occurred. ", e.getMessage(), e);
            return false;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    Log.e("IOException occurred. ", e.getMessage(), e);
                }
            }
        }
    }

    //读取文件
    public static String readFile(String filePath) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file));
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent.toString();
        } catch (IOException e) {
            Log.e("IOException occurred. ", e.getMessage(), e);
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("IOException occurred. ", e.getMessage(), e);
                }
            }
        }
    }

    public static String getFileDir(String filePath) {
        String dir;
        if (isExistSDCard()) {
            dir = BaseApplication.getAppContext().getExternalFilesDir("").getAbsolutePath();
        } else {
            dir = BaseApplication.getAppContext().getFilesDir().getAbsolutePath();
        }

        if (TextUtils.isEmpty(filePath)) return dir;
        else {
            if (filePath.startsWith(File.separator)) {
                dir += filePath;
            } else dir += File.separator + filePath;


            FileUtil.makeDirs(dir);

            return dir;
        }
    }


    /**
     * 将文件路径转成File集合
     *
     * @param filePath 文件路径
     * @return
     */
    public static List<File> str2File(String filePath) {
        List<File> files = new ArrayList<>();
        files.add(new File(filePath));
        return files;
    }

    public static String getDownApkFile( ) {
        return getAppRootFile("apk")+"updateWpyx.apk";
    }


    /**
     * 删除缓存的接口
     */
    public interface OnDeleteCacheListener {
        void finish();
    }

    /**
     * 删除图片加载框架缓存的图片文件(默认两天清除一次)
     */
    public static void deleteGlideCache() {
        long delete_time = SPUtils.getLong("delete_time");
        long now = System.currentTimeMillis();
        //三天的时间清理一次
        if ((now - delete_time) < ONE_DAY_TIME * DEFAULT_CACHE_DAY) {
            return;
        }
        delete(getImageCacheFolder());
        //将本次清除文件的时间存入偏好设置
        SPUtils.setLong("delete_time", System.currentTimeMillis());
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[4096];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
                inStream.close();
                fs.close();
                LogUtils.logd("oldPath : " + oldPath);
                LogUtils.logd("newPath : " + newPath);
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(newString(oldPath, File.separator, file[i]), newString(newPath, File.separator, file[i]));
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }
    }

    public static String newString(String... strings) {
        if (strings == null || strings.length == 0) return null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i]);
        }
        return builder.toString();

    }

    private static boolean createFileFolder = false;

    public static File mkdirStorageFileDir(Context context, String phone) {
        try {
            File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File temp = new File(filesDir.getParent(), getChild("wpyx"));
            if (!temp.exists()) temp.mkdirs();
            newFile(temp, "cache");
            File newFile = newFile(temp, phone);
            newFile(newFile, "image");
            newFile(newFile, "video");
            newFile(newFile, "music");
            newFile(newFile, "files");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFile() {
        return getAppRootFile("file");
    }

    public static String getSpFile() {
        return getAppRootFile(".sp");
    }

    public static String getDbFile() {
        return getAppRootFile("db");
    }

    public static String getAppRootFile(String file) {
        if (isExistSDCard()) {
            String wpyx = getExternalStorageDirectory() + getChild("wpyx") + file + "/";
            makeDirs(wpyx);
            return wpyx;
        } else {
            File filesDir = BaseApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            assert filesDir != null;
            File temp = new File(filesDir.getParent(), getChild("wpyx"));
            String wpyx = temp.getAbsolutePath() + File.separator + file + "/";
            makeDirs(wpyx);
            return wpyx;
        }
    }

    public static String getAppFilePath() {
        if (isExistSDCard()) {
            return getExternalStorageDirectory() + getChild("wpyx");
        } else {
            File filesDir = BaseApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File temp = new File(filesDir.getParent(), getChild("wpyx"));
            makeDirs(temp.getAbsolutePath());
            return temp.getAbsolutePath();
        }
    }

    public static String getAppFilePath(String phone) {
        if (isExistSDCard()) {
            return getExternalStorageDirectory() + getChild("wpyx") + phone;
        } else {
            File filesDir = BaseApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File temp = new File(filesDir.getParent(), getChild("wpyx") + phone);
            return temp.getAbsolutePath();
        }
    }

    public static void makeDirs() {
        makeDirs(getExerciseDingMp3FileName());
        makeDirs(getExerciseBj2Mp3FileName(1));
    }

    //    public static String getExerciseDingMp3FileName() {
//        return getFile() + "ding";
//    }
//
//    public static String getExerciseBj2Mp3FileName(int i) {
//        return getFile() + "pg" + i;
//    }
//
//    public static String getExerciseBGFileName(int i) {
//        return getFile() + "pg" + i ;
//    }
    public static String getExerciseDingMp3FileName() {
        return getFile() + "ding.mp3";
    }

    public static String getExerciseBj2Mp3FileName(int i) {
        return getFile() + "pg" + i + ".mp3";
    }
    public static String getExerciseBj2Mp4FileName(int i) {
        return getFile() + "pd" + i + ".mp4";
    }
    public static String getExerciseBGFileName(int i) {
        return getFile() + "pg" + i + ".jpeg";
    }

    //获取图片加载框架缓存的目录
    public static String getImageCacheFolder() {
        if (isExistSDCard()) {
            return getExternalStorageDirectory() + getChild("wpyx") + "cache";
        } else {
            File filesDir = BaseApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File temp = new File(filesDir.getParent(), getChild("wpyx"));
            return temp.getAbsolutePath() + File.separator + "cache";
        }
    }

    //获取下载图片文件夹
    public static String getDownImageFolder() {
        return getMyAPPFolder("image");
    }

    //获取下载音频文件夹
    public static String getDownMusicFolder() {
        return getMyAPPFolder("music");
    }

    //获取下载视频文件夹
    public static String getDownVideoFolder() {
        return getMyAPPFolder("video");
    }

    //获取应用用户的文件夹
    public static String getMyAPPFolder(String folder) {
        String phone = SPUtils.getString("phone");
        if (isExistSDCard()) {
            return getExternalStorageDirectory() + getChild("wpyx") + phone + getChild(folder);
        } else {
            File filesDir = BaseApplication.getAppContext().getFilesDir();
            File temp = new File(filesDir.getParent(), getChild("wpyx"));
            return temp.getAbsolutePath() + File.separator + phone + getChild(folder);
        }
    }

    //创建用户目录
    public static void createFileFolder(String phone) {
        try {
            if (isExistSDCard()) {
                mkdirStorageFileDir(BaseApplication.getAppContext(), phone);
                File rootFolder = Environment.getExternalStorageDirectory();
                File temp = new File(rootFolder, getChild("wpyx"));
                if (!temp.exists()) temp.mkdirs();
                newFile(temp, "cache");
                File newFile = newFile(temp, phone);
                newFile(newFile, "image");
                newFile(newFile, "video");
                newFile(newFile, "music");
                newFile(newFile, "files");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File newFile(File file, String name) {
        File temp = new File(file.getAbsolutePath(), getChild(name));
        if (!temp.exists()) temp.mkdirs();
        return temp;
    }

    private static String getChild(String name) {
        return File.separator + name + File.separator;
    }


    /**
     * 获取sd卡路径
     *
     * @return Stringpath
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 获取空闲的空间大小
     *
     * @return 空间大小
     */
    public static long getFreeSpaceBytes() {
        long freeSpaceBytes;
        final StatFs statFs = new StatFs(getSDPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            freeSpaceBytes = statFs.getAvailableBytes();
        } else {
            //noinspection deprecation
            freeSpaceBytes = statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
        }

        return freeSpaceBytes;
    }
}
