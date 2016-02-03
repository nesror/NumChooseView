package cn.yzapp.numchooseviewlib.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    /**
     * 默认样式的吐司
     */
    public static void shortToast(Context context, String msg) {
        context = context.getApplicationContext();
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

}
