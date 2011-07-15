
package comm;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EventArrayResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventArrayResult">
 *   &lt;complexContent>
 *     &lt;extension base="{http://results.monopoly/xsd}MonopolyResult">
 *       &lt;sequence>
 *         &lt;element name="results" type="{http://monopoly/xsd}Event" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventArrayResult", namespace = "http://results.monopoly/xsd", propOrder = {
    "results"
})
public class EventArrayResult
    extends MonopolyResult
{

    @XmlElement(nillable = true)
    protected List<Event> results;

    /**
     * Gets the value of the results property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the results property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResults().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Event }
     * 
     * 
     */
    public List<Event> getResults() {
        if (results == null) {
            results = new ArrayList<Event>();
        }
        return this.results;
    }

}
