package bane.innovation.a19kworddictionary.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HandlePdf {
	
	Context context;
	private final String fileName="Developers Guide.pdf";
	
	public HandlePdf(Context context) {
		super();
		this.context = context;
	}
	
	public void show()
	{
		try{
		copyReadAssets();
		Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "Pdfs/" + fileName), "application/pdf");
	    context.startActivity(intent);
		}
		catch(Exception ex)
		{
			Toast.makeText(context, "No application to handle pdf file", Toast.LENGTH_LONG).show();
		}
	}

    private void copyReadAssets()
    {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;

        String strDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ File.separator + "Pdfs";
        File fileDir = new File(strDir);
        fileDir.mkdirs();   
        File file = new File(fileDir,fileName);

        try
        {

            in = assetManager.open(fileName);  
            out = new BufferedOutputStream(new FileOutputStream(file)); 
            Log.e("pdf", file.getAbsolutePath());

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }

      
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

}
