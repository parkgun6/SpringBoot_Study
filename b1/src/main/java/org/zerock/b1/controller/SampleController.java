package org.zerock.b1.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class SampleController {

    @GetMapping("/doA")
    public String[] doA(){
        log.info("doA333333...........................");
        log.info("doA222222...........................");
        log.info("doA111111...........................");
        return new String[]{"AAA","BBB"};
    }
}
