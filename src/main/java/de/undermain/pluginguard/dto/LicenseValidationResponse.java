package de.undermain.pluginguard.dto;

import java.time.LocalDateTime;

public class LicenseValidationResponse{

    //Variablen die der Server in der Response zurückgibt
    private boolean valid;
    private String message;
    private String licenseKey;
    private String pluginName;
    private LocalDateTime expiresAt;
    private String status;

    //Konstruktoren
    public LicenseValidationResponse() {}

    public LicenseValidationResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    //Methoden zum erstellen der Antwort
    public static LicenseValidationResponse success(String licenseKey, String pluginName, LocalDateTime expiresAt, String status) {

        LicenseValidationResponse response = new LicenseValidationResponse(true, "Lizenz ist gültig");
        response.setLicenseKey(licenseKey);
        response.setPluginName(pluginName);
        response.setExpiresAt(expiresAt);
        response.setStatus(status);
        return response;

    }

    //Fehler Antwort
    public static LicenseValidationResponse failure(String message) {
        return new LicenseValidationResponse(false, message);
    }

    //Getter und Setter

    //Getter und Setter für Gültigkeit
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    //Getter und Setter für message
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //Getter und Setter für Lizenz Key
    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    //Getter und Setter für Plugin Namen
    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    //Getter und Setter für ablauf Zeit
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    //Getter und Setter für status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}