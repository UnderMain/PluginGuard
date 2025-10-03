package de.undermain.pluginguard.dto;

import java.time.LocalDateTime;

public class LicenseCreationRequest {

    //Variablen deklarieren
    private String pluginName;
    private String customerEmail;
    private LocalDateTime expiresAt;
    private Integer maxServers = 1;

    //Konstruktoren
    public LicenseCreationRequest() {}

    public LicenseCreationRequest(String pluginName, String customerEmail) {
        this.pluginName = pluginName;
        this.customerEmail = customerEmail;
    }

    //Getter und Setter

    //Getter und Setter für Plugin Name
    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    //Getter und Setter für Email
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    //Getter und Setter für Ablauf
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    //Getter und Setter für maximale Server
    public Integer getMaxServers() {
        return maxServers;
    }

    public void setMaxServers(Integer maxServers) {
        this.maxServers = maxServers;
    }

}