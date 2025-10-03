package de.undermain.pluginguard.dto;

public class LicenseValidationRequest {

    //Variablen deklarieren
    private String licenseKey;
    private String serverIp;
    private String hwid;
    private String pluginName;

    //Konstruktoren

    public LicenseValidationRequest() {

    }

    public LicenseValidationRequest(String licenseKey, String serverIp, String hwid, String pluginName) {
        this.licenseKey = licenseKey;
        this.serverIp = serverIp;
        this.hwid = hwid;
        this.pluginName = pluginName;
    }

    //Getter und Setter

    //Getter und Setter Lizenz Key
    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    //Getter und Setter Plugin Name
    public String getPluginName() {
        return pluginName;
    }
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    //Getter und Setter Server IP
    public String getServerIp() {
        return serverIp;
    }
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    //Getter und Setter Hardware ID
    public String getHwid() {
        return hwid;
    }
    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

}