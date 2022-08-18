package com.tourism.travels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TravelsController {

    public String getPackages() {

        return "Travels";
    }

}
