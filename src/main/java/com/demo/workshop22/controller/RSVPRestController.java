package com.demo.workshop22.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.workshop22.models.RSVP;
import com.demo.workshop22.services.RSVPService;

import org.springframework.web.bind.annotation.RequestParam;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path = "/api/rsvp", produces = MediaType.APPLICATION_JSON_VALUE)
public class RSVPRestController {
    @Autowired
    private RSVPService rsvpSvc;
    @GetMapping()
    public ResponseEntity<String> getAllCustomer(@RequestParam(required = false) String q) throws Exception {
        // Query the database for the rsvps
        List<RSVP> rsvps = rsvpSvc.getAllRSVP(q);
        // Build the result
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (RSVP c : rsvps)
            arrBuilder.add(c.toJSON());
        JsonArray result = arrBuilder.build();
        System.out.println("" + result.toString());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }
    @GetMapping(path = "/count")
    public ResponseEntity<String> countRSVP() {
        JsonObject resp;
        // Query the database for the rsvps
        Integer totalCntRsvps = rsvpSvc.getTotalRSVP();
        resp = Json.createObjectBuilder()
                .add("total_cnt", totalCntRsvps)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(resp.toString());
    }
// takes in a string json value, produce an object(rsvp)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postRSVP(@RequestBody String json) {
        RSVP rsvp = null;
        RSVP rsvpResult = null;
        JsonObject resp;
        //create a json
        try {
            rsvp = RSVP.create(json);
        } catch (Exception e) {
            e.printStackTrace();
            resp = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(resp.toString());
        }
        rsvpResult = rsvpSvc.insertRsvp(rsvp);
        resp = Json.createObjectBuilder()
                .add("rsvpId", rsvpResult.getId())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(resp.toString());
    }
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putRSVP(@RequestBody String json) {
        RSVP rsvp = null;
        boolean rsvpResult = false;
        JsonObject resp;
        try {
            rsvp = RSVP.create(json);
        } catch (Exception e) {
            e.printStackTrace();
            resp = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(resp.toString());
        }
        rsvpResult = rsvpSvc.updateRSVP(rsvp);
        resp = Json.createObjectBuilder()
                .add("updated", rsvpResult)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(resp.toString());
    }
}
