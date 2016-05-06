package com.tongdao.sdk.permissions;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by kinjal.patel on 28/04/16.
 */
public interface IPermissionResponse<T> extends Serializable {
    <T> T permissionGranted() throws Exception;
    <T> T permissionDenied();
}
