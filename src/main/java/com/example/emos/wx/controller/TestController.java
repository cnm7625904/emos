package com.example.emos.wx.controller;

import com.example.emos.commn.util.R;
import com.example.emos.config.shiro.JwtUtil;
import com.example.emos.wx.controller.form.TestSayHellowForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * copyright (C), 2022, 运达科技有限公司
 * fileName  TestController
 *
 * @author 王玺权
 * date  2022-01-02 19:10
 * description
 * reviser
 * revisionTime
 */
@RestController
@RequestMapping("/test")
@Api("测试Web接口")
public class TestController {
    @PostMapping("/sayHello")
    @ApiOperation("简单测试")
    public R sayHello(@Valid @RequestBody TestSayHellowForm testSayHellowForm){
      return R.ok().put("message","Hello，"+testSayHellowForm.getName());
    }
}
