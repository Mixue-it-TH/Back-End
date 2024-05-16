package com.example.kanbanbackend.Utils;

import org.hibernate.mapping.Array;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Permission {
    private static List<Integer> vipStatusId = new ArrayList<>(Arrays.asList(1,4));

    public boolean canEditOrDelete(Integer id){
        return vipStatusId.contains(id);
    }
}
