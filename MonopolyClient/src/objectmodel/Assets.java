//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.26 at 10:31:45 PM IST 
//


package objectmodel;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Assets complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Assets">
 *   &lt;complexContent>
 *     &lt;extension base="{}Group">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Asset" type="{}Asset"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Assets", propOrder = {
    "asset"
})
public class Assets
    extends Group
{

    @XmlElement(name = "Asset")
    protected List<CellBase> asset;

    /**
     * Gets the value of the asset property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asset property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsset().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Asset }
     * 
     * 
     */
    public List<CellBase> getAsset() {
        if (asset == null) {
            asset = new ArrayList<CellBase>();
        }
        return this.asset;
    }
}
