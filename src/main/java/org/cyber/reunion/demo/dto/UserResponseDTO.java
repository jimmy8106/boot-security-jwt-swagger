package org.cyber.reunion.demo.dto;

import java.util.List;

import org.cyber.reunion.demo.models.AppUserRole;

import io.swagger.annotations.ApiModelProperty;

public class UserResponseDTO {

  @ApiModelProperty(position = 0)
  private Integer id;
  @ApiModelProperty(position = 1)
  private String username;
  @ApiModelProperty(position = 2)
  private String email;
  @ApiModelProperty(position = 3)
  List<AppUserRole> appUserRoles;

}
