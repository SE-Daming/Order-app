package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "地址管理")
@Slf4j
public class AddressController {

    @Autowired
    AddressService addressService;

    @GetMapping("list")
    @ApiOperation(value = "展示所有地址")
    public Result<List<AddressBook>>list(){
        List<AddressBook> list=addressService.getAllByUserId();
        return Result.success(list);
    }

    @PostMapping()
    @ApiOperation(value = "新增地址")
    public Result add(@RequestBody AddressBook addressBook){
        addressService.save(addressBook);
        log.info("地址信息：{}",addressBook);
        return Result.success();
    }

    @PutMapping
    @ApiOperation(value = "根据id修改地址")
    public Result modifyById(@RequestBody AddressBook addressBook){
        addressService.update(addressBook);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation(value = "根据id删除地址")
    public Result deleteById(Long id){
        addressService.deleteById(id);
        return Result.success();
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id){
        AddressBook addressBook=addressService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping("default")
    @ApiOperation(value = "设为默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook){
        addressService.setDefault(addressBook.getId());
        return Result.success();
    }

    /**
     * 查询默认地址
     * 提交订单时调用
     * @return
     */
    @GetMapping("default")
    @ApiOperation(value = "查询默认地址")
    public Result<AddressBook>getDefault(){
        AddressBook addressBook=addressService.getDefaultByUserId();
        if(addressBook==null){
            return Result.error("该用户未设定默认地址");
        }
        return Result.success(addressBook);
    }
}
