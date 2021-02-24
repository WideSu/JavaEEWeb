package org.hqu.vibsignal_analysis.mapper.entity;


public class Authorization {
    private Experiment experiment;

    private User user;

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
