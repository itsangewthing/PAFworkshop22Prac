package com.demo.workshop22.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.workshop22.models.RSVP;
import com.demo.workshop22.repository.RSVPRepository;

// collects all the name of the functions that are done in Repository.
@Service
//interface that calls the repository
public class RSVPService {
    
    @Autowired
    // firstly, state the functions for the repo.
    private RSVPRepository rsvprepo;

    // task 2.1 
    public List<RSVP> getAllRSVP(String q) throws Exception {
        return rsvprepo.getAllRSVP(q);
        
    }

    //define the insert
//task 2.3
    public RSVP insertRsvp(RSVP r) {
        return rsvprepo.insertRSVP(r);
    }
//task 2.4
    public Integer getTotalRSVP(){
        return rsvprepo.getTotalRSVP();
    }    
// task 2.2
    public RSVP searchRSVPByName(String name) throws Exception{
        return rsvprepo.searchRSVPByName(name);
    }

    public boolean updateRSVP(final RSVP rsvp) {
        return rsvprepo.updateRSVP(rsvp);
    }
}

// all of the tasks/functions must go back to the named repository . 