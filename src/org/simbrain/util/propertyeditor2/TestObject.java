package org.simbrain.util.propertyeditor2;

import org.simbrain.util.StandardDialog;
import org.simbrain.util.UserParameter;
import org.simbrain.util.propertyeditor.ComboBoxWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

/**
 * For testing ReflectivePropertyEditor.
 *
 * @author Jeff Yoshimi
 */
public class TestObject implements EditableObject {

    /**
     * Color test
     */
    @UserParameter(label = "Color")
    Color theColor = Color.red;

    // Boolean tests
    boolean theBool = true;
    Boolean theBooleanObject = true;

    // String Test
    String theString = "testing";

    // Object number tests (TODO Name)
    Integer theIntObject = 1;
    Double theDoubleObject = 1.1;
    Float theFloatObject = .123213f;
//    Long theLongObject = 12321L;
//    Short theShortObject = 20;

    // Primitive number tests
    @UserParameter(label = "The int", description = "The int", minimumValue = -10, maximumValue = 10, defaultValue = "1", order = 1)
    int theInt = 1;
    double theDouble = .9092342;
    float theFloat = 0;
//    @UserParameter(label = "The long", description = "The long", minimumValue = -10, maximumValue = 10, defaultValue = "5", order = 1)
//    long theLong = 20L;
//    short theShort = 20; // TODO: Figure out about shorts...

    // Array test(s)
    double[] doubleArray = new double[] {.1, .2, .3, 4};

    // Enum / Combo Box test
    private TestEnum theEnum = TestEnum.FOUR;
    ComboBoxWrapper boxable;

    public enum TestEnum {
        ONE("One"), TWO("Two"), THREE("Three"), FOUR("Four"), FIVE("Five");

        private String name;

        private TestEnum(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    ;

    public String toString() {

        return "(Test Object) \n" +

            "The Enum: " + theEnum + "\n"
            + "The Color: " + theColor + "\n"
            + "The Boolean Object: "
            + theBooleanObject + "\n" + "The Boolean: "
            + theBool + "\n" + "The String: " + theString
            + "\n" + "The Integer Object: " + theIntObject
            + "\n" + "The Double Array: " + Arrays.toString(doubleArray)
            + "\n" + "The Double Object: " + theDoubleObject + "\n"
            + "The Float Object: " + theFloatObject + "\n" + "The Long Object: "
            + "The int: " + theInt + "\n" + "The double: " + theDouble + "\n"
            + "The float: " + theFloat + "\n";

        //TODO: Longs and shorts
    }

    /*
     * Wrap the enum object in a ComboBoxable wrapper
     *
     * @return the boxable
     */
    public ComboBoxWrapper getEnumeration() {
        return new ComboBoxWrapper() {

            public Object getCurrentObject() {
                return theEnum;
            }

            public Object[] getObjects() {
                return TestEnum.values();
            }

        };
    }

    /**
     * Sets the enumeration based on a boxable object.
     *
     * @param object the boxable to set
     */
    public void setEnumeration(ComboBoxWrapper object) {
        theEnum = (TestEnum) object.getCurrentObject();
    }

    /**
     * @return the theColor
     */
    public Color getTheColor() {
        return theColor;
    }

    /**
     * @param theColor the theColor to set
     */
    public void setTheColor(Color theColor) {
        this.theColor = theColor;
    }

    /**
     * @return the theBool
     */
    public boolean isTheBool() {
        return theBool;
    }

    /**
     * @param theBool the theBool to set
     */
    public void setTheBool(boolean theBool) {
        this.theBool = theBool;
    }

    /**
     * @return the theBooleanObject
     */
    public Boolean getTheBooleanObject() {
        return theBooleanObject;
    }

    /**
     * @param theBooleanObject the theBooleanObject to set
     */
    public void setTheBooleanObject(Boolean theBooleanObject) {
        this.theBooleanObject = theBooleanObject;
    }

    /**
     * @return the theString
     */
    public String getTheString() {
        return theString;
    }

    /**
     * @param theString the theString to set
     */
    public void setTheString(String theString) {
        this.theString = theString;
    }

    /**
     * @return the theIntObject
     */
    public Integer getTheIntObject() {
        return theIntObject;
    }

    /**
     * @param theIntObject the theIntObject to set
     */
    public void setTheIntObject(Integer theIntObject) {
        this.theIntObject = theIntObject;
    }

    /**
     * @return the theDoubleObject
     */
    public Double getTheDoubleObject() {
        return theDoubleObject;
    }

    /**
     * @param theDoubleObject the theDoubleObject to set
     */
    public void setTheDoubleObject(Double theDoubleObject) {
        this.theDoubleObject = theDoubleObject;
    }

    /**
     * @return the theFloatObject
     */
    public Float getTheFloatObject() {
        return theFloatObject;
    }

    /**
     * @param theFloatObject the theFloatObject to set
     */
    public void setTheFloatObject(Float theFloatObject) {
        this.theFloatObject = theFloatObject;
    }

//    /**
//     * @return the theLongObject
//     */
//    public Long getTheLongObject() {
//        return theLongObject;
//    }
//
//    /**
//     * @param theLongObject the theLongObject to set
//     */
//    public void setTheLongObject(Long theLongObject) {
//        this.theLongObject = theLongObject;
//    }
//
//    /**
//     * @return the theShortObject
//     */
//    public Short getTheShortObject() {
//        return theShortObject;
//    }
//
//    /**
//     * @param theShortObject the theShortObject to set
//     */
//    public void setTheShortObject(Short theShortObject) {
//        this.theShortObject = theShortObject;
//    }

    /**
     * @return the theInt
     */
    public int getTheInt() {
        return theInt;
    }

    /**
     * @param theInt the theInt to set
     */
    public void setTheInt(int theInt) {
        this.theInt = theInt;
    }

    /**
     * @return the theDouble
     */
    public double getTheDouble() {
        return theDouble;
    }

    /**
     * @param theDouble the theDouble to set
     */
    public void setTheDouble(double theDouble) {
        this.theDouble = theDouble;
    }

    /**
     * @return the theFloat
     */
    public float getTheFloat() {
        return theFloat;
    }

    /**
     * @param theFloat the theFloat to set
     */
    public void setTheFloat(float theFloat) {
        this.theFloat = theFloat;
    }

//    /**
//     * @return the theLong
//     */
//    public long getTheLong() {
//        return theLong;
//    }
//
//    /**
//     * @param theLong the theLong to set
//     */
//    public void setTheLong(long theLong) {
//        this.theLong = theLong;
//    }

//    /**
//     * @return the theShort
//     */
//    public short getTheShort() {
//        return theShort;
//    }
//
//    /**
//     * @param theShort the theShort to set
//     */
//    public void setTheShort(short theShort) {
//        this.theShort = theShort;
//    }

    /**
     * @return the doubleArray
     */
    public double[] getDoubleArray() {
        return doubleArray;
    }

    /**
     * @param doubleArray the doubleArray to set
     */
    public void setDoubleArray(double[] doubleArray) {
        this.doubleArray = doubleArray;
    }



    /**
     * Simple test routine.
     *
     * @param args not used.
     */
    public static void main(String[] args) {

        TestObject testObject = new TestObject();
        AnnotatedPropertyEditor editor = new AnnotatedPropertyEditor(testObject);
        StandardDialog dialog = editor.getDialog();
        dialog.pack();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent arg) {
                editor.commitChanges();
                System.out.println(testObject);
            }
        });
        dialog.setVisible(true);

    }


}
