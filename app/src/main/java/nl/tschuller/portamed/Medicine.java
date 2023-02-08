package nl.tschuller.portamed;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Objects;

/**
 * This class is primarily for storing information about a used medication, and for retrieving information about it by its rvg-number when it is first added.
 *
 * @author Thomas Schuller
 */
public class Medicine {
    private String activeIngredient;
    private BigInteger rvgNumber;
    private String friendlyName;
    private String brand;
    private String reference;
    private String dosageForm;
    private Drawable boxExample;

    public Medicine(BigInteger rvgNumber, String friendlyName, String activeIngredient, String brand, String reference, String dosageForm, Drawable boxExample) {
        this.activeIngredient = activeIngredient;
        this.rvgNumber = rvgNumber;
        this.friendlyName = friendlyName;
        this.brand = brand;
        this.reference = reference;
        this.dosageForm = dosageForm;
        this.boxExample = boxExample;

        if (rvgNumber != null) retrieveFromRvg(false);
    }

    /**
     * This function is used to retrieve the details of the added medication by rvg-registration number from the rvg-dataset
     *
     * @param rvgNumber the registration number to retrieve details from
     * @return basic Medicine with the found details (activeIngredient, brand, reference and dosageForm) set to it, or null if the number has not been found.
     * @author Thomas Schuller
     */
    private static Medicine retrieveFromRvg(BigInteger rvgNumber) {
        //TODO: implement retrieval from rvg-dataset
        return new Medicine(null, null, null, null, null, null, null);
    }

    /**
     * Uses retrieveFromRvg to automatically sets the applicable fields by querying the rvg-dataset
     *
     * @param override whether the function should change the fields to the ones retrieved from the dataset if they are not null
     */
    public void retrieveFromRvg(boolean override) {
        Medicine dataset = retrieveFromRvg(rvgNumber);
        for (Field field : this.getClass().getDeclaredFields()) {

            //make sure we have access to the field before fetching its value
            //TODO: test without field.setAccessible
            boolean accessible = field.isAccessible();
            if (!accessible) field.setAccessible(true);

            try {
                Object thisField = field.get(this);
                Object dataField = field.get(dataset);
                boolean imageField = field.getType().equals(Image.class);
                if ((dataField != null && !Objects.equals(dataField, "")) && (override || thisField == null || (!imageField && Objects.equals(thisField, "")))) {
                    field.set(this, field.get(dataset));
                }
            } catch (IllegalAccessException e) {
                System.err.println("Error trying to get Medicine field \"" + field.getName() + "\"'s value from dataset: " + e);
            }

            //revert the accessibility of the field to its original state
            if (!accessible) field.setAccessible(false);
        }

        if (activeIngredient == null) activeIngredient = dataset.activeIngredient;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public BigInteger getRvgNumber() {
        return rvgNumber;
    }

    public void setRvgNumber(BigInteger rvgNumber) {
        this.rvgNumber = rvgNumber;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getReference() {
        return reference;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public Drawable getBoxExample() {
        return boxExample;
    }

    public void setBoxExample(Drawable boxExample) {
        this.boxExample = boxExample;
    }
}
