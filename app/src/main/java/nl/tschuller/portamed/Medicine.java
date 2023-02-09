package nl.tschuller.portamed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Objects;

/**
 * This class is primarily for storing information about a used medication, and for retrieving information about it by its rvg-number when it is first added.
 *
 * @author Thomas Schuller
 */
public class Medicine implements Externalizable {
    private String activeIngredient;
    private BigInteger rvgNumber;
    private String friendlyName;
    private String brand;
    private String reference;
    private String dosageForm;
    private Bitmap boxExample;

    /**
     * Data model for storing information about medications
     *
     * @param rvgNumber        the rvg-registration number of the medication
     * @param friendlyName     a user-recognizable name
     * @param activeIngredient the active ingrediënt used in the medication
     * @param brand            the name of the brand that makes the medication
     * @param reference        a reference for the medication (as obtained from the rvg-dataset)
     * @param dosageForm       a short string containing the way in which the medication will be dosed (eg. "pill")
     * @param boxExample       an image so the user can recognize the medication
     */
    public Medicine(BigInteger rvgNumber, String friendlyName, String activeIngredient, String brand, String reference, String dosageForm, Bitmap boxExample) {
        this.activeIngredient = activeIngredient;
        this.rvgNumber = rvgNumber;
        this.friendlyName = friendlyName;
        this.brand = brand;
        this.reference = reference;
        this.dosageForm = dosageForm;
        this.boxExample = boxExample;

        if (rvgNumber != null) retrieveFromRvg(false);
    }

    public Medicine(){

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

    public Bitmap getBoxExample() {
        return boxExample;
    }

    public void setBoxExample(Bitmap boxExample) {
        this.boxExample = boxExample;
    }

    @Override
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        byte[] bitmapBytes = new byte[0];
        if (this.boxExample != null) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boxExample.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
            bitmapBytes = byteStream.toByteArray();
        } else {
            bitmapBytes = null;
        }

        objectOutput.writeObject(new Serializable[]{this.activeIngredient,
                this.rvgNumber,
                this.friendlyName,
                this.brand,
                this.reference,
                this.dosageForm,
                bitmapBytes});
    }

    @Override
    public void readExternal(ObjectInput objectInput) throws ClassNotFoundException, IOException {
        Serializable[] sArr = (Serializable[]) objectInput.readObject();
        this.activeIngredient = (String) sArr[0];
        this.rvgNumber = (BigInteger) sArr[1];
        this.friendlyName = (String) sArr[2];
        this.brand = (String) sArr[3];
        this.reference = (String) sArr[4];
        this.dosageForm = (String) sArr[5];
        byte[] serializedBitmapBytes = (byte[]) sArr[6];

        if (serializedBitmapBytes == null) {
            this.boxExample = null;
        } else {
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(serializedBitmapBytes);
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            int b;
            while ((b = byteInStream.read()) != -1)
                byteOutStream.write(b);
            byte[] bitmapBytes = byteOutStream.toByteArray();
            this.boxExample = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        }
    }
}
