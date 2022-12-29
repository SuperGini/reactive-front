package com.gini.service;

import com.gini.dto.request.AddressRequest;
import com.gini.dto.request.BasketItemRequest;
import com.gini.dto.request.CustomerRequest;
import com.gini.dto.response.CustomerResponse;
import com.gini.gateway.ReactiveCoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final ReactiveCoreClient reactiveCoreClient;

    public Mono<CustomerResponse> saveCustomer(CustomerRequest customerRequest){
            return reactiveCoreClient.saveCustomer(customerRequest)
                    .next()
                    .log();
    }

    public Mono<CustomerResponse> getCustomer(String username){
        return  reactiveCoreClient.getCustomer(username)
                .next()
                .log();
    }

    public Flux<CustomerResponse> getAllCustomers(){
        return reactiveCoreClient.getAllCustomers()
                .log();
    }

    public Mono<CustomerResponse> updateCustomerAddress(AddressRequest addressRequest, String username){
        return reactiveCoreClient.updateCustomerAddress(addressRequest, username)
                .next()
                .log();
    }

    public Mono<Void> deleteCustomerByUsername (String username){
        return  reactiveCoreClient.deleteCustomerByUsername(username)
                .next()
                .log();
    }

    public Mono<CustomerResponse> updateCustomerWithBasketItems (Set<BasketItemRequest> basketItemRequests,
                                                                 String username){
        return reactiveCoreClient.updateCustomerWithBasketItems(basketItemRequests, username)
                .next()
                .log();

    }







}
