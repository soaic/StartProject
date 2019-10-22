package com.soaic.libcommon.network.listener;

import com.soaic.libcommon.network.response.BaseResponse;

public interface ErrorHandlerListener {

    <T extends BaseResponse> boolean handleRsp(T t);

    Throwable getError();

}
