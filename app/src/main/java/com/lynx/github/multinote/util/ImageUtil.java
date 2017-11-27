package com.lynx.github.multinote.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rizalfahmi on 21/11/17.
 */

public class ImageUtil {

    private Context mContext;

    public static synchronized ImageUtil getInstance(Context context){
        return new ImageUtil(context);
    }

    public ImageUtil(Context context){
        this.mContext = context;
    }

    /**
     * Blur Bitmap for imageView. Credit to Cutta https://github.com/Cutta/Simple-Image-Blur
     * @param smallBitmap
     * @param radius
     * @return
     */

    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(mContext);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    public static boolean isImageExist(String filename, ImagePath basePath){
        File fullPath = new File(basePath.getPath() + File.separator + filename);
        return fullPath.exists();
    }



    public enum ImagePath{

        BASE_IMAGE_PATH("images"),

        IMAGE_PROFILE_PATH("images/images profile"),

        MY_IMAGE_PROFILE_PATH("images/my_images_profile");

        private final String path;
        private ImagePath(String path){
            this.path = path;
        }

        private String getPath(){
            return path;
        }

    }

    public static class ImageSaver{
        private Context mContext;
        private String filename;
        private String directory;
        private final String TAG = getClass().getSimpleName();

        public ImageSaver(Context context){
            this.mContext = context;
        }

        public ImageSaver setFileName(String fileName){
            this.filename = fileName;
            return this;
        }

        public ImageSaver setDirectory(String directory){
            this.directory = directory;
            return this;
        }

        public void save(Bitmap bitmap){
            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(getFile());
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                try {
                    if(fos!=null)
                        fos.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        @NonNull
        private File getFile() {
            File dir = mContext.getDir(directory,Context.MODE_PRIVATE);
            if(!dir.exists() && !dir.mkdirs()){
                Log.e(TAG,"Image dir cannot be created : "+dir);
            }
            return new File(dir,filename);
        }

        public File getFile(String filename, ImagePath basePath){
            File dir = mContext.getDir(basePath.getPath() + File.separator + filename,Context.MODE_PRIVATE);
            return dir;
        }

    }
}
