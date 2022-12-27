package osp.leobert.android.reportprinter.spi;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Classname: IModuleInitializer </p>
 * Description: the entrance of module for initializer </p>
 * Created by leobert on 2022/12/27.
 */
public interface IModuleInitializer {
    void initialize(ProcessingEnvironment env);
}
