package com.example.githubactions.service;

import com.example.githubactions.InformationNotFoundException;
import com.example.githubactions.Recommendation;
import com.example.githubactions.SheetUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

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

    public Set<Object> getAllProjectsBetweenDates(String startDate,
                                                  String endDate) {
        //TODO exception if incorrect format in date String
        Set<Object> projects = new HashSet<>();
        System.out.println("Getting all projects between dates");

        for(Map object: this.getAll().stream().filter(
                e -> filterProjectsBetweenDates(startDate, endDate, e)
        ).collect(Collectors.toList()))
        {
            projects.add(object.get("Product"));
        }

        return projects;
    }

    public List<Map<String, String>> getResourcesByProperty(String property,
                                                            String type) {
        List<Map<String, String>> resources = this.getAll();
        List<Map<String, String>> foundResources = new ArrayList<>();

        for(Map resource: resources) {
            try {
                if (resource.get(property).equals(type)) {
                    foundResources.add(resource);
                }
            } catch (Exception ex) {

            }
        }
        return foundResources;
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
                value = parseInt(productName.replaceAll("[^0-9]", ""));
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
                value = parseInt(employeeName.replaceAll("[^0-9]", ""));
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
                value = parseInt(vendorName.replaceAll("[^0-9]", ""));
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
                value = parseInt(productName.replaceAll("[^0-9]", ""));
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
        //TODO exception for incorrect role
        Map employee = extractEngineers().get(0);

        for(String role: recommendation.getRole())
        {
            results.put(role, extractEngineers().stream()
                    .filter(resource -> filterByLocation(resource, recommendation.getLocation()))
                    .filter(resource -> filterByRole(resource, role))
                    .filter(resource -> filterByAvailability(resource, recommendation.getDate()))
                    .collect(Collectors.toList()));
        }

        return results;
    }

    public List<Map<String, String>> extractEngineers() {
        List<Map<String, String>> allResources = this.getAll();
        List<Map<String, String>> engineers = new ArrayList<>();
        engineers =
                allResources.stream().filter(e -> !e.get("Role Level").isEmpty())
                        .collect(Collectors.toList());
        return engineers;
    }

    public boolean filterByRole(Map<String,
            String> resource, String role) {
        return resource.get("Role Level").equals(role);
    }

    /**
     * @param resource - resource from which date will be extracted from
     * @param location- location input from Post Request
     * @return boolean results for whether a resource is within a location
     */
    public boolean filterByLocation(Map<String, String> resource,
                                    String location) {
        return resource.get("Prod Build Location").equals(location);
    }

    /**
     * @param resource - resource from which date will be extracted from
     * @param date - input from Post Request
     * @return boolean results for whether a date is before a specific date
     */
    public boolean filterByAvailability(Map<String, String> resource,
                                        String date) {

        LocalDate resourceProjectEndDate = formattedDate(resource.get("resource product end date"));
        LocalDate recommendationDate = formattedDate(date);

        return resourceProjectEndDate.isBefore(recommendationDate);
    }

    /**
     * Checks time between start and end as well as conditions including
     * a test that starts / ends on the query string
     */
    public boolean filterProjectsBetweenDates(String startDate,
                                              String endDate, Map project)
    {
        LocalDate projectStartDate =
                formattedDate((String) project.get("Prod Start Date"));
        LocalDate projectEndDate =
                formattedDate((String) project.get("Prod End Date"));
        LocalDate queryStartDate =
                formattedDate(startDate);
        LocalDate queryEndDate =
                formattedDate(endDate);

        return projectStartDate.isAfter(queryStartDate) && projectEndDate.isBefore(queryEndDate) ||
                projectStartDate.isEqual(queryStartDate) && projectEndDate.isEqual(queryEndDate) ||
                projectStartDate.isAfter(queryStartDate) && projectEndDate.isEqual(queryEndDate) ||
                projectStartDate.isEqual(queryStartDate) && projectEndDate.isBefore(queryEndDate);
    }

    /*
    Spreadsheet Date Format: 12/31/21 month/day/year
    Need to convert to LocalDate for comparison
    JS Input Date Format: "date":"2021-05-13"
    Local date format 21/12/31 year/month/dayOfMonth
    */
    public LocalDate formattedDate(String date) {
        String[] dateArr;
        LocalDate dateObject = LocalDate.of(2021,12,3);
        if(date.indexOf('-') != -1)
        {
            dateArr = date.split("-");
            dateObject = LocalDate.of(
                    Integer.parseInt(dateArr[0]),
                    Integer.parseInt(dateArr[1]),
                    Integer.parseInt(dateArr[2]));
            return dateObject;
        } else if(date.indexOf('/') != -1) {

            dateArr = date.split("/");
            /* concat the beginning of the year */
            dateArr[2] = "20" + dateArr[2];
            dateObject = LocalDate.of(
                    Integer.parseInt(dateArr[2]),
                    Integer.parseInt(dateArr[0]),
                    Integer.parseInt(dateArr[1])
            );
            return dateObject;
        }
        return dateObject;
    }

}
