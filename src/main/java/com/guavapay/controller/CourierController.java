package com.guavapay.controller;

import com.guavapay.model.dto.CourierFilterDto;
import com.guavapay.model.request.CourierFilterRequest;
import com.guavapay.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "courier")
public class CourierController {

    private final CourierService courierService;

    @PostMapping(value = "/filter", consumes = {"application/json"}, produces = {"application/json"})
    public List<CourierFilterDto> courierFilter(@Valid @RequestBody CourierFilterRequest courierFilterRequest) {
        return courierService.courierFilter(courierFilterRequest);
    }
}
