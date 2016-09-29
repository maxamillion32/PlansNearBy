package com.arroyo.b.plansnearby.Models.EventBriteModels;

/**
 * Created by nolbertoarroyo on 8/20/16.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TicketClass {

    @SerializedName("cost")
    @Expose
    private Cost cost;
    @SerializedName("fee")
    @Expose
    private Fee fee;
    @SerializedName("tax")
    @Expose
    private Tax tax;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("donation")
    @Expose
    private Boolean donation;
    @SerializedName("free")
    @Expose
    private Boolean free;
    @SerializedName("minimum_quantity")
    @Expose
    private Integer minimumQuantity;
    @SerializedName("maximum_quantity")
    @Expose
    private Object maximumQuantity;
    @SerializedName("maximum_quantity_per_order")
    @Expose
    private Integer maximumQuantityPerOrder;
    @SerializedName("on_sale_status")
    @Expose
    private String onSaleStatus;
    @SerializedName("sales_end")
    @Expose
    private String salesEnd;
    @SerializedName("include_fee")
    @Expose
    private Boolean includeFee;
    @SerializedName("hide_description")
    @Expose
    private Boolean hideDescription;
    @SerializedName("variants")
    @Expose
    private List<Object> variants = new ArrayList<Object>();
    @SerializedName("has_pdf_ticket")
    @Expose
    private Object hasPdfTicket;
    @SerializedName("sales_channels")
    @Expose
    private Object salesChannels;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     *
     * @return
     * The cost
     */
    public Cost getCost() {
        return cost;
    }

    /**
     *
     * @param cost
     * The cost
     */
    public void setCost(Cost cost) {
        this.cost = cost;
    }

    /**
     *
     * @return
     * The fee
     */
    public Fee getFee() {
        return fee;
    }

    /**
     *
     * @param fee
     * The fee
     */
    public void setFee(Fee fee) {
        this.fee = fee;
    }

    /**
     *
     * @return
     * The tax
     */
    public Tax getTax() {
        return tax;
    }

    /**
     *
     * @param tax
     * The tax
     */
    public void setTax(Tax tax) {
        this.tax = tax;
    }

    /**
     *
     * @return
     * The resourceUri
     */
    public String getResourceUri() {
        return resourceUri;
    }

    /**
     *
     * @param resourceUri
     * The resource_uri
     */
    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The description
     */
    public Object getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(Object description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The donation
     */
    public Boolean getDonation() {
        return donation;
    }

    /**
     *
     * @param donation
     * The donation
     */
    public void setDonation(Boolean donation) {
        this.donation = donation;
    }

    /**
     *
     * @return
     * The free
     */
    public Boolean getFree() {
        return free;
    }

    /**
     *
     * @param free
     * The free
     */
    public void setFree(Boolean free) {
        this.free = free;
    }

    /**
     *
     * @return
     * The minimumQuantity
     */
    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    /**
     *
     * @param minimumQuantity
     * The minimum_quantity
     */
    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    /**
     *
     * @return
     * The maximumQuantity
     */
    public Object getMaximumQuantity() {
        return maximumQuantity;
    }

    /**
     *
     * @param maximumQuantity
     * The maximum_quantity
     */
    public void setMaximumQuantity(Object maximumQuantity) {
        this.maximumQuantity = maximumQuantity;
    }

    /**
     *
     * @return
     * The maximumQuantityPerOrder
     */
    public Integer getMaximumQuantityPerOrder() {
        return maximumQuantityPerOrder;
    }

    /**
     *
     * @param maximumQuantityPerOrder
     * The maximum_quantity_per_order
     */
    public void setMaximumQuantityPerOrder(Integer maximumQuantityPerOrder) {
        this.maximumQuantityPerOrder = maximumQuantityPerOrder;
    }

    /**
     *
     * @return
     * The onSaleStatus
     */
    public String getOnSaleStatus() {
        return onSaleStatus;
    }

    /**
     *
     * @param onSaleStatus
     * The on_sale_status
     */
    public void setOnSaleStatus(String onSaleStatus) {
        this.onSaleStatus = onSaleStatus;
    }

    /**
     *
     * @return
     * The salesEnd
     */
    public String getSalesEnd() {
        return salesEnd;
    }

    /**
     *
     * @param salesEnd
     * The sales_end
     */
    public void setSalesEnd(String salesEnd) {
        this.salesEnd = salesEnd;
    }

    /**
     *
     * @return
     * The includeFee
     */
    public Boolean getIncludeFee() {
        return includeFee;
    }

    /**
     *
     * @param includeFee
     * The include_fee
     */
    public void setIncludeFee(Boolean includeFee) {
        this.includeFee = includeFee;
    }

    /**
     *
     * @return
     * The hideDescription
     */
    public Boolean getHideDescription() {
        return hideDescription;
    }

    /**
     *
     * @param hideDescription
     * The hide_description
     */
    public void setHideDescription(Boolean hideDescription) {
        this.hideDescription = hideDescription;
    }

    /**
     *
     * @return
     * The variants
     */
    public List<Object> getVariants() {
        return variants;
    }

    /**
     *
     * @param variants
     * The variants
     */
    public void setVariants(List<Object> variants) {
        this.variants = variants;
    }

    /**
     *
     * @return
     * The hasPdfTicket
     */
    public Object getHasPdfTicket() {
        return hasPdfTicket;
    }

    /**
     *
     * @param hasPdfTicket
     * The has_pdf_ticket
     */
    public void setHasPdfTicket(Object hasPdfTicket) {
        this.hasPdfTicket = hasPdfTicket;
    }

    /**
     *
     * @return
     * The salesChannels
     */
    public Object getSalesChannels() {
        return salesChannels;
    }

    /**
     *
     * @param salesChannels
     * The sales_channels
     */
    public void setSalesChannels(Object salesChannels) {
        this.salesChannels = salesChannels;
    }

    /**
     *
     * @return
     * The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     *
     * @param eventId
     * The event_id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

}
