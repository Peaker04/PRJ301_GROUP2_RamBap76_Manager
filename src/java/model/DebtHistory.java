

package model;

import java.sql.Date;
import java.sql.Timestamp;


public class DebtHistory {
   private int id;
    private int shipperId;
    private java.sql.Date debtDate;
    private double oldAmount;
    private double newAmount;
    private String changeReason;
    private int changedBy;
    private java.sql.Timestamp changedAt;
    private String changerName;
    private String changerRole;

    public DebtHistory(int id, int shipperId, Date debtDate, double oldAmount, double newAmount, String changeReason, int changedBy, Timestamp changedAt, String changerName, String changerRole) {
        this.id = id;
        this.shipperId = shipperId;
        this.debtDate = debtDate;
        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
        this.changeReason = changeReason;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
        this.changerName = changerName;
        this.changerRole = changerRole;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public Date getDebtDate() {
        return debtDate;
    }

    public void setDebtDate(Date debtDate) {
        this.debtDate = debtDate;
    }

    public double getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(double oldAmount) {
        this.oldAmount = oldAmount;
    }

    public double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(double newAmount) {
        this.newAmount = newAmount;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public int getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(int changedBy) {
        this.changedBy = changedBy;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }

    public String getChangerName() {
        return changerName;
    }

    public void setChangerName(String changerName) {
        this.changerName = changerName;
    }

    public String getChangerRole() {
        return changerRole;
    }

    public void setChangerRole(String changerRole) {
        this.changerRole = changerRole;
    }

    @Override
    public String toString() {
        return "DebtHistory{" + "id=" + id + ", shipperId=" + shipperId + ", debtDate=" + debtDate + ", oldAmount=" + oldAmount + ", newAmount=" + newAmount + ", changeReason=" + changeReason + ", changedBy=" + changedBy + ", changedAt=" + changedAt + ", changerName=" + changerName + ", changerRole=" + changerRole + '}';
    }

    
    
}
