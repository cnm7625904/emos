package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * copyright (C), 2022, 运达科技有限公司
 * fileName  TestSayHellowForm
 *
 * @author 王玺权
 * date  2022-01-02 20:21
 * description
 * reviser
 * revisionTime
 */
@ApiModel
@Data
public class TestSayHellowForm {
//    @NotBlank
//    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,15}$")
    @ApiModelProperty("姓名")
    private String name;
}
