
package comm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gameName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="humanPlayers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="computerizedPlayers" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="useAutomaticDiceRoll" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "gameName",
    "humanPlayers",
    "computerizedPlayers",
    "useAutomaticDiceRoll"
})
@XmlRootElement(name = "startGame")
public class StartGame {

    @XmlElementRef(name = "gameName", namespace = "http://monopoly", type = JAXBElement.class, required = false)
    protected JAXBElement<String> gameName;
    protected Integer humanPlayers;
    protected Integer computerizedPlayers;
    protected Boolean useAutomaticDiceRoll;

    /**
     * Gets the value of the gameName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGameName() {
        return gameName;
    }

    /**
     * Sets the value of the gameName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGameName(JAXBElement<String> value) {
        this.gameName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the humanPlayers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHumanPlayers() {
        return humanPlayers;
    }

    /**
     * Sets the value of the humanPlayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHumanPlayers(Integer value) {
        this.humanPlayers = value;
    }

    /**
     * Gets the value of the computerizedPlayers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getComputerizedPlayers() {
        return computerizedPlayers;
    }

    /**
     * Sets the value of the computerizedPlayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setComputerizedPlayers(Integer value) {
        this.computerizedPlayers = value;
    }

    /**
     * Gets the value of the useAutomaticDiceRoll property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUseAutomaticDiceRoll() {
        return useAutomaticDiceRoll;
    }

    /**
     * Sets the value of the useAutomaticDiceRoll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseAutomaticDiceRoll(Boolean value) {
        this.useAutomaticDiceRoll = value;
    }

}
