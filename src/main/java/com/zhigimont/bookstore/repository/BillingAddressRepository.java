package com.zhigimont.bookstore.repository;

import com.zhigimont.bookstore.domain.BillingAddress;
import org.springframework.data.repository.CrudRepository;

public interface BillingAddressRepository extends CrudRepository<BillingAddress, Long>{
}
