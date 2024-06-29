package dev.brianmiller.javafxchallengecontactsapp.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;

/**
 * @author Brian A. Miller, except where noted
 */
public class ContactData {

    private static final String CONTACTS_FILE = "contacts.xml";

    private static final String CONTACT = "contact";
    private static final String ID="id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String EMAIL_ADDRESS = "email_address";
    private static final String NOTES = "notes";

    private final ObservableList<Contact> contacts;
    // private final ObservableMap<Long, Contact> contactsMap;

    private static final ContactData instance = new ContactData();

    private ContactData() {
        contacts = FXCollections.observableArrayList();
    }

    public static ContactData getInstance() {
        return instance;
    }

    public void addContact(Contact newContact) {
        if (!contacts.contains(newContact)) {
            contacts.add(newContact);
            System.out.println("Added contact to ContactData.contacts: " + newContact);
            // contactsMap.put(newContact.getUniqueId(), newContact);
        }
    }

    public void deleteContact(Contact contactToRemove) {
        contacts.remove(contactToRemove);
        // contactsMap.remove(contactToRemove.getUniqueId());
    }

    public ObservableList<Contact> getContacts() {
        return contacts;
        //return FXCollections.observableArrayList(contactsMap.values());
    }
//    public ObservableMap<Long, Contact> getContactsMap() {
//        return contactsMap;
//    }

    /**
     * Load contacts from XML file
     *
     * @author Tim Buchalka (instructor of Udemy course
     * Java 17 Masterclass) (Learning Programming Academy)
     *
     * Modified by Brian A. Miller (June 28, 2024)
     */
    public void loadContacts() {
        System.out.println("Entering loadContacts()");
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(CONTACTS_FILE);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Contact contact = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have a contact item, we create a new contact
                    if (startElement.getName().getLocalPart().equals(CONTACT)) {
                        contact = new Contact();
                        System.out.println("Constructed new Contact() in loadContacts()");
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(ID)) {
                            event = eventReader.nextEvent();
                            if (contact != null) {
                                contact.setId(Long.valueOf(event.asCharacters().getData()));
                            }
                            continue;
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(FIRST_NAME)) {
                            event = eventReader.nextEvent();
                            if (contact != null) {
                                contact.setFirstName(event.asCharacters().getData());
                            }
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(LAST_NAME)) {
                        event = eventReader.nextEvent();
                        if (contact != null) {
                            contact.setLastName(event.asCharacters().getData());
                        }
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(PHONE_NUMBER)) {
                        event = eventReader.nextEvent();
                        if (contact != null) {
                            try {
                                contact.setPhoneNumber(event.asCharacters().getData());
                            } catch (ClassCastException noPhoneNumberReadFromFile) {
                                contact.setPhoneNumber("");
                            }
                        }
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(EMAIL_ADDRESS)) {
                        event = eventReader.nextEvent();
                        if (contact != null) {
                            try {
                                contact.setEmailAddress(event.asCharacters().getData());
                            } catch (ClassCastException noEmailAddressReadFromFile) {
                                contact.setEmailAddress("");
                            }
                        }
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(NOTES)) {
                        event = eventReader.nextEvent();
                        if (contact != null) {
                            try {
                                contact.setNotes(event.asCharacters().getData());
                            } catch (ClassCastException noNotesReadFromFile) {
                                contact.setNotes("");
                            }
                        }
                        continue;
                    }
                }

                // If we reach the end of a contact element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(CONTACT)) {
                        contacts.add(contact);
                        // contactsMap.put(contact.getUniqueId(), contact);
                    }
                }
            }
        } catch (FileNotFoundException | XMLStreamException ex) {
            ex.printStackTrace(System.err);
        }
    } // end of loadContacts() method

    /**
     * Save contacts to XML file
     *
     * @author Tim Buchalka (instructor of Udemy course
     * Java 17 Masterclass) (Learning Programming Academy)
     */
    public void saveContacts() {

        try {
            // create an XMLOutputFactory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // create XMLEventWriter
            XMLEventWriter eventWriter = outputFactory
                    .createXMLEventWriter(new FileOutputStream(CONTACTS_FILE));
            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            XMLEvent end = eventFactory.createDTD("\n");
            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement contactsStartElement = eventFactory.createStartElement("",
                    "", "contacts");
            eventWriter.add(contactsStartElement);
            eventWriter.add(end);

            for (Contact contact: contacts) {
            // for (Contact contact : contactsMap.values()) {
                saveContact(eventWriter, eventFactory, contact);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "contacts"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Problem with Contacts file: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        catch (XMLStreamException e) {
            System.out.println("Problem writing contact: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * @author Tim Buchalka
     */
    private void saveContact(XMLEventWriter eventWriter, XMLEventFactory eventFactory, Contact contact)
            throws FileNotFoundException, XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // create contact open tag
        StartElement configStartElement = eventFactory.createStartElement("",
                "", CONTACT);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        createNode(eventWriter, ID, contact.getId().toString());
        createNode(eventWriter, FIRST_NAME, contact.getFirstName());
        createNode(eventWriter, LAST_NAME, contact.getLastName());
        createNode(eventWriter, PHONE_NUMBER, contact.getPhoneNumber());
        createNode(eventWriter, EMAIL_ADDRESS, contact.getEmailAddress());
        createNode(eventWriter, NOTES, contact.getNotes());

        eventWriter.add(eventFactory.createEndElement("", "", CONTACT));
        eventWriter.add(end);
    }

    /**
     * @author Tim Buchalka
     * @throws XMLStreamException if there is a problem writing XML
     */
    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    public void updateContact(Contact contact) {
        if ((contact == null) || (contact.getId() == null)) {
            System.err.println("Can not update contact");
            return;
        }
        // contactsMap.put(contact.getUniqueId(), contact);
        for (Contact c : contacts) {
            if (contact.getId().equals(c.getId())) {
                c.setFirstName(contact.getFirstName());
                c.setLastName(contact.getLastName());
                c.setPhoneNumber(contact.getPhoneNumber());
                c.setEmailAddress(contact.getEmailAddress());
                c.setNotes(contact.getNotes());
                System.out.println("Updated contact (ID=" + c.getId() + ") in ContactData");
            }
        }
    }

} // end of ContactData class
