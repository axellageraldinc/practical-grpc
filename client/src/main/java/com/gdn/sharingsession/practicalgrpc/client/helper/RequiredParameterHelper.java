package com.gdn.sharingsession.practicalgrpc.client.helper;

import com.gdn.sharingsession.practicalgrpc.client.model.master.RequiredParameter;

/**
 * Created by axellageraldinc.a on 12/4/2019.
 */
public interface RequiredParameterHelper {
  RequiredParameter get();

  String getRequestId();

  void set(RequiredParameter requiredParameter);
}
