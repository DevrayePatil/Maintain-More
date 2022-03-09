package com.example.maintainmore.Models;

public class ServiceBookingModels {
    String bookingID, userID, assignedTechnician,
            serviceName, serviceDescription,
            serviceType, serviceIconUrl, visitingDate, visitingTime,
            serviceRequiredTime, bookingDate, bookingTime, servicePrice,
            servicesForMale, servicesForFemale, totalServices, totalServicesPrice,
            cancellationTill, serviceStatus;

    public ServiceBookingModels(
            String bookingID, String userID, String assignedTechnician,
            String serviceName, String serviceDescription,
            String serviceType, String serviceIconUrl, String visitingDate,String visitingTime,
            String serviceRequiredTime, String bookingDate, String bookingTime, String servicePrice,
            String servicesForMale, String servicesForFemale, String totalServices, String totalPrice,
            String cancellationTill, String serviceStatus
    ) {
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
        this.totalServicesPrice = totalPrice;
        this.cancellationTill = cancellationTill;
        this.serviceStatus = serviceStatus;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(String assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceIconUrl() {
        return serviceIconUrl;
    }

    public void setServiceIconUrl(String serviceIconUrl) {
        this.serviceIconUrl = serviceIconUrl;
    }

    public String getVisitingDate() {
        return visitingDate;
    }

    public void setVisitingDate(String visitingDate) {
        this.visitingDate = visitingDate;
    }

    public String getVisitingTime() {
        return visitingTime;
    }

    public void setVisitingTime(String visitingTime) {
        this.visitingTime = visitingTime;
    }

    public String getServiceRequiredTime() {
        return serviceRequiredTime;
    }

    public void setServiceRequiredTime(String serviceRequiredTime) {
        this.serviceRequiredTime = serviceRequiredTime;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServicesForMale() {
        return servicesForMale;
    }

    public void setServicesForMale(String servicesForMale) {
        this.servicesForMale = servicesForMale;
    }

    public String getServicesForFemale() {
        return servicesForFemale;
    }

    public void setServicesForFemale(String servicesForFemale) {
        this.servicesForFemale = servicesForFemale;
    }

    public String getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(String totalServices) {
        this.totalServices = totalServices;
    }

    public String getTotalServicesPrice() {
        return totalServicesPrice;
    }

    public void setTotalServicesPrice(String totalServicesPrice) {
        this.totalServicesPrice = totalServicesPrice;
    }

    public String getCancellationTill() {
        return cancellationTill;
    }

    public void setCancellationTill(String cancellationTill) {
        this.cancellationTill = cancellationTill;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
