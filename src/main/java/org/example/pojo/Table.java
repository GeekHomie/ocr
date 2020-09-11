package org.example.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Ethan
 * @version 1.0
 * @date 8/31 1:46
 */
public class Table {
    private String uuid;
    private String approvalFormNumber;
    private String jobNumber;
    private String name;
    private String travelReason;
    private String travelLocation;
    private String locationType;
    private int travelLocationType;
    private Date departureDate;
    private Date arrivalDate;
    private BigDecimal transportFee;
    private BigDecimal accommodationFee;
    private int employeeType ;
    private int travelDays ;
    private boolean travelDaysConsisted;
    private boolean nameConsisted;
    private boolean feeConsisted;
    private int transportAllowance;
    private int mealAllowance;
    private BigDecimal approvalFormFee;
    private BigDecimal allFee;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getApprovalFormNumber() {
        return approvalFormNumber;
    }

    public void setApprovalFormNumber(String approvalFormNumber) {
        this.approvalFormNumber = approvalFormNumber;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTravelReason() {
        return travelReason;
    }

    public void setTravelReason(String travelReason) {
        this.travelReason = travelReason;
    }

    public String getTravelLocation() {
        return travelLocation;
    }

    public void setTravelLocation(String travelLocation) {
        this.travelLocation = travelLocation;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public int getTravelLocationType() {
        return travelLocationType;
    }

    public void setTravelLocationType(int travelLocationType) {
        this.travelLocationType = travelLocationType;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public BigDecimal getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(BigDecimal transportFee) {
        this.transportFee = transportFee;
    }

    public BigDecimal getAccommodationFee() {
        return accommodationFee;
    }

    public void setAccommodationFee(BigDecimal accommodationFee) {
        this.accommodationFee = accommodationFee;
    }

    public int getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
    }

    public int getTravelDays() {
        return travelDays;
    }

    public void setTravelDays(int travelDays) {
        this.travelDays = travelDays;
    }

    public boolean isTravelDaysConsisted() {
        return travelDaysConsisted;
    }

    public void setTravelDaysConsisted(boolean travelDaysConsisted) {
        this.travelDaysConsisted = travelDaysConsisted;
    }

    public boolean isNameConsisted() {
        return nameConsisted;
    }

    public void setNameConsisted(boolean nameConsisted) {
        this.nameConsisted = nameConsisted;
    }

    public boolean isFeeConsisted() {
        return feeConsisted;
    }

    public void setFeeConsisted(boolean feeConsisted) {
        this.feeConsisted = feeConsisted;
    }

    public int getTransportAllowance() {
        return transportAllowance;
    }

    public void setTransportAllowance(int transportAllowance) {
        this.transportAllowance = transportAllowance;
    }

    public int getMealAllowance() {
        return mealAllowance;
    }

    public void setMealAllowance(int mealAllowance) {
        this.mealAllowance = mealAllowance;
    }

    public BigDecimal getApprovalFormFee() {
        return approvalFormFee;
    }

    public void setApprovalFormFee(BigDecimal approvalFormFee) {
        this.approvalFormFee = approvalFormFee;
    }

    public BigDecimal getAllFee() {
        return allFee;
    }

    public void setAllFee(BigDecimal allFee) {
        this.allFee = allFee;
    }

    @Override
    public String toString() {
        return "Table{" +
                "approvalFormNumber='" + approvalFormNumber + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", name=" + name +
                ", travelReason='" + travelReason + '\'' +
                ", travelLocation='" + travelLocation + '\'' +
                ", locationType='" + locationType + '\'' +
                ", travelLocationType=" + travelLocationType +
                ", departureDate=" + departureDate +
                ", arrivalDate=" + arrivalDate +
                ", transportFee=" + transportFee +
                ", accommodationFee=" + accommodationFee +
                ", employeeType=" + employeeType +
                ", travelDays=" + travelDays +
                ", travelDaysConsisted=" + travelDaysConsisted +
                ", nameConsisted=" + nameConsisted +
                ", feeConsisted=" + feeConsisted +
                ", transportAllowance=" + transportAllowance +
                ", mealAllowance=" + mealAllowance +
                ", approvalFormFee=" + approvalFormFee +
                ", allFee=" + allFee +
                '}';
    }
}