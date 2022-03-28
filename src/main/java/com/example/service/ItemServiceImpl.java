package com.example.service;

import com.example.dto.ItemDTO;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemServiceImpl implements ItemService {

    @Autowired
    SqlSessionFactory sqlFactory;

    @Override
    public int insertItem(ItemDTO item) {
        return sqlFactory.openSession().insert("Item.insertItemOne", item);
    }
    
}
