package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressService {
    void save(AddressBook addressBook);

    List<AddressBook> getAllByUserId();

    void update(AddressBook addressBook);

    void deleteById(Long id);

    AddressBook getById(Long id);

    void setDefault(Long id);

    AddressBook getDefaultByUserId();
}
