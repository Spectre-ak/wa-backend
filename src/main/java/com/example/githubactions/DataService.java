package com.example.githubactions;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public Set<String> getVendors() {

        List<Map<String, String>> resources = this.getAll();
        List<Map<String, String>> vendors = new ArrayList<>();
        Set<String> foundVendors = new HashSet<>();

        for(Map vendor : resources)
        {
            String vendorName = vendor.get("Vendor").toString();
            if(!vendorName.isEmpty() && !vendorName.equals(""))
                foundVendors.add(vendorName);
        }

        return foundVendors;

    }

    public List<Map<String, String>> getVendorsById(int id) {

        List<Map<String, String>> resources = this.getAll();
        List<Map<String, String>> vendors = new ArrayList<>();

        for(Map vendor : resources)
        {
            int value = 0;
            String vendorName = vendor.get("Vendor").toString();
            if(!vendorName.isEmpty() ) {
                value = Integer.parseInt(vendorName.replaceAll("[^0-9]", ""));
                if(id == value)
                vendors.add(vendor);
            }
        }
        return vendors;
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

    public Map<String, List<Map> > getRecommendations(Recommendation recommendation) {
        Map results = new HashMap();
        extractEngineers();
        //TODO exception for incorrect role

        for(String role: recommendation.getRole())
        {
            System.out.println(role);
            results.put(role,filterResourcesByRole(extractEngineers(), role));
        }

        results.entrySet().forEach(entry -> {
            System.out.println(entry);
        });

        //filter by location
        //format date on engineer object
        //check against provided date

        return null;
    }

    public List<Map<String, String>> extractEngineers() {
        List<Map<String, String>> allResources = this.getAll();
        List<Map<String, String>> engineers = new ArrayList<>();
        engineers =
                allResources.stream().filter(e -> !e.get("Role Level").isEmpty())
                        .collect(Collectors.toList());
        return engineers;
    }

    public List<Map<String, String>> filterResourcesByRole(List<Map<String,
            String>> resources, String role) {
        return resources.stream().filter(e -> e.get("Role Level").equals(role))
                .collect(Collectors.toList());
    }
}
