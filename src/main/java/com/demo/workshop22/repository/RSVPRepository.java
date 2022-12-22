package com.demo.workshop22.repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.demo.workshop22.models.RSVP;
import com.demo.workshop22.models.RSVPTotalCntMapper;

import static com.demo.workshop22.repository.Queries.*;

@Repository
public class RSVPRepository {
   
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private BigInteger primaryKeyVal;

    //Task 2 

    public List<RSVP> getAllRSVP(String q) {
        // prevent inheritance --- to create a completely new one
        final List<RSVP> rsvps = new LinkedList<>();
        // set every rowset as zero first
        SqlRowSet rs = null; 
        // perform the query
        System.out.println("Q>" + q);

        //if q is zero, search using SELECT_ALL_RSVP first, but if there's an input, it'll return results by name
        if (q == null) {
            rs = jdbcTemplate.queryForRowSet(SQL_SELECT_ALL_RSVP);
        } else {
            rs = jdbcTemplate.queryForRowSet(SQL_SEARCH_RSVP_BY_NAME, q);
        }


        while (rs.next()) {
            rsvps.add(RSVP.create(rs));
        }
        return rsvps;
    }
        
 // HANDLING TASK 2.3 
 public RSVP searchRSVPByName(String name) {
    // prevent inheritance
    final List<RSVP> rsvps = new LinkedList<>();
    // perform the query
    final SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_SEARCH_RSVP_BY_NAME, name);

    while (rs.next()) {
        rsvps.add(RSVP.create(rs));
    }
    return rsvps.get(0);
}


// HANDLING TASK 2.4
 // --- add new RSVP
public RSVP insertRSVP(final RSVP rsvp) {
    // final RSVP r = new RSVP(); -> redundant
    //template.query(SQL_INSERT_RSVP, null, null, null)

 // auto increment id #   
    KeyHolder keyholder = new GeneratedKeyHolder();
   
//                      below: functional callback; conn = connection; 
    jdbcTemplate.update(conn -> {
        // PreparedStatement that is established through the connection; 
        PreparedStatement ps = conn.prepareStatement(SQL_INSERT_RSVP,Statement.RETURN_GENERATED_KEYS);
                //      get the id (from rsvp) to fulfill the object 
        ps.setString(1, rsvp.getName());
        ps.setString(2, rsvp.getEmail());
        ps.setString(3, rsvp.getPhone());
        System.out.println("Confirmation date > " + rsvp.getConfirmationDate());
    //timestamp comes back as a string, converted into a timestamp <-- using JodaTime
        ps.setTimestamp(4, new Timestamp(rsvp.getConfirmationDate().toDateTime().getMillis()));
        ps.setString(5, rsvp.getComments());
        return ps;
    }, keyholder);

    primaryKeyVal = (BigInteger) keyholder.getKey();
    rsvp.setId(primaryKeyVal.intValue());
    return rsvp;
}


public boolean updateRSVP(final RSVP rsvp) {
    return jdbcTemplate.update(SQL_UPDATE_RSVP_BY_EMAIL,
            rsvp.getName(),
            rsvp.getEmail(),
            rsvp.getPhone(),
            new Timestamp(rsvp.getConfirmationDate().toDateTime().getMillis()),
            rsvp.getComments(),
            rsvp.getEmail()) > 0;
}


public Integer getTotalRSVP() {
    // perform the query                                     2nd paramter to do total mapping, 3rd-- define empty object
    List<RSVP> rsvps = jdbcTemplate.query(SQL_TOTAL_CNT_RSVP, new RSVPTotalCntMapper(), new Object[] {});

    return rsvps.get(0).getTotalCnt();
}

}
