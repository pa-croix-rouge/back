package fr.croixrouge.exposition.dto.core;

public class LocalUnitStatsResponse {
    private int numberOfVolunteers;
    private int numberOfBeneficiaries;

    public LocalUnitStatsResponse() {
    }

    public LocalUnitStatsResponse(int numberOfVolunteers, int numberOfBeneficiaries) {
        this.numberOfVolunteers = numberOfVolunteers;
        this.numberOfBeneficiaries = numberOfBeneficiaries;
    }

    public int getNumberOfVolunteers() {
        return numberOfVolunteers;
    }

    public int getNumberOfBeneficiaries() {
        return numberOfBeneficiaries;
    }
}
