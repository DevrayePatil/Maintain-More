package com.example.maintainmore.Modals;

public class RepairApplianceModal {
    String serviceType, name, description,timeRequired, price, iconUrl, backgroundImageUrl;

    public RepairApplianceModal(String serviceType, String name, String description,
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

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(String timeRequired) {
        this.timeRequired = timeRequired;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
