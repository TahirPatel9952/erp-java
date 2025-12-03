package com.erp.manufacturing.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "goods_receipt_notes")
@SQLDelete(sql = "UPDATE goods_receipt_notes SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsReceiptNote extends BaseEntity {

    @Column(name = "grn_number", nullable = false, unique = true, length = 30)
    private String grnNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Column(length = 20)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, PENDING_QC, QC_PASSED, QC_FAILED, COMPLETED

    @Column(name = "vehicle_no", length = 20)
    private String vehicleNo;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(name = "challan_no", length = 50)
    private String challanNo;

    @Column(name = "challan_date")
    private LocalDate challanDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "qc_notes", columnDefinition = "TEXT")
    private String qcNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qc_by")
    private User qcBy;

    @Column(name = "qc_at")
    private LocalDateTime qcAt;

    @OneToMany(mappedBy = "goodsReceiptNote", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GrnItem> items = new ArrayList<>();

    public void addItem(GrnItem item) {
        items.add(item);
        item.setGoodsReceiptNote(this);
    }

    public void removeItem(GrnItem item) {
        items.remove(item);
        item.setGoodsReceiptNote(null);
    }
}

