
package com.axisrooms.mistay.generated.updatePrice;

import com.axisrooms.mistay.util.Constants;
import com.axisrooms.mistay.util.OccupancyNotSupportedException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Rate {
    //add more if required
    private String    SINGLE;
    private String    DOUBLE;
    private String    TWIN;
    private String    TRIPLE;
    private String    QUAD;
    private String    PENTA;
    private String    HEXA;
    private String    HEPTA;
    private String    OCTA;
    private String    NINE;
    private String    TEN;
    private String    ELEVEN;
    private String    TWELVE;
    private String    THIRTEEN;
    private String    FOURTEEN;
    private String    FIFTEEN;
    private String    SIXTEEN;
    private String    TWENTY;
    private String    THIRTY;
    private String    EXTRA_PERSON;
    private String    EXTRA_BED;
    private String    EXTRA_ADULT;
    private String    EXTRA_INFANT;
    private String    EXTRA_CHILD;
    private String    EXTRA_CHILD_BELOW_FIVE;
    private String    EXTRA_CHILD_ABOVE_FIVE;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate endDate;
    @JsonFormat(pattern = Constants.DATE_PATTERN)
    private LocalDate    startDate;

    public LocalDate getendDate() {
        return endDate;
    }

    public void setendDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getstartDate() {
        return startDate;
    }

    public void setstartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setPriceByOccupancyName(String occupancyName, String price) throws OccupancyNotSupportedException {
     switch (occupancyName.toLowerCase())
     {
        case "single": this.setSINGLE(price);
                        break;
        case "double": this.setDOUBLE(price);
                        break;
        case "twin":   this.setTWIN(price);
                        break;
        case "triple": this.setTRIPLE(price);
                        break;
        case "quad":   this.setQUAD(price);
                        break;
        case "penta":  this.setPENTA(price);
                        break;
        case "hexa":   this.setHEXA(price);
                        break;
        case "hepta":  this.setHEPTA(price);
                        break;
        case "octa":   this.setOCTA(price);
                        break;
        case "nine":   this.setNINE(price);
                        break;
        case "ten":    this.setTEN(price);
                        break;
        case "eleven":   this.setELEVEN(price);
                            break;
        case "twelve":   this.setTWELVE(price);
                            break;
        case "thirteen": this.setTHIRTEEN(price);
                            break;
        case "fourteen": this.setFOURTEEN(price);
                            break;
        case "fifteen":  this.setFIFTEEN(price);
                            break;
        case "sixteen":  this.setSIXTEEN(price);
                            break;
        case "twenty":   this.setTWENTY(price);
                            break;
        case "thirty":   this.setTHIRTY(price);
                            break;
        case "extraperson":  this.setEXTRA_PERSON(price);
                                break;
        case "extrabed":     this.setEXTRA_BED(price);
                                break;
        case "extraadult":   this.setEXTRA_ADULT(price);
                                break;
        case "extrainfant":  this.setEXTRA_INFANT(price);
                                break;
        case "extrachild":   this.setEXTRA_CHILD(price);
                                break;
        case "extrachildbelowfive":  this.setEXTRA_CHILD_BELOW_FIVE(price);
                                            break;
        case "extrachildabovefive":  this.setEXTRA_CHILD_ABOVE_FIVE(price);
                                            break;

        default:
            throw new OccupancyNotSupportedException(occupancyName+" occupancy is not supported");
     }
    }
}
