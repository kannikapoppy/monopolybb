//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.26 at 10:31:45 PM IST 
//


package objectmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for City complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="City">
 *   &lt;complexContent>
 *     &lt;extension base="{}CellBase">
 *       &lt;sequence>
 *         &lt;element name="SingleHousePrice" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="HouseToll">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="One" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="Two" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="Three" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Country" type="{}Country"/>
 *         &lt;element name="Owner" type="{}Player" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="HousesNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "City", propOrder = {
    "singleHousePrice",
    "houseToll",
    "country",
    "owner",
    "numberOfHouses"
})
public class City
    extends CellBase
{
    @XmlElement(name = "SingleHousePrice")
    protected int singleHousePrice;
    @XmlElement(name = "HouseToll", required = true)
    protected City.HouseToll houseToll;
    @XmlElement(name = "Country", required = true)
    protected Country country;
    @XmlElement(name = "Owner", required=false)
    protected PlayerDetails owner;
    @XmlElement(name = "NumberOfHouses", required=false)
    protected int numberOfHouses = 0;

    /**
     * Gets the value of the singleHousePrice property.
     * 
     */
    public int getSingleHousePrice() {
        return singleHousePrice;
    }

    /**
     * Gets the value of the houseToll property.
     * 
     * @return
     *     possible object is
     *     {@link City.HouseToll }
     *     
     */
    public City.HouseToll getHouseToll() {
        return houseToll;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link Country }
     *     
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Gets the owner of this city
     *
     */
    public PlayerDetails getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this city
     *
     */
    public void setOwner(PlayerDetails player) {
        owner = player;
        if (owner == null)
            numberOfHouses = 0;

    }

    /**
     * Gets the number of houses in the city
     *
     */
    public int GetNumberOfHouses() {
        return numberOfHouses;
    }

    /**
     * Build a house
     *
     */
    public void IncrementNumberOfHouses() {
        numberOfHouses++;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;all>
     *         &lt;element name="One" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="Two" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="Three" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {

    })
    public static class HouseToll {

        @XmlElement(name = "One")
        protected int one;
        @XmlElement(name = "Two")
        protected int two;
        @XmlElement(name = "Three")
        protected int three;

        /**
         * Gets the value of the one property.
         * 
         */
        public int getOne() {
            return one;
        }

        /**
         * Gets the value of the two property.
         * 
         */
        public int getTwo() {
            return two;
        }

        /**
         * Gets the value of the three property.
         * 
         */
        public int getThree() {
            return three;
        }
    }

}
