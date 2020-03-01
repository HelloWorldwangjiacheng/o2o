package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author w1586
 * @Date 2020/3/1 19:08
 * @Cersion 1.0
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController {

    @GetMapping("/index")
    public String index(){
        return "frontend/index";
    }
}
