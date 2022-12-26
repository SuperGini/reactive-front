package com.gini.gateway;

import com.gini.dto.request.AddressRequest;
import com.gini.dto.request.BasketItemRequest;
import com.gini.dto.request.CustomerRequest;
import com.gini.dto.response.CustomerResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface ReactiveCoreClient {

    @PostExchange("/customer")
    Flux<CustomerResponse> saveCustomer(@RequestBody CustomerRequest customerRequest);

    @GetExchange("/customer/{username}")
    Flux<CustomerResponse> getCustomer(@PathVariable String username);

    @GetExchange("/customers")
    Flux<CustomerResponse> getAllCustomers();

    @PutExchange("/customer/{username}")
    Flux<CustomerResponse> updateCustomerAddress(@RequestBody AddressRequest addressRequest,
                                                 @PathVariable String username);

    @DeleteExchange("/customer/{username}")
    Flux<Void> deleteCustomerByUsername(@PathVariable String username);

    @PutExchange("/basketItems/{username}")
    Flux<CustomerResponse> updateCustomerWithBasketItems(@RequestBody Set<BasketItemRequest> basketItemRequests,
                                                                @PathVariable String username);



}
