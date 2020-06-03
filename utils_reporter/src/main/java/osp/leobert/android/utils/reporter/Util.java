package osp.leobert.android.utils.reporter;

import androidx.annotation.StringDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Package:</b> osp.leobert.android.utils.reporter </p>
 * <p><b>Project:</b> MotorFans </p>
 * <p><b>Classname:</b> Util </p>
 *
 * Created by leobert on 2019/2/13.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Util {
    @UtilGroup String group();

    String usage();

    @StringDef({
            UtilGroup.KeyBoard, UtilGroup.MD5,
    })
    @interface UtilGroup {
        String KeyBoard = "KeyBoardUtils(类名：KeyboardUtil)";
        String MD5 = "Md5Utils(类名：MD5)";
    }
}
