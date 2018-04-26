package com.example.askarbayev1.cargotrans;

class Order {
    int id;
    int user_id;
    int status;
    String location;
    String destination;
    String description;
    String height;
    String width;
    String length;
    String weight;
    double budget;
    String pickup_date;
    String dropoff_date;
    String loc_latitude;
    String loc_longitude;
    String dest_latitude;
    String dest_longitude;

    public Order(int id, int user_id, int status, String location, String destination, String description, String height, String width, String length, String weight, double budget, String pickup_date, String dropoff_date, String loc_latitude, String loc_longitude, String dest_latitude, String dest_longitude) {
        this.id = id;
        this.user_id = user_id;
        this.status = status;
        this.location = location;
        this.destination = destination;
        this.description = description;
        this.height = height;
        this.width = width;
        this.length = length;
        this.weight = weight;
        this.budget = budget;
        this.pickup_date = pickup_date;
        this.dropoff_date = dropoff_date;
        this.loc_latitude = loc_latitude;
        this.loc_longitude = loc_longitude;
        this.dest_latitude = dest_latitude;
        this.dest_longitude = dest_longitude;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public String getLength() {
        return length;
    }

    public String getWeight() {
        return weight;
    }

    public double getBudget() {
        return budget;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public String getDropoff_date() {
        return dropoff_date;
    }

    public String getLoc_latitude() {
        return loc_latitude;
    }

    public String getLoc_longitude() {
        return loc_longitude;
    }

    public String getDest_latitude() {
        return dest_latitude;
    }

    public String getDest_longitude() {
        return dest_longitude;
    }


}
