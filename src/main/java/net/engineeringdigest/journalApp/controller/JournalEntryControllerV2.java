package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.ent.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        try {
            List<JournalEntry> all = journalEntryService.getAll();
            if (all != null && !all.isEmpty()) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while retrieving journal entries", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntries){
        try{
            myEntries.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(myEntries);
            return new ResponseEntity<>(true,HttpStatus.CREATED) ;
        }catch(Exception e){
            return new ResponseEntity<>("An error occured while creating new Entry", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getJournalEntryOfById(@PathVariable ObjectId myId) {
        try {
            JournalEntry entry = journalEntryService.findById(myId)
                    .orElseThrow(() -> new RuntimeException("Journal entry not found with id: " + myId));
            return new ResponseEntity<>(entry, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception if necessary and throw it further
            throw new RuntimeException("An error occurred while retrieving the journal entry", e);
        }
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId) {
        try {
            Optional<JournalEntry> entry = journalEntryService.findById(myId);
            if (entry.isPresent()) {
                journalEntryService.deleteById(myId);
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Journal entry not found with id: " + myId, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Handle any other exceptions that might occur
            throw new RuntimeException("An error occurred while deleting the journal entry", e);
        }
    }



    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newEntries) {
        try {
            JournalEntry old = journalEntryService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Journal entry not found with id: " + id));

            if (!newEntries.getTitle().trim().isEmpty()) {
                old.setTitle(newEntries.getTitle());
            }
            if (!newEntries.getContent().trim().isEmpty()) {
                old.setContent(newEntries.getContent());
            }

            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating the journal entry", e);
        }
    }


}
