package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.ent.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @GetMapping
    public List<JournalEntry> getAll(){
        return journalEntryService.getAll();
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntries){
        myEntries.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntries);
        return true;
    }

    @GetMapping("/id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId myId){
        return journalEntryService.findById(myId).orElse(null);
    }
    @DeleteMapping("/id/{myId}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId myId){
        journalEntryService.deletebById(myId);
        return true;
    }

    @PutMapping("/id/{id}")
    //this is a put methond that updates the exisitng entries`T
    public JournalEntry updateJournalEntry(@PathVariable ObjectId id,@RequestBody JournalEntry newEntries){
        JournalEntry old = journalEntryService.findById(id).orElse(null);
        if(old!=null){
            old.setTitle(newEntries.getTitle()!=null&& !newEntries.getTitle().equals("")? newEntries.getTitle() : old.getTitle());
            old.setContent(newEntries.getContent()!=null&& !newEntries.getContent().equals("")? newEntries.getContent() : old.getContent());
        }
        journalEntryService.saveEntry(old);
        return old;
    }
}
