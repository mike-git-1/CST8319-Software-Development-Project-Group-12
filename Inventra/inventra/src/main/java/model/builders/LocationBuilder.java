package model.builders;
import model.beans.Location;

public class LocationBuilder {
    private final Location location;

    public LocationBuilder() {
        location = new Location();
    }

    public LocationBuilder withLocationId(int id) {
        location.setLocationId(id);
        return this;
    }

    public LocationBuilder withCompanyId(int companyId) {
        location.setCompanyId(companyId);
        return this;
    }

    public LocationBuilder withName(String name) {
        location.setName(name);
        return this;
    }

    public LocationBuilder withAddress1(String address1) {
        location.setAddress1(address1);
        return this;
    }

    public LocationBuilder withAddress2(String address2) {
        location.setAddress2(address2);
        return this;
    }

    public Location build() {
        return location;
    }
}
