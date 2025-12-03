package com.erp.manufacturing.entity;

import com.erp.manufacturing.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "delivery_challans")
@SQLDelete(sql = "UPDATE delivery_challans SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryChallan extends BaseEntity {

    @Column(name = "challan_number", nullable = false, unique = true, length = 30)
    private String challanNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "so_id", nullable = false)
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private DeliveryStatus status = DeliveryStatus.DRAFT;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "shipping_city", length = 50)
    private String shippingCity;

    @Column(name = "shipping_state", length = 50)
    private String shippingState;

    @Column(name = "shipping_pincode", length = 10)
    private String shippingPincode;

    @Column(name = "vehicle_no", length = 20)
    private String vehicleNo;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(name = "driver_phone", length = 20)
    private String driverPhone;

    @Column(name = "transporter_name", length = 100)
    private String transporterName;

    @Column(name = "lr_no", length = 50)
    private String lrNo;

    @Column(name = "lr_date")
    private LocalDate lrDate;

    @Column(name = "eway_bill_no", length = 50)
    private String ewayBillNo;

    @Column(name = "eway_bill_date")
    private LocalDate ewayBillDate;

    @Column(name = "dispatched_at")
    private LocalDateTime dispatchedAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "received_by", length = 100)
    private String receivedBy;

    @Column(name = "receiver_phone", length = 20)
    private String receiverPhone;

    @Column(name = "proof_of_delivery_url", length = 255)
    private String proofOfDeliveryUrl;

    @Column(name = "signature_url", length = 255)
    private String signatureUrl;

    @Column(name = "delivery_notes", columnDefinition = "TEXT")
    private String deliveryNotes;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @OneToMany(mappedBy = "deliveryChallan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DeliveryChallanItem> items = new ArrayList<>();

    public void addItem(DeliveryChallanItem item) {
        items.add(item);
        item.setDeliveryChallan(this);
    }

    public void removeItem(DeliveryChallanItem item) {
        items.remove(item);
        item.setDeliveryChallan(null);
    }
}

