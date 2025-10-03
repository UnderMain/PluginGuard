package de.undermain.pluginguard.controller;

import de.undermain.pluginguard.dto.LicenseCreationRequest;
import de.undermain.pluginguard.dto.LicenseValidationRequest;
import de.undermain.pluginguard.dto.LicenseValidationResponse;
import de.undermain.pluginguard.model.License;
import de.undermain.pluginguard.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController //REST API CONTROLLER
@RequestMapping("/api/v1/licenses") //Alle Endpoints starten mit diesem Pfad

public class LicenseController {

    //Basis API Url ist http://undermain.de/api/v1/licenses

    @Autowired
    private LicenseService licenseService;

    //Endpoint 1 Lizenz Validieren

    //URL http://undermain.de/api/v1/licenses/validate
    //wird z.B. vom Minecraft Plugin aufgerufen um eine Lizenz zu validieren

    @PostMapping("/validate") //POST-Request
    public ResponseEntity<LicenseValidationResponse> validateLicense(@RequestBody LicenseValidationRequest request) {
        //Service aufrufen
        LicenseValidationResponse response = licenseService.validateLicense(request);
        //HTTP-Status Code je nach Ergebnis zurückgeben
        if(response.isValid()) {
            return ResponseEntity.ok(response); // HTTP Code 200 für OK
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); //HTTP Code 403 für verboten
        }
    }

    //Endpoint 2 Neue Lizenz erstellen

    //URL http://undermain.de/api/v1/licenses
    //wird vom Admin ausgeführt um neue Lizenzen zu erstellen

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody LicenseCreationRequest request) {

        //Service ruft creation Methode auf
        License license = licenseService.createLicense(request);
        //HTTP Code 201 Created zurückgeben mit der erstellten Lizenz
        return ResponseEntity.status(HttpStatus.CREATED).body(license);//HTTP Code 201 für erfolgreich erstellt
    }

    //Endpoint 3 Eine Lizenz abrufen

    //URL GET http://undermain.de/api/v1/licenses/ABCD-ABCD-ABCD-ABCD
    //Lizenz abrufen als Admin
    @GetMapping("/{licenseKey}") //{licenseKey} ist ein Paramater für den Pfad
    public ResponseEntity<License> getLicense(@PathVariable String licenseKey) { //@PathVariable nimmt den Wert aus der URL

        //Lizenz mit dem Schlüssel aus der Datenbank finden
        Optional<License> license = licenseService.getLicenseByKey(licenseKey);
        //Wenn erfolgreich aufgerufen dann Http Code 200 OK, wenn nicht dann 404 für nicht gefunden
        return license.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    //Endpoint 4 Alle Lizenzen abrufen

    //URL GET http://undermain.de/api/v1/licenses

    @GetMapping
    public ResponseEntity<List<License>> getAllLicenses() {

        List<License> licenses = licenseService.getAllLicenses();

        return ResponseEntity.ok(licenses); //Http Code 200 mit den Lizenzen

    }

    //Endpoint 5 Lizenzen mit Email abrufen

    //URL GET http://undermain.de/api/v1/licenses/customer/irmak.emre@web.de

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<License>> getLicensesByCustomer(@PathVariable String email) {

        List<License> licenses = licenseService.getLicensesByCustomer(email);

        return ResponseEntity.ok(licenses);

    }

    //Endpoint 6 Lizenzen mit Pluginnamen abrufen

    //URL GET http://undermain.de/api/v1/licenses/plugin/ChatSystem

    @GetMapping("/plugin/{pluginName}")
    public ResponseEntity<List<License>> getLicensesByPlugin(@PathVariable String pluginName) {

        List<License> licenses = licenseService.getLicensesByPlugin(pluginName);

        return ResponseEntity.ok(licenses);
    }

    //Endpoint 7 Lizenz widerrufen

    //URL PUT http://undermain.de/api/v1/licenses/ABCD-ABCD-ABCD-ABCD/revoke

    @PutMapping("/{licenseKey}/revoke")
    public ResponseEntity<Void> revokeLicense(@PathVariable String licenseKey) {

        boolean success = licenseService.revokeLicense(licenseKey);

        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build(); // Http Code 200 fpr OK wenn erfolgreich ansonsten 404 icht gefunden

    }

    //Endpoint 8 Lizenz sperren

    //URL PUT http://undermain.de/api/v1/licenses/ABCD-ABCD-ABCD-ABCD/suspend

    @PutMapping("/{licenseKey}/suspend")
    public ResponseEntity<Void> suspendLicense(@PathVariable String licenseKey) {

        boolean success = licenseService.suspendLicense(licenseKey);

        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    //Endpoint 9 Lizenz aktivieren

    //URL PUT http://undermain.de/api/v1/licenses/ABCD-ABCD-ABCD-ABCD/activate
    @PutMapping("/{licenseKey}/activate")
    public ResponseEntity<Void> activateLicense(@PathVariable String licenseKey) {
        boolean success = licenseService.activateLicense(licenseKey);

        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}