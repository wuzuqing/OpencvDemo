package com.example.administrator.opencvdemo.youtu;

import java.io.File;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2018/1/17 21:54
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2018/1/17$
 * @updateDes ${TODO}
 */


public interface OnCompressListener {

    /**
     * Fired when the compression is started, override to handle in your own code
     */
    void onStart();

    /**
     * Fired when a compression returns successfully, override to handle in your own code
     */
    void onSuccess(File file);

    /**
     * Fired when a compression fails to complete, override to handle in your own code
     */
    void onError(Throwable e);
}

