package de.undermain.pluginguard.service;

import de.undermain.pluginguard.dto.LicenseCreationRequest;
import de.undermain.pluginguard.dto.LicenseValidationRequest;
import de.undermain.pluginguard.dto.LicenseValidationResponse;
import de.undermain.pluginguard.model.License;
import de.undermain.pluginguard.model.LicenseStatus;
import de.undermain.pluginguard.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LicenseService {

    //Dependencies einfügen
    @Autowired
    private LicenseRepository licenseRepository;

    //Lizenz Validation Methode

    @Transactional //Das heisst es ist eine Datenbank Transaktion
    public LicenseValidationResponse validateLicense(LicenseValidationRequest request) {

        //Lizenz in der Datenbank suchen
        Optional<License> licenseOpt = licenseRepository.findByLicenseKey(request.getLicenseKey());

        //Prüfen ob Lizenz in der DB ist
        if(licenseOpt.isEmpty()) {
            return LicenseValidationResponse.failure("Lizenz wurde nicht gefunden..."); //Failure Nachricht ausgeben
        }

        //Lizenz aus dem Opt holen
        License license = licenseOpt.get();

        //Plugin Name prüfen
        if(!license.getPluginName().equals(request.getPluginName())) {
            return LicenseValidationResponse.failure("Lizenz ist nicht gültig für dieses Plugin");
        }

        //Ablaufdatum prüfen
        if(license.isExpired()) {
            license.setStatus(LicenseStatus.EXPIRED); //Status aktualisieren auf EXPIRED
            licenseRepository.save(license); //Neuen Status in der Datenbank speichern
            return LicenseValidationResponse.failure("Lizenz ist abgelaufen"); //Failure Nachricht ausgeben

        }

        //Status prüfen
        if(license.getStatus() != LicenseStatus.ACTIVE) { //Wenn der Lizenz Status nicht Aktiv ist
            return LicenseValidationResponse.failure("Lizenz Status: " + license.getStatus().toString().toLowerCase());
        }

        //Hardware ID prüfen
        if(license.getHwid() != null && !license.getHwid().isEmpty()) {
            if(!license.getHwid().equals(request.getHwid())) {
                //Lizenz ist schon an anderen Server gebunden
                return LicenseValidationResponse.failure("Lizenz ist an einen anderen Server gebunden!");
            }
        } else if(request.getHwid() != null && !request.getHwid().isEmpty()) {
            //Hardware ID ist leer also wird aus der REQUEST die Hardware Id mit der Lizenz in der Datenbank gespeichert
            license.setHwid(request.getHwid());
        }
        
        //Informationen aktualisieren
        license.setServerIp(request.getServerIp());
        license.setLastValidated(LocalDateTime.now());
        licenseRepository.save(license);
        
        //Antwort geben
        return LicenseValidationResponse.success(
                license.getLicenseKey(),
                license.getPluginName(),
                license.getExpiresAt(),
                license.getStatus().toString()
        );
    }
    
    //Lizenz Erstellen Methode
    
    @Transactional
    public License createLicense(LicenseCreationRequest request) {
        
        //Lizenz Schlüssel generieren
        String licenseKey = generateLicenseKey();
        //Lizenz Objekt erstellen
        License license = new License(licenseKey, request.getPluginName(), request.getCustomerEmail());
        //Lizenz Optionale Variablen setzen
        license.setExpiresAt(request.getExpiresAt());
        license.setMaxServers(request.getMaxServers());
        //In der Datenbank speichern
        return licenseRepository.save(license);
    }
    
    //Lizenz Widerruf Methode
    @Transactional
    public boolean revokeLicense(String licenseKey) {
        
        //Lizenz anhand des Lizenz Schlüssels in der Datenbank finden
        Optional<License> licenseOpt = licenseRepository.findByLicenseKey(licenseKey);
        
        if(licenseOpt.isEmpty()) {
            //Lizenz wurde nicht gefunden
            return false;
        }
        
        License license = licenseOpt.get();
        license.setStatus(LicenseStatus.REVOKED); //Status auf REVOKED setzen
        licenseRepository.save(license); //Lizenz speichern
        
        return true;
    }
    
    //Lizenz sperren
    
    @Transactional
    public boolean suspendLicense(String licenseKey) {
        
        //Lizenz anhand des Lizenz Schlüssels in der Datenbank finden
        Optional<License> licenseOpt = licenseRepository.findByLicenseKey(licenseKey);

        if(licenseOpt.isEmpty()) {
            //Lizenz wurde nicht gefunden
            return false;
        }
        
        License license = licenseOpt.get();
        license.setStatus(LicenseStatus.SUSPENDED);
        licenseRepository.save(license); //Lizenz speichern
        
        return true;
    }
    
    //Lizenz Aktivieren
    
    @Transactional
    public boolean activateLicense(String licenseKey) {

        //Lizenz anhand des Lizenz Schlüssels in der Datenbank finden
        Optional<License> licenseOpt = licenseRepository.findByLicenseKey(licenseKey);

        if(licenseOpt.isEmpty()) {
            //Lizenz wurde nicht gefunden
            return false;
        }
        
        License license = licenseOpt.get();
        
        if(license.getStatus() == LicenseStatus.REVOKED) {
            //REVOKED Lizenzen kann man nichtmehr aktivieren
            return false;
        }
        
        license.setStatus(LicenseStatus.ACTIVE);
        licenseRepository.save(license);
        
        return true;
    }
    
    //Getter
    
    //Finde Lizenz mit Key
    public Optional<License> getLicenseByKey(String licenseKey) {
        return licenseRepository.findByLicenseKey(licenseKey);
    }
    
    //Finde alle Lizenzen mit Email
    public List<License> getLicensesByCustomer(String customerEmail) {
        return licenseRepository.findByCustomerEmail(customerEmail);
    }
    
    //Findet alle Lizenzen für Pluginname
    public List<License> getLicensesByPlugin(String pluginName) {
        return licenseRepository.findByPluginName(pluginName);
    }
    
    //Gibt alle Lizenzen
    public List<License> getAllLicenses() {
        return licenseRepository.findAll();
    }
    
    //Lizenz Generator
    
    private String generateLicenseKey() {
        String key;
        do {
            key = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            key = formatLicenseKey(key);
        }while (licenseRepository.existsByLicenseKey(key));
        
        return key;
    }
    
    //Lizenz formatieren in ABCD-ABCD-ABCD-ABCD
    private String formatLicenseKey(String key) {
        StringBuilder formatted = new StringBuilder();
        for(int i = 0; i < key.length(); i++) {
            if(i > 0 && i % 4 == 0) {
                formatted.append("-"); //Nach jedem 4 Zeichen ein -
            }
            formatted.append(key.charAt(i));
        }
        return formatted.toString();
    }
    
    
    
}