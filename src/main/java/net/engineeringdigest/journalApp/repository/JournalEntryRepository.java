package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.ent.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.ObjectInput;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {

}
