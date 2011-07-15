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
 * <p>Java class for Asset complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Asset">
 *   &lt;complexContent>
 *     &lt;extension base="{}CellBase">
 *       &lt;sequence>
 *         &lt;element name="Group" type="{}Assets"/>
 *         &lt;element name="GroupToll" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Owner" type="{}Player" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Asset", propOrder = {
    "group",
    "groupToll",
    "owner"
})
public class Asset
    extends CellBase
{

    @XmlElement(name = "Group", required = true)
    protected Assets group;
    @XmlElement(name = "GroupToll")
    protected int groupToll;
    @XmlElement(name = "Owner", required=false)
    protected PlayerDetails owner;

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link Assets }
     *     
     */
    public Assets getGroup() {
        return group;
    }

    /**
     * Gets the value of the groupToll property.
     * 
     */
    public int getGroupToll() {
        return groupToll;
    }

    /**
     * Gets the owner of this asset
     *
     */
    public PlayerDetails getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this asset
     *
     */
    public void setOwner(PlayerDetails player) {
        owner = player;
    }
}