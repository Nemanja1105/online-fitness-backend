package org.unibl.etf.models.enums;

public enum Location {
    ONLINE("Online"),GYM("Gym"),PARK("Park");
    private final String status;

    Location(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString()
    {
        return this.status;
    }

    public static Location getByStatus(String status)
    {
        for(var el:Location.values())
            if(el.status.equals(status))
                return el;
        throw new IllegalArgumentException();
    }
}
