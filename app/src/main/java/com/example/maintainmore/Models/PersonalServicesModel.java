package com.example.maintainmore.Models;

public class PersonalServicesModel {
    String serviceType, name, description,timeRequired, price, iconUrl, backgroundImageUrl;


    public PersonalServicesModel(String serviceType, String name, String description,
                                 String timeRequired, String price, String iconUrl,
                                 String backgroundImageUrl) {
        this.serviceType = serviceType;
        this.name = name;
        this.description = description;
        this.timeRequired = timeRequired;
        this.price = price;
        this.iconUrl = iconUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }


    public String getServiceType() {
        return serviceType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeRequired() {
        return timeRequired;
    }

    public String getPrice() {
        return price;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }
}
