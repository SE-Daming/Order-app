package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressMapper;
import com.sky.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressMapper addressMapper;

    /**
     * 新增地址
     */
    @Override
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressMapper.save(addressBook);
    }

    /**
     * 查询当前用户的所有地址
     */
    @Override
    public List<AddressBook> getAllByUserId() {
        return addressMapper.getAllByUserId(BaseContext.getCurrentId());
    }

    /**
     * 修改地址
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressMapper.update(addressBook);
    }

    /**
     * 根据id删除地址
     */
    @Override
    public void deleteById(Long id) {
        addressMapper.deleteById(id);
    }

    /**
     * 根据id查询地址
     */
    @Override
    public AddressBook getById(Long id) {
        return addressMapper.getById(id);
    }

    /**
     * 把id处记录设为默认地址
     * 把相同userId的default设为0
     */
    @Override
    public void setDefault(Long id) {

        //把相同userId的default设为0
        addressMapper.setNotDefault(BaseContext.getCurrentId());

        //把id处记录设为默认地址
        addressMapper.setDefault(id);
    }

    /**
     * 查询默认地址
     */
    @Override
    public AddressBook getDefaultByUserId() {
        Long userId=BaseContext.getCurrentId();
        return addressMapper.getDefaultAddressByUserId(userId);
    }
}
