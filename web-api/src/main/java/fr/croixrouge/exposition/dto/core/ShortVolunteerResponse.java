package fr.croixrouge.exposition.dto.core;

public class ShortVolunteerResponse {
    public Long id;
    public String firstName;
    public String lastName;

    public ShortVolunteerResponse(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
