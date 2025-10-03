package de.undermain.pluginguard.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity //Das ist die Datenbank Tabelle
@Table(name = "licenses") //Die Tabelle heißt licenses also Lizenzen

public class License {

    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Lizenz Infos
    @Column(unique=true, nullable = false)
    private String licenseKey;

    @Column(nullable = false)
    private String pluginName;

    @Column(nullable = false)
    private String customerEmail;

    //Server Infos

    //Server IP zum eindeutigen festelegen der Lizenz
    @Column
    private String serverIp;

    // HardwareID zum eindeutigen festelegen der Lizenz
    @Column
    private String hwid;

    //Status

    @Enumerated(EnumType.STRING) // Speichert den Enum als String nicht als Int
    @Column(nullable = false)
    private LicenseStatus status;

    //Zeitstempel

    @Column(nullable = false)
    private LocalDateTime createdAt; //Zeitstempel wann die Lizenz erstellt wurde

    @Column
    private LocalDateTime expiresAt; //Wann die Lizenz abläuft

    @Column
    private LocalDateTime lastValidated; //Zeitstempel wann das Plugin das letzte mal die Lizenz gecheckt hat

    //Plugin Limits quasi wie viele Server eine Lizenz benutzen können

    @Column
    private Integer maxServer = 1; //Maximal 1 Server

    @Column
    private Integer currentServerCount = 0; //Wie viele Server gerade die Lizenz benutzen

    //Konstruktoren

    public License() {
        this.createdAt = LocalDateTime.now(); //Zeitstempel der Erstellung
        this.status = LicenseStatus.ACTIVE; // Neu erstellte Lizenzen starten mit ACTIVE
    }

    public License(String licenseKey, String pluginName, String customerEmail) {
        this(); //Zuerst den leeren Konstruktor aufrufen
        this.licenseKey = licenseKey;
        this.pluginName = pluginName;
        this.customerEmail = customerEmail;
    }

    //Getter und Setter damit andere Klassen darauf zugreifen können


    //Getter und Setter ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    //Getter und Setter Email

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    //Getter und Setter Server IP

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    //Getter und Setter HardwareID

    public String getHwid() {
        return hwid;
    }

    public void setHwid(String hwid) {
        this.hwid = hwid;
    }

    //Getter und Setter Lizenz Status

    public LicenseStatus getStatus() {
        return status;
    }

    public void setStatus(LicenseStatus status) {
        this.status = status;
    }

    //Getter und Setter Zeitstempel createdAt

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    //Getter und Setter Zeitstempel Ablaufzeit

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    //Getter und Setter Zeitstempel letzze Valkidierung

    public LocalDateTime getLastValidated() {
        return lastValidated;
    }

    public void setLastValidated(LocalDateTime lastValidated) {
        this.lastValidated = lastValidated;
    }

    //Getter und Setter Maximale Server

    public Integer getMaxServers() {
        return maxServer;
    }

    public void setMaxServers(Integer maxServer) {
        this.maxServer = maxServer;
    }

    //Getter und Setter Akutelle Server Anzahl

    public Integer getCurrentServerCount() {
        return currentServerCount;
    }

    public void setCurrentServerCount(Integer currentServerCount) {
        this.currentServerCount = currentServerCount;
    }

    //Methoden zum checken und gültigkeit checken

    //Abgelaufen
    public boolean isExpired() {
        //Wenn expiresAt null ist, dann läuft die Lizenz nie ab
        //Wenn expiresAt existiert, checkt das ob Jetzt nach dem Ablaufdatum ist
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    //Gültig
    public boolean isValid() {
        return status == LicenseStatus.ACTIVE && !isExpired();
    }





}