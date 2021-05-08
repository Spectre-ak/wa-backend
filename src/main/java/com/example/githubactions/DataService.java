package com.example.githubactions;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataService {

    private SheetUtils sheetUtils;

    public DataService(SheetUtils sheetUtils) {
        this.sheetUtils = sheetUtils;
    }

    public List<Map<String, String>> getAll() {
        return sheetUtils.mapDataSet(sheetUtils.retrieveDataSet());
    }

    public Set<Object> getAllProjects(){
        Set<Object> products = new HashSet<>();
        for(Map object: this.getAll())
        {
            products.add(object.get("Product"));
        }
        return products;
    }

    public List<Map<String, String>> getProjectResource(int id) {
        //obtain resources per project
        return null;
    }

    public Map<String, String> getProjectsById(int id) throws InformationNotFoundException {
        Map<String, String>  product = new HashMap();
        for(Map object: this.getAll())
        {
            String productName = object.get("Product").toString();
            int value = 0;
            if(productName != "")
                value = Integer.parseInt(productName.replaceAll("[^0-9]", ""));
            if(value == id)
            {
                product.put("Product", object.get("Product").toString());
                product.put("Prod Build Location", object.get("Prod Build " + "Location").toString());
                product.put("Prod Start Date", object.get("Prod Start Date").toString());
                product.put("Prod End Date",object.get("Prod End Date").toString());

                return product;
            } else {
            }
        }
        return product;
    }
}
