package com.example.maintainmore.Models;

public class ServiceCanceledModal {

    String bookingID, userID, assignedTechnician,
            serviceName, serviceDescription,
            serviceType, serviceIconUrl, visitingDate, visitingTime,
            serviceRequiredTime, bookingDate, bookingTime, servicePrice,
            servicesForMale, servicesForFemale, totalServices, totalServicesPrice,
            cancellationTill, serviceStatus;

    public ServiceCanceledModal(String bookingID, String userID, String assignedTechnician, String serviceName, String serviceDescription, String serviceType, String serviceIconUrl, String visitingDate, String visitingTime, String serviceRequiredTime, String bookingDate, String bookingTime, String servicePrice, String servicesForMale, String servicesForFemale, String totalServices, String totalServicesPrice, String cancellationTill, String serviceStatus) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.assignedTechnician = assignedTechnician;
        this.serviceName = serviceName;
        this.serviceDescription = serviceDescription;
        this.serviceType = serviceType;
        this.serviceIconUrl = serviceIconUrl;
        this.visitingDate = visitingDate;
        this.visitingTime = visitingTime;
        this.serviceRequiredTime = serviceRequiredTime;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.servicePrice = servicePrice;
        this.servicesForMale = servicesForMale;
        this.servicesForFemale = servicesForFemale;
        this.totalServices = totalServices;
        this.totalServicesPrice = totalServicesPrice;
        this.cancellationTill = cancellationTill;
        this.serviceStatus = serviceStatus;
    }

    public String getBookingID() {
        return bookingID;
    }

    public String getUserID() {
        return userID;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceIconUrl() {
        return serviceIconUrl;
    }

    public String getVisitingDate() {
        return visitingDate;
    }

    public String getVisitingTime() {
        return visitingTime;
    }

    public String getServiceRequiredTime() {
        return serviceRequiredTime;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public String getServicesForMale() {
        return servicesForMale;
    }

    public String getServicesForFemale() {
        return servicesForFemale;
    }

    public String getTotalServices() {
        return totalServices;
    }

    public String getTotalServicesPrice() {
        return totalServicesPrice;
    }

    public String getCancellationTill() {
        return cancellationTill;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }
}
