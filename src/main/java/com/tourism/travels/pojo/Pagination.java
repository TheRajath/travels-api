package com.tourism.travels.pojo;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Pagination {

    @Min(0)
    private int pageNumber = 0;

    @Min(1)
    private int pageSize = 25;

    private long totalReturnCount;

    private int totalRowCount;

}
