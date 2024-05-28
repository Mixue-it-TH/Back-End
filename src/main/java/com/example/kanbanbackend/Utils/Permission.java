package com.example.kanbanbackend.Utils;

import org.hibernate.mapping.Array;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Permission {
    private static List<Integer> exceptStatus = new ArrayList<>(Arrays.asList(1,7));

    public boolean canEditOrDelete(Integer id){
        return !exceptStatus.contains(id);
    }
}
