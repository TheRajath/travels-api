package com.tourism.travels.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/test")
class TestController {

    private final Pojo mock;

    TestController(Pojo mock) {
        this.mock = mock;
    }

    @PostMapping
    public Object post(@RequestBody @Valid Pojo pojo) {

        mock.foo();

        return null;
    }

}

@Getter
@Setter
@Component
@NoArgsConstructor
class Pojo {

    @NotEmpty
    private String foo;

    private Status status;

    public Object foo() { return null; }

}

enum Status {

    OPEN
}
