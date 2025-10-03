package de.undermain.pluginguard.repository;

import de.undermain.pluginguard.model.License;
import de.undermain.pluginguard.model.LicenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {

    //Finde Lizenz anhand des Keys
    Optional<License> findByLicenseKey(String licenseKey);

    //Finde alle Lizenzen eines Kunden mit der Email
    List<License> findByCustomerEmail(String customerEmail);

    //Finde alle Lizenzen für ein Plugin
    List<License> findByPluginName(String pluginName);

    //Finde alle Lizenzen mit einem bestimmten Status z.B. ACTIVE oder EXPIRED
    List<License> findByStatus(LicenseStatus status);

    //Prüft ob eine Lizenz existiert
    boolean existsByLicenseKey(String licenseKey);




}