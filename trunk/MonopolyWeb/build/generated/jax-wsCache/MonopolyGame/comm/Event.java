
package comm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Event complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Event">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="boardSquareID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="eventMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eventType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="firstDiceResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="gameName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nextBoardSquareID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="paymemtFromUser" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="paymentAmount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="paymentToOrFromTreasury" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="paymentToPlayerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="playerMoved" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="playerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secondDiceResult" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="timeoutCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Event", namespace = "http://monopoly/xsd", propOrder = {
    "boardSquareID",
    "eventID",
    "eventMessage",
    "eventType",
    "firstDiceResult",
    "gameName",
    "nextBoardSquareID",
    "paymemtFromUser",
    "paymentAmount",
    "paymentToOrFromTreasury",
    "paymentToPlayerName",
    "playerMoved",
    "playerName",
    "secondDiceResult",
    "timeoutCount"
})
public class Event {

    protected Integer boardSquareID;
    protected Integer eventID;
    @XmlElementRef(name = "eventMessage", namespace = "http://monopoly/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> eventMessage;
    protected Integer eventType;
    protected Integer firstDiceResult;
    @XmlElementRef(name = "gameName", namespace = "http://monopoly/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> gameName;
    protected Integer nextBoardSquareID;
    protected Boolean paymemtFromUser;
    protected Integer paymentAmount;
    protected Boolean paymentToOrFromTreasury;
    @XmlElementRef(name = "paymentToPlayerName", namespace = "http://monopoly/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> paymentToPlayerName;
    protected Boolean playerMoved;
    @XmlElementRef(name = "playerName", namespace = "http://monopoly/xsd", type = JAXBElement.class, required = false)
    protected JAXBElement<String> playerName;
    protected Integer secondDiceResult;
    protected Integer timeoutCount;

    /**
     * Gets the value of the boardSquareID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBoardSquareID() {
        return boardSquareID;
    }

    /**
     * Sets the value of the boardSquareID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBoardSquareID(Integer value) {
        this.boardSquareID = value;
    }

    /**
     * Gets the value of the eventID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEventID() {
        return eventID;
    }

    /**
     * Sets the value of the eventID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEventID(Integer value) {
        this.eventID = value;
    }

    /**
     * Gets the value of the eventMessage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEventMessage() {
        return eventMessage;
    }

    /**
     * Sets the value of the eventMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEventMessage(JAXBElement<String> value) {
        this.eventMessage = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the eventType property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEventType() {
        return eventType;
    }

    /**
     * Sets the value of the eventType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEventType(Integer value) {
        this.eventType = value;
    }

    /**
     * Gets the value of the firstDiceResult property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFirstDiceResult() {
        return firstDiceResult;
    }

    /**
     * Sets the value of the firstDiceResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFirstDiceResult(Integer value) {
        this.firstDiceResult = value;
    }

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
     * Gets the value of the nextBoardSquareID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNextBoardSquareID() {
        return nextBoardSquareID;
    }

    /**
     * Sets the value of the nextBoardSquareID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNextBoardSquareID(Integer value) {
        this.nextBoardSquareID = value;
    }

    /**
     * Gets the value of the paymemtFromUser property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPaymemtFromUser() {
        return paymemtFromUser;
    }

    /**
     * Sets the value of the paymemtFromUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPaymemtFromUser(Boolean value) {
        this.paymemtFromUser = value;
    }

    /**
     * Gets the value of the paymentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the value of the paymentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPaymentAmount(Integer value) {
        this.paymentAmount = value;
    }

    /**
     * Gets the value of the paymentToOrFromTreasury property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPaymentToOrFromTreasury() {
        return paymentToOrFromTreasury;
    }

    /**
     * Sets the value of the paymentToOrFromTreasury property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPaymentToOrFromTreasury(Boolean value) {
        this.paymentToOrFromTreasury = value;
    }

    /**
     * Gets the value of the paymentToPlayerName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPaymentToPlayerName() {
        return paymentToPlayerName;
    }

    /**
     * Sets the value of the paymentToPlayerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPaymentToPlayerName(JAXBElement<String> value) {
        this.paymentToPlayerName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the playerMoved property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPlayerMoved() {
        return playerMoved;
    }

    /**
     * Sets the value of the playerMoved property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPlayerMoved(Boolean value) {
        this.playerMoved = value;
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

    /**
     * Gets the value of the secondDiceResult property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecondDiceResult() {
        return secondDiceResult;
    }

    /**
     * Sets the value of the secondDiceResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecondDiceResult(Integer value) {
        this.secondDiceResult = value;
    }

    /**
     * Gets the value of the timeoutCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTimeoutCount() {
        return timeoutCount;
    }

    /**
     * Sets the value of the timeoutCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTimeoutCount(Integer value) {
        this.timeoutCount = value;
    }

}
