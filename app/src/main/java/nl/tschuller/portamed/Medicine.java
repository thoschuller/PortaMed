package nl.tschuller.portamed;

import android.media.Image;

import java.math.BigInteger;

/**
 * This class is primarily for storing information about a used medication, and for retrieving information about it by its rvg-number when it is first added.
 */
public class Medicine {
    String activeIngredient;
    BigInteger rvgNumber;
    String friendlyName;
    String brand;
    String reference;
    String dosageForm;
    Image boxExample;

    public Medicine(BigInteger rvgNumber, String friendlyName, String activeIngredient, String brand, String reference, String dosageForm, Image boxExample) {
        this.activeIngredient = activeIngredient;
        this.rvgNumber = rvgNumber;
        this.friendlyName = friendlyName;
        this.brand = brand;
        this.reference = reference;
        this.dosageForm = dosageForm;
        this.boxExample = boxExample;

        if(rvgNumber != null) retrieveFromRvg();
    }

    /**
     * This function is used to retrieve the details of the added medication by rvg-registration number from the rvg-dataset
     *
     * @param rvgNumber the registration number to retrieve details from
     * @return a basic Medicine-object with the found details (activeIngredient, brand, reference and dosageForm) set to it, or null if the number has not been found.
     */
    private static Medicine retrieveFromRvg(BigInteger rvgNumber){
        //TODO: implement retrieval from rvg-dataset
        return null;
    }

    /**
     * Overloaded retrieveFromRvg for own rvg-number
     * @return a basic Medicine-object with the found details (activeIngredient, brand, reference and dosageForm) set to it, or null if the number has not been found.
     */
    private Medicine retrieveFromRvg(){
        return retrieveFromRvg(rvgNumber);
    }


}
