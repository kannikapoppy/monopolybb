
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
 *         &lt;element name="playerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "playerName"
})
@XmlRootElement(name = "joinGame")
public class JoinGame {

    @XmlElementRef(name = "gameName", namespace = "http://monopoly", type = JAXBElement.class, required = false)
    protected JAXBElement<String> gameName;
    @XmlElementRef(name = "playerName", namespace = "http://monopoly", type = JAXBElement.class, required = false)
    protected JAXBElement<String> playerName;

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
     * Gets the value of the playerName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPlayerName() {
        return playerName;
    }

    /**
     * Sets the value of the playerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPlayerName(JAXBElement<String> value) {
        this.playerName = ((JAXBElement<String> ) value);
    }

}
