package com.gdn.sharingsession.practicalgrpc.server.model.master;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by axellageraldinc.a on 1/29/2020.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequiredParameter {
  private String requestId;
}
