package org.unibl.etf.models.enums;

public enum Difficulty {
    BEGINNER("Beginner"),INTERMEDIATE("Intermediate"),EXPERT("Expert");
    private final String status;

    Difficulty(String status) {
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

    public static Difficulty getByStatus(String status)
    {
        for(var el:Difficulty.values())
            if(el.status.equals(status))
                return el;
        throw new IllegalArgumentException();
    }
}
