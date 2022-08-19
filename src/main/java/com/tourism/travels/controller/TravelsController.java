package com.tourism.travels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TravelsController {

    @GetMapping("/packages")
    public String getPackages(String string) {

        return string;
    }

    @GetMapping("/totalPackages")
    public Integer getTotalPackages() {

        return 20;
    }

}
