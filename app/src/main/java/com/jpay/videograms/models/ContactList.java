package com.jpay.videograms.models;

import com.jpay.videograms.utils.SyncStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by anguyen on 12/14/2015.
 */
public class ContactList {
    private HashMap<Integer, Contact> contactList;

    public ContactList() {
        this.contactList = new HashMap<>();
    }

    public ArrayList<Contact> GetListOfContact() {
        ArrayList<Contact> outList = new ArrayList<>();

        for(Map.Entry<Integer, Contact> entry : this.contactList.entrySet()) {
            outList.add(entry.getValue());
        }

        return outList;
    }

    public List<Videograms> GetListOfVideogramForContactKey(int key) {
        if(this.contactList.containsKey(key)) {
            Contact contact = this.contactList.get(key);

            return contact.getVideogrames();
        }

        return null;
    }

    public boolean updateVGInContactList(Videograms videograms) {
        for(Map.Entry<Integer, Contact> entry : this.contactList.entrySet()) {
            Contact contact = entry.getValue();
            if(contact.updateVGInContact(videograms)) {
                return true;
            }
        }

        return false;
    }

    /**
     * get videograms list based on contact id and sync status
     *
     * @param contactId
     * @param status
     * @return
     */
    public List<Videograms> GetListOfVideogramByContactId(String contactId, SyncStatus status) {
        ArrayList<Contact> contacts = GetListOfContact();

        for (Contact contact : contacts) {
            if(contact.id.equals(contactId)) {
                List<Videograms> videogramses = contact.getVideogrames();
                if(videogramses == null) {
                    return null;
                }

                List<Videograms> result = new ArrayList<>();
                for(Videograms videograms : videogramses) {
                    if(videograms.syncStatus == status) {
                        result.add(videograms);
                    }
                }

                return result;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        if(contactList == null || contactList.size() == 0) {
            return "Empty List";
        }

        Iterator<Map.Entry<Integer, Contact>> contactIterator = contactList.entrySet().iterator();
        String content = "";
        while(contactIterator.hasNext()) {
            Map.Entry<Integer, Contact> entry = contactIterator.next();

            content += "Index : " + entry.getKey() + " - Sender " + entry.getValue().name;
        }

        return content;
    }
}
