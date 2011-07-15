
package comm;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PlayerDetailsResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PlayerDetailsResult">
 *   &lt;complexContent>
 *     &lt;extension base="{http://results.monopoly/xsd}MonopolyResult">
 *       &lt;sequence>
 *         &lt;element name="isActive" type="{http://www.w3.org/2001/XMLSchema}boolean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isHumans" type="{http://www.w3.org/2001/XMLSchema}boolean" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="money" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="names" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlayerDetailsResult", namespace = "http://results.monopoly/xsd", propOrder = {
    "isActive",
    "isHumans",
    "money",
    "names"
})
public class PlayerDetailsResult
    extends MonopolyResult
{

    @XmlElement(nillable = true)
    protected List<Boolean> isActive;
    @XmlElement(nillable = true)
    protected List<Boolean> isHumans;
    @XmlElement(nillable = true)
    protected List<Integer> money;
    @XmlElement(nillable = true)
    protected List<String> names;

    /**
     * Gets the value of the isActive property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the isActive property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIsActive().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getIsActive() {
        if (isActive == null) {
            isActive = new ArrayList<Boolean>();
        }
        return this.isActive;
    }

    /**
     * Gets the value of the isHumans property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the isHumans property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIsHumans().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Boolean }
     * 
     * 
     */
    public List<Boolean> getIsHumans() {
        if (isHumans == null) {
            isHumans = new ArrayList<Boolean>();
        }
        return this.isHumans;
    }

    /**
     * Gets the value of the money property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the money property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMoney().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getMoney() {
        if (money == null) {
            money = new ArrayList<Integer>();
        }
        return this.money;
    }

    /**
     * Gets the value of the names property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the names property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNames().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNames() {
        if (names == null) {
            names = new ArrayList<String>();
        }
        return this.names;
    }

}
