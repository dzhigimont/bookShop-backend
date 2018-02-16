package com.zhigimont.bookstore.repository;

import com.zhigimont.bookstore.domain.ShippingAddress;
import org.springframework.data.repository.CrudRepository;

public interface ShippingAddressRepository extends CrudRepository<ShippingAddress, Long>{
}
