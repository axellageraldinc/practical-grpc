package com.gdn.sharingsession.practicalgrpc.client.model.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by axellageraldinc.a on 1/30/2020.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBookRequest {
  @NotEmpty
  private List<String> ids;
}
