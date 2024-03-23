package org.example.workerservice.service;

import org.example.workerservice.dto.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(name = "localhost:8080")
public interface ApiService {
    @RequestMapping(value = "/client-service/calls", method = RequestMethod.GET)
    List<ClientResponse> getAllClientsByNumberAndCallDateBetween(@RequestParam("number") String number,
                                                                 @RequestParam("startDate") Date startDate,
                                                                 @RequestParam("endDate") Date endDate);
}