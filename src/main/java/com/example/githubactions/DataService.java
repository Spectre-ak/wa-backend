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
        List<Map<String, String>> employees = this.getAll();
        List<Map<String, String>> onProject = new ArrayList<>();

        for(Map employee : employees)
        {
            String productName = employee.get("Product").toString();
            int value = 0;
            if(productName != "")
                value = Integer.parseInt(productName.replaceAll("[^0-9]", ""));
            if(value == id) {
                onProject.add(employee);
            }
        }
        return onProject;
    }

    public Map<String, String> getResourceById(int id) {

        List<Map<String, String>> employees = this.getAll();
        Map<String, String> foundEmployee = new HashMap<>();

        for(Map employee : employees)
        {
            String employeeName = employee.get("Name").toString();
            int value = 0;
            if(employeeName != "")
                value = Integer.parseInt(employeeName.replaceAll("[^0-9]", ""));
            if(value == id) {
                return employee;
            }
        }
        return foundEmployee;
    }

    public ArrayList getProjectsById(int id) throws InformationNotFoundException {
        ArrayList<Object> projectResources = new ArrayList<>();
        Map<String, String>  product = new HashMap();
        for(Map object: this.getAll())
        {
            String productName = object.get("Product").toString();
            int value = 0;
            if(productName != "")
                value = Integer.parseInt(productName.replaceAll("[^0-9]", ""));
            if(value == id)
            {
                List currentResources = this.getProjectResource(id);
                product.put("Product", object.get("Product").toString());
                product.put("Prod Build Location", object.get("Prod Build " + "Location").toString());
                product.put("Prod Start Date", object.get("Prod Start Date").toString());
                product.put("Prod End Date",object.get("Prod End Date").toString());
                projectResources.add(product);
                projectResources.add(currentResources);
                return projectResources;
            } else {
            }
        }
        return projectResources;
    }
}
