//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.26 at 10:31:45 PM IST 
//


package objectmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BonusCard complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BonusCard">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Money"/>
 *             &lt;enumeration value="Jail Pass"/>
 *             &lt;enumeration value="MoveTo"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="Amount" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="Bank" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="MoveTo">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Chance"/>
 *             &lt;enumeration value="Community Chest"/>
 *             &lt;enumeration value="Jail"/>
 *             &lt;enumeration value="Go"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="GetGoCash" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BonusCard", propOrder = {
    "message"
})
public class BonusCard {

    @XmlElement(name = "Message", required = true)
    protected String message;
    @XmlAttribute(name = "ID", required = true)
    protected int id;
    @XmlAttribute(name = "Type", required = true)
    protected String type;
    @XmlAttribute(name = "Amount")
    protected Integer amount;
    @XmlAttribute(name = "Bank")
    protected Boolean bank;
    @XmlAttribute(name = "MoveTo")
    protected String moveTo;
    @XmlAttribute(name = "GetGoCash")
    protected Boolean getGoCash;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setID(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAmount(Integer value) {
        this.amount = value;
    }

    /**
     * Gets the value of the bank property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBank() {
        return bank;
    }

    /**
     * Sets the value of the bank property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBank(Boolean value) {
        this.bank = value;
    }

    /**
     * Gets the value of the moveTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoveTo() {
        return moveTo;
    }

    /**
     * Sets the value of the moveTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoveTo(String value) {
        this.moveTo = value;
    }

    /**
     * Gets the value of the getGoCash property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGetGoCash() {
        return getGoCash;
    }

    /**
     * Sets the value of the getGoCash property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGetGoCash(Boolean value) {
        this.getGoCash = value;
    }

}
