
package comm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GameDetailsResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GameDetailsResult">
 *   &lt;complexContent>
 *     &lt;extension base="{http://results.monopoly/xsd}MonopolyResult">
 *       &lt;sequence>
 *         &lt;element name="isAutomaticDiceRoll" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="joinedHumanPlayers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalComputerPlayers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="totalHumanPlayers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GameDetailsResult", namespace = "http://results.monopoly/xsd", propOrder = {
    "isAutomaticDiceRoll",
    "joinedHumanPlayers",
    "status",
    "totalComputerPlayers",
    "totalHumanPlayers"
})
public class GameDetailsResult
    extends MonopolyResult
{

    protected Boolean isAutomaticDiceRoll;
    protected Integer joinedHumanPlayers;
    @XmlElementRef(name = "status", namespace = "http://results.monopoly/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> status;
    protected Integer totalComputerPlayers;
    protected Integer totalHumanPlayers;

    /**
     * Gets the value of the isAutomaticDiceRoll property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAutomaticDiceRoll() {
        return isAutomaticDiceRoll;
    }

    /**
     * Sets the value of the isAutomaticDiceRoll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAutomaticDiceRoll(Boolean value) {
        this.isAutomaticDiceRoll = value;
    }

    /**
     * Gets the value of the joinedHumanPlayers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getJoinedHumanPlayers() {
        return joinedHumanPlayers;
    }

    /**
     * Sets the value of the joinedHumanPlayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setJoinedHumanPlayers(Integer value) {
        this.joinedHumanPlayers = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStatus(JAXBElement<String> value) {
        this.status = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the totalComputerPlayers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalComputerPlayers() {
        return totalComputerPlayers;
    }

    /**
     * Sets the value of the totalComputerPlayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalComputerPlayers(Integer value) {
        this.totalComputerPlayers = value;
    }

    /**
     * Gets the value of the totalHumanPlayers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalHumanPlayers() {
        return totalHumanPlayers;
    }

    /**
     * Sets the value of the totalHumanPlayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalHumanPlayers(Integer value) {
        this.totalHumanPlayers = value;
    }

}
