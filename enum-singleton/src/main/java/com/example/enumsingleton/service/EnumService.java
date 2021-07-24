package com.example.enumsingleton.service;

public enum EnumService {
    INSTANCE;

    private RegularService regularService;

    public void setRegularService(RegularService regularService) {
        this.regularService = regularService;
    }

    public String doWork() {
        return regularService.doWork();
    }
}
