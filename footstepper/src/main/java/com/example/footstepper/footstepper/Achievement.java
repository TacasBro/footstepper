package com.example.footstepper.footstepper;

public class Achievement {

    private String name;
    private String description;
    private int minimalTotalSteps;

    public Achievement(String name, String description,int minimalTotalSteps){
        setName(name);
        setDescription(description);
        setMinimalTotalSteps(minimalTotalSteps);
    }
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getMinimalTotalSteps() {
        return minimalTotalSteps;
    }

    private void setMinimalTotalSteps(int minimalTotalSteps) {
        this.minimalTotalSteps = minimalTotalSteps;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

}
