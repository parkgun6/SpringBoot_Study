package org.zerock.mreview.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Data
public class PageRequestDTO {


    private Integer page = 1;
    private Integer size = 10;

    private String type;
    private String keyword;

    public void PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){

        return PageRequest.of(page -1,size, sort);
    }
}
