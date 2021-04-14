package org.zerock.b4.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.b4.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sample")
@Log4j2
public class SampleController {

    @GetMapping("/ex1")
    public void ex1(){
        log.info("ex1.....................");
    }

    @GetMapping("/ex2")
    public void ex2(Model model){
        log.info("ex2.....................");

        //for loop 대신에 사용
        List<SampleDTO> list =
                IntStream.rangeClosed(1,100).asLongStream().mapToObj(i -> {
            return SampleDTO.builder()
                    .sno(i)
                    .first("first"+i)
                    .last("last"+i)
                    .regTime(LocalDateTime.now())
                    .build();
        }).collect(Collectors.toList());

        model.addAttribute("list",list);
    }

    @GetMapping("/testLayout")
    public void testLayout(){
        log.info("test Layout....................");
    }
}
